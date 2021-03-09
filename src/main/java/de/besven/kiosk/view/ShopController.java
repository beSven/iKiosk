package de.besven.kiosk.view;

import de.besven.kiosk.model.Product;
import de.besven.kiosk.model.Transaction;
import de.besven.kiosk.model.User;
import de.besven.kiosk.services.ProductService;
import de.besven.kiosk.services.TransactionService;
import de.besven.kiosk.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static java.util.UUID.randomUUID;

/**
 * Handles the login-jobs.
 * If the login is successful, the user will be
 * redirected to the shop and the 'ShopController'.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Controller
@RequestMapping(path = "/kiosk")
public class ShopController {

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
    public ShopController(UserService userService,
                          ProductService productService,
                          TransactionService transactionService) {
        this.userService = userService;
        this.productService = productService;
        this.transactionService = transactionService;
    }

    /**
     * Todo: add comment
     */
    @GetMapping("/")
    String shopOverview(@RequestParam(value = "userUuid", required = true) String userUuid,
                        @RequestParam(value = "productUuid", required = false) String productUuid,
                        Model model) {
        User currentUser = userService.getUser(userUuid);
        Product selectedProduct = productService.getProduct(productUuid);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("userCredit", transactionService.getCredit(currentUser));
        model.addAttribute("user", currentUser);
        model.addAttribute("products", productService.retrieveProducts());

        if (selectedProduct == null) {

            return "shop";
        } else {
            model.addAttribute("product", selectedProduct);
            return "shop";
        }
    }

    /**
     * TODO: add comment
     *
     * @param userUuid
     * @param productUuid
     * @param countProduct
     * @param model
     * @return
     */
    @PostMapping("/done")
    String shopDone(@RequestParam(value = "userUuid", required = true) String userUuid,
                    @RequestParam(value = "productUuid", required = true) String productUuid,
                    @RequestParam(value = "countProduct", required = true) Integer countProduct,
                    Model model) {

        User currentUser = userService.getUser(userUuid);
        Product selectedProduct = productService.getProduct(productUuid);

        for (int i = 0; i < countProduct; i++) {
            Transaction transaction = new Transaction(
                    randomUUID().toString(),
                    new Date(),
                    selectedProduct.getPrice().negate(),
                    currentUser);
            transaction.setProduct(selectedProduct);
            transaction.setDescription("Kauf " + selectedProduct.getProductName());
            transactionService.createTransaction(transaction);
        }

        return "redirect:../kiosk/?userUuid=" + currentUser.getUuid();
    }

}

