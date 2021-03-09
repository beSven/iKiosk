package de.besven.kiosk.services;

import de.besven.kiosk.model.Product;
import de.besven.kiosk.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * This service manages the products specific operations.
 * Only basic options like create, edit, delete should be
 * processed in this service.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Service
public class ProductService {

    /**
     * Global instance of the productRepository.
     */
    private final ProductRepository productRepository;

    /**
     * Class constructor, work with the database.
     *
     * @param productRepository the JpaRepository for object 'Product'
     */
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Create an new product.
     *
     * @param product the object of a new product.
     * @return the saved product.
     */
    public Product createProduct(final Product product) {
        return productRepository.save(product);
    }

    /**
     * Retrieves all products, sort by their name.
     *
     * @return the sorted list with all availible products.
     */
    public List<Product> retrieveProducts() {
        List<Product> allProducts = productRepository.findAll();
        allProducts.sort(Comparator.comparing(Product::getProductName));
        return allProducts;
    }

    /**
     * Retrieves an product, found by their UUID.
     *
     * @param uuid the products identifier
     * @return the founded product or 'null' if there is not
     */
    public Product getProduct(final String uuid) {
        return productRepository.getProductByUuid(uuid);
    }
}
