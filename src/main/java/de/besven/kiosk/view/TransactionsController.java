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

/**
 * TODO: Add comments!!!
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Controller
@RequestMapping("/transactions")
public class TransactionsController {

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
    public TransactionsController(UserService userService,
                                  ProductService productService,
                                  TransactionService transactionService) {
        this.userService = userService;
        this.productService = productService;
        this.transactionService = transactionService;
    }

    /**
     * TODO: verschieben in TransactionsController
     */
    @GetMapping("/")
    public String transactions(@RequestParam("userUuid") String userUuid, Model model) {
        User currentUser = userService.getUser(userUuid);
        model.addAttribute("user", currentUser);
        model.addAttribute("transactions", transactionService.getTransactions(currentUser));
        model.addAttribute("userCredit", transactionService.getCredit(currentUser));
        return "transactions";
    }
}
