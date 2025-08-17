package domostroy.auth.users.service;

import domostroy.auth.brokers.ConfirmationCodeSender;
import domostroy.auth.dto.ConfirmRequest;
import domostroy.auth.dto.SignUpRequest;
import domostroy.auth.dto.VerificationData;
import domostroy.auth.dto.users.input.ChangePasswordDTO;
import domostroy.auth.dto.users.input.ChangeUserInfoDTO;
import domostroy.auth.dto.users.output.UserNotificationFlag;
import domostroy.auth.exceptions.RoleNotFoundException;
import domostroy.auth.exceptions.UserAlreadyExistsException;
import domostroy.auth.mapper.UserMapper;
import domostroy.auth.users.model.Role;
import domostroy.auth.users.model.RoleValue;
import domostroy.auth.users.model.User;
import domostroy.auth.users.repository.dao.RoleRepository;
import domostroy.auth.users.repository.dao.UserRepository;
import domostroy.dto.UserDTO;
import domostroy.events.mail.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;
    private final ConfirmationCodeSender confirmationCodeSender;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public UserDTO save(UserDTO userDTO) {
        return userMapper.toDTO(userRepository.save(
                        userMapper.toEntity(userDTO)
                )
        );
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userMapper.toDTO(userRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        return userMapper.toDTO(userRepository.findByEmail(email));
    }

    public List<UserDTO> findAllByIds(List<Long> ids) {
        return userRepository.findAllByIds(ids)
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> searchUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable)
                .map(userMapper::toDTO);
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        Integer randomNumber = new SecureRandom().nextInt(900000) + 100000;
        String confirmationCode = String.format("%06d", randomNumber);
        verificationCodeService.saveVerificationCode(req.email(),
                new VerificationData(req.password(),
                        confirmationCode,
                        req.firstName(),
                        req.lastName(),
                        req.phoneNumber()),
                5,
                TimeUnit.MINUTES);

        confirmationCodeSender.sendConfirmationCode(
                new UserRegisteredEvent(
                        req.email(),
                        req.password(),
                        confirmationCode
                ));
    }

    @Transactional
    public void confirmRegistration(ConfirmRequest req) {
        Role role = roleRepository.findByRole(RoleValue.USER)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        VerificationData data = verificationCodeService.getVerificationData(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found" + req.email()));

        boolean isPasswordValid = req.confirmationCode().equals(data.verificationCode());
        boolean isCodeValid = data.verificationCode().equals(req.confirmationCode());

        if (isPasswordValid && isCodeValid) {
            log.info("User is valid registering");

            User user = new User(
                    null,
                    req.email(),
                    passwordEncoder.encode(req.password()),
                    data.firstName(),
                    data.lastName(),
                    role,
                    data.phoneNumber(),
                    LocalDateTime.now(),
                    false,
                    true
            );

            userRepository.save(user);
        }
        verificationCodeService.deleteVerificationCode(req.email());
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
        user.setBanned(isBanned);
        userRepository.save(user);
    }

    @Transactional
    public void editNotifications(UserDetails user, boolean notificationsEnabled) {
        User curUser = userRepository.findByEmail(user.getUsername());
        curUser.setNotificationsEnabled(notificationsEnabled);
        userRepository.save(curUser);
    }

    public UserNotificationFlag getUserNotificationFlag(UserDetails user) {
        User curUser = userRepository.findByEmail(user.getUsername());
        return new UserNotificationFlag(curUser.isNotificationsEnabled());
    }
}
