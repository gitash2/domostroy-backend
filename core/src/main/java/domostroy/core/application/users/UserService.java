package domostroy.core.application.users;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangePasswordDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangeUserInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.AdminUserDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.AnotherUserDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.offers.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDTO getCurrentUserData(Long userId) {
        User user = userRepository.findById(userId);
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole().getRole().toString(),
                user.getIsBanned()
        );
    }


    public AnotherUserDTO getUserData(Long userId) {
        User user = userRepository.findById(userId);
        return new AnotherUserDTO(
                userId,
                user.getFirstName(),
                user.getLastName(),
                offerRepository.getMyOffersCount(user.getId()),
                user.getCreatedAt(),
                user.getPhoneNumber(),
                user.getRole().getRole().toString(),
                user.getIsBanned()
        );
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userRepository.findById(userId);
        if (!passwordEncoder.matches(dto.previousPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный старый пароль");
        }

        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Новый пароль должен отличаться от текущего"
            );
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void changeUserInfo(Long userId, ChangeUserInfoDTO dto) {
        User user = userRepository.findById(userId);
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());
        userRepository.save(user);
    }

    @Transactional
    public void banUser(Long userId, Boolean isBanned) {
        User user = userRepository.findById(userId);
        user.setIsBanned(isBanned);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public Page<AdminUserDTO> searchUsers(String query, Pageable pageable, UserDetails user) {
        User me = userRepository.findByEmail(user.getUsername());
        Page<User> users = userRepository.searchUsers(query, pageable);
        List<User> filteredUsers = users
                .getContent()
                .stream()
                .filter(it -> !it.getId().equals(me.getId()))
                .toList();

        Map<Long, Integer> userIdToNumberOfOffers = filteredUsers.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        it -> offerRepository.getMyOffersCount(it.getId())
                ));

        List<AdminUserDTO> dtoList = filteredUsers.stream()
                .map(it -> new AdminUserDTO(
                        it.getId(),
                        (it.getFirstName() + " " + (it.getLastName() != null ? it.getLastName() : "")).trim(),
                        it.getEmail(),
                        it.getPhoneNumber(),
                        userIdToNumberOfOffers.getOrDefault(it.getId(), 0),
                        it.getIsBanned(),
                        it.getRole().getRole().toString(),
                        it.getCreatedAt()
                ))
                .toList();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}
