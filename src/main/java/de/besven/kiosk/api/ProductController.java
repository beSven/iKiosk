package de.besven.kiosk.api;

import de.besven.kiosk.model.Product;
import de.besven.kiosk.services.ProductService;
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
 * This controller is the RESTful-interface to handle http-requests
 * product concerned. The standart-path will be '/products' for all
 * products and '/products/{uuid}' for a specific product.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    /**
     * Global instance to get the 'ProductService'
     */
    private final ProductService productService;

    /**
     * The constructor.
     *
     * @param productService the required manager for 'products'-tasks.
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Save a new product in database.
     *
     * @param product the object 'Product' who will create
     * @return the entire http-response inclusive status 201
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    /**
     * Put all products from database in an ArrayList.
     *
     * @return the ArrayList with all products.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return new ArrayList<>(productService.retrieveProducts());
    }

    /**
     * Find a product by his unique UUID.
     *
     * @param uuid variable to complete the URL to the specific product.
     * @return the entire http-reponse inclusive status 200 or 404
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProduct(@PathVariable String uuid) {
        if (productService.getProduct(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Product actualProduct = productService.getProduct(uuid);
            return new ResponseEntity<>(actualProduct, HttpStatus.OK);
        }
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable String uuid, @Valid @RequestBody Product product) {
        if (productService.getProduct(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
        }
    }
}
