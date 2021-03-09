package de.besven.kiosk.view;

import de.besven.kiosk.model.User;
import de.besven.kiosk.services.ProductService;
import de.besven.kiosk.services.TransactionService;
import de.besven.kiosk.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * TODO: Add comments!!!
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * The required Services for the entities.
     */
    private UserService userService;
    private ProductService productService;
    private TransactionService transactionService;

    /**
     * The constructor.
     *
     * @param userService
     * @param productService
     * @param transactionService
     */
    public AdminController(UserService userService,
                           ProductService productService,
                           TransactionService transactionService) {
        this.userService = userService;
        this.productService = productService;
        this.transactionService = transactionService;
    }

    /**
     * Delivers the content for the administrators landing-page.
     *
     * @param userUuid The users identifier
     * @param model    The model with attributes to fill the html-file
     * @return The html-template for admins or startpage
     */
    @GetMapping
    public String adminWelcome(@RequestParam("uuid") String userUuid,
                               Model model) {

        User currentUser = userService.getUser(userUuid);
        Boolean admin = currentUser.getAdmin();
        model.addAttribute("user", currentUser);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("bank", transactionService.getBankSummary());
        model.addAttribute("transactions", transactionService.retrieveTransactions());

        if (admin == true) {
            return "adminstart";
        } else {
            return "redirect:/";
        }
    }


}
