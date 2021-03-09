package de.besven.kiosk.view;

import de.besven.kiosk.model.User;
import de.besven.kiosk.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles the login-jobs.
 * If the login is successful, the user will be
 * redirected to the shop and the 'ShopController'.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Controller
@RequestMapping(path = "/login")
public class LoginController {

    /**
     * The required Services for the entities.
     */
    private UserService userService;

    /**
     * The constructor.
     *
     * @param userService
     */
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * New users, which created by admins, has no pins to login. This method
     * check if an pin exists and select the modal to create a new pin or login.
     *
     * @return the correct template with modal
     */
    @GetMapping("/")
    String loginForm(@RequestParam("uuid") String uuid, Model model) {
        User currentUser = userService.getUser(uuid);
        model.addAttribute("user", currentUser);

        if (currentUser.getPin() == null) {
            return "newpinform";
        } else {
            return "loginform";
        }
    }

    /**
     * TODO: ADD COMMENT
     */
    @PostMapping("/update")
    String update(@RequestParam(value = "uuid") String uuid,
                  @RequestParam(value = "pin") String newPin,
                  @RequestParam(value = "pinWdh") String pinWdh,
                  Model model) {

        User updateUser = userService.getUser(uuid);
        if (newPin.equals("")) {


            return "redirect:/login/?uuid=" + updateUser.getUuid();
        } else {

            if (newPin.equals(pinWdh)) {
                updateUser.setPin(newPin);
                updateUser.setActive(true);
                userService.createUser(updateUser);
            }
            return "redirect:/login/?uuid=" + updateUser.getUuid();
        }
    }

    /**
     * TODO: add comment
     *
     * @return
     */
    @PostMapping("/loginGo")
    String loginOk(@RequestParam("uuid") String uuid,
                   @RequestParam("pin") String pin) {

        User currentUser = userService.getUser(uuid);

        if (currentUser.getPin().equals(pin)) {
            return "redirect:../kiosk/?userUuid=" + currentUser.getUuid();
        } else {
            return "redirect:/login/?uuid=" + currentUser.getUuid();
        }
    }
}

