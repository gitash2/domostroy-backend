package domostroy.core.application.users;

import domostroy.core.adapters.adaptersInput.dto.input.users.ChangePasswordDTO;
import domostroy.core.adapters.adaptersInput.dto.input.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDTO getUserData(Long userId) {
        User user = userRepository.findById(userId);
        return new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
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
}
