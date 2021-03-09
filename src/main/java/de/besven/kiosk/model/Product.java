package de.besven.kiosk.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Blob;

/**
 * The products who will be retail.
 * This object-members are the same like the database-table 'products'.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Product {

    /**
     * The PrimaryKey and unique identifier for the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCTS_GEN")
    @SequenceGenerator(allocationSize = 1, name = "SEQ_PRODUCTS_GEN", sequenceName = "SEQ_PRODUCTS")
    private Long id;

    /**
     * The unique identifier.
     */
    @NotBlank
    @Size(max = 250)
    private String uuid;

    /**
     * The product's name.
     */
    @NotBlank
    @Size(max = 250)
    private String productName;

    /**
     * A short description of the article (maker, kind etc.)
     */
    private String description;

    /**
     * The retailers-price.
     */
    private BigDecimal cost;

    /**
     * The price of sale.
     */
    private BigDecimal price;

    /**
     * A reference value for the products count. This should be the
     * same like the sum from the transaction-list.
     */
    private double total;

    /**
     * Charset from database for the picture.
     */
    private Blob picture;

    /**
     * Constructor with required parameter.
     *
     * @param uuid        the products identifier.
     * @param productName the products name.
     */
    public Product(@NotBlank @Size(max = 250) String uuid, @NotBlank @Size(max = 250) String productName) {
        this.uuid = uuid;
        this.productName = productName;
    }
}
