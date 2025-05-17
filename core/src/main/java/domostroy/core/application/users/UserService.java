package domostroy.core.application.users;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangePasswordDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangeUserInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.AnotherUserDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.offers.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                user.getPhoneNumber()
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
                user.getPhoneNumber()
        );
    }

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

        user.setPassword(dto.newPassword());
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
}
