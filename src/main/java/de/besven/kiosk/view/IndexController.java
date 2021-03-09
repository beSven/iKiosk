package de.besven.kiosk.view;

import de.besven.kiosk.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles the landingpage and loads all users from database.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Controller
public class IndexController {

    /**
     * The required Services for the entities.
     */
    private UserService userService;

    /**
     * The class-constructor with injected services we need.
     *
     * @param userService handles User-requests
     */
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method delivers content for the landing page.
     *
     * @param model The attributes and its values for the index.html
     * @return The name of the html-file
     */
    @GetMapping("/")
    private String home(Model model) {
        model.addAttribute("users", userService.retrieveUsers());
        return "index";
    }

}
