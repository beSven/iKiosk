package de.besven.kiosk.api;


import de.besven.kiosk.model.User;
import de.besven.kiosk.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller is the RESTful-Interface to handle
 * http-requests user concerned. The standard-path will be '/users'
 * for all users and '/user/{id}' for a specific user.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Global instance to get the 'UserService'
     */
    private final UserService userService;

    /**
     * The constructor required the following parameter:
     *
     * @param userService the required manager for 'user'-tasks
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Save a new user in the database.
     *
     * @param user the object user who will create
     * @return entire http-response inclusive status-code 201
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    /**
     * Put all users from database in an ArrayList.
     *
     * @return an ArrayList with all users
     */
    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(userService.retrieveUsers());
    }

    /**
     * Find a user from database by his UUID.
     *
     * @param uuid variable to complete the URL to specific user
     * @return the entire http-response inclusive status 200
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<User> getAUser(@PathVariable String uuid) {

        if (userService.getUser(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            User actualUser = userService.getUser(uuid);
            return new ResponseEntity<>(actualUser, HttpStatus.OK);
        }
    }

    /**
     * Find a user by his UUID and add new (or old) values to the
     * properties.
     *
     * @param updatedUser the user with new properties
     * @param uuid        variable to complete the URL to specific user
     * @return the entire http-response inclusive status 201
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateAUser(@Valid @RequestBody final User updatedUser, @PathVariable String uuid) {
        return new ResponseEntity<>(userService.createUser(updatedUser), HttpStatus.CREATED);
    }
}
