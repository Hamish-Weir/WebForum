package forum.dao.services;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import forum.dao.repositories.UserRepository;
import forum.entities.User;
import forum.exceptions.DuplicateResourceException;
import forum.exceptions.ValidationException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User newUser(String username, String email, String passwordHash) {
        
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username must not be blank");
        }
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Invalid email address");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new ValidationException("Password must not be blank");
        }

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateResourceException("Username already taken: " + username);
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email already registered: " + email);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Race condition guard: two requests passed the pre-check simultaneously
            throw new DuplicateResourceException("User could not be created due to a conflict");
        }
    }
}
