package de.besven.kiosk.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The transaction-class describe the relationship between a
 * user and an product or renewed his/her credit) and add a sum
 * and a timestamp.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    /**
     * Unique identifier and database PrimaryKey for each transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANSACTIONS_GEN")
    @SequenceGenerator(allocationSize = 1, name = "SEQ_TRANSACTIONS_GEN", sequenceName = "SEQ_TRANSACTIONS")
    private Long id;

    /**
     * The unique identifier.
     */
    @NotBlank
    @Size(max = 250)
    private String uuid;

    /**
     * The transaction's timestamp.
     */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;

    /**
     * The transaction's type.
     */
    @Size(max = 250)
    private String description;

    /**
     * The transaction's cash-value.
     */
    @NotNull
    private BigDecimal amount;

    /**
     * If an transaction is cancelled, then is it inactive {@code false}.
     */
    @NotNull
    private Boolean active;

    /**
     * Which user is involved in this transaction.
     */
    @ManyToOne
    @NotNull
    private User user;

    /**
     * Which product is bought.
     */
    @ManyToOne
    private Product product;

    /**
     * Constructor with required parameter.
     *
     * @param uuid      the transactions identifier
     * @param timestamp the transactions timestamp
     * @param amount    the cash-value
     * @param user      the user who is involved
     */
    public Transaction(@NotBlank @Size(max = 250) String uuid,
                       @NotNull Date timestamp,
                       @NotNull BigDecimal amount,
                       @NotNull User user) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.amount = amount;
        this.active = true;
        this.user = user;
    }
}
