package de.besven.kiosk.repository;

import de.besven.kiosk.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductRepository  for database-handling
 * This is the special used, extended JpaRepository for database tasks for the object 'Product'.
 * Extra added methods will save the used parameters.
 * The JpaRepository refers the object 'Product' and his identifier is type 'Long'.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Get the product by their UUID.
     *
     * @param uuid the product's identifier as UUID
     * @return response from database
     */
    Product getProductByUuid(String uuid);

}
