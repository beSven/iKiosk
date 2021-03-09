package de.besven.kiosk.services;

import de.besven.kiosk.model.User;
import de.besven.kiosk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * This service manages the user specific operations.
 * Only basic options like create, edit, delete should be
 * processed in this service.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Service
public class UserService {

    /**
     * Global instance of the userRepository.
     */
    private final UserRepository userRepository;

    /**
     * Class Constructor, use parameter UserRepository
     * and get the global userRepository to work with database.
     *
     * @param userRepository for user's extended JpaRepository
     */
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all available users without conditions.
     * The result will be sort by the first name.
     *
     * @return the sorted list with all available users
     */
    public List<User> retrieveUsers() {
        List<User> allUsers = userRepository.findAll();
        allUsers.sort(Comparator.comparing(User::getFirstName));
        return allUsers;
    }

    /**
     * Retrieves an user by their UUID.
     *
     * @param uuid the user's UUID
     * @return the founded user or {@code null} if the user was not found
     */
    public User getUser(final String uuid) {
        return userRepository.getUserByUuid(uuid);
    }

    /**
     * Creates an user.
     *
     * @param user the user object with filled data required for a new creation
     * @return the saved user or {@code null} if any exception during the user creation is thrown
     */
    public User createUser(final User user) {
        return userRepository.save(user);
    }
}
