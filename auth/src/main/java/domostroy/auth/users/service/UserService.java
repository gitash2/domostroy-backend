package domostroy.auth.users.service;

import domostroy.auth.config.rabbit.RabbitMQConfigConstants;
import domostroy.auth.dto.ConfirmRequest;
import domostroy.auth.dto.SignUpRequest;
import domostroy.auth.dto.VerificationData;
import domostroy.auth.exceptions.RoleNotFoundException;
import domostroy.auth.exceptions.UserAlreadyExistsException;
import domostroy.auth.users.model.Role;
import domostroy.auth.users.model.RoleValue;
import domostroy.auth.users.model.User;
import domostroy.auth.users.repository.RoleRepository;
import domostroy.auth.users.repository.UserRepository;
import domostroy.events.mail.UserRegisteredEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
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

        rabbitTemplate.convertAndSend(
                RabbitMQConfigConstants.Queue.QUEUE_SEND_CONFIRMATION_EMAIL,
                new UserRegisteredEvent(
                        req.email(),
                        req.password(),
                        confirmationCode));
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
                    req.email(),
                    passwordEncoder.encode(req.password()),
                    data.firstName(),
                    data.lastName(),
                    role,
                    data.phoneNumber()
            );

            userRepository.save(user);
        }
        verificationCodeService.deleteVerificationCode(req.email());
    }
}
