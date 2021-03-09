package de.besven.kiosk.repository;

import de.besven.kiosk.model.Transaction;
import de.besven.kiosk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * This class is for database-handling
 * This is a special used, extended JpaRepository for database tasks
 * for the Object 'Transaction'.
 * Extra added methods will save the used parameters.
 * The JpaRepository refers the object 'Transaction' and his identifier is type 'Long'.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Get a transaction by their UUID.
     *
     * @param uuid the transaction's identifier as UUID.
     * @return the transaction who's found in database.
     */
    Transaction getTransactionByUuid(String uuid);

    /**
     * Delete a transaction by their UUID.
     *
     * @param uuid the transaction's identifier.
     * @return the transaction who's found in database.
     */
    Transaction deleteTransactionByUuid(String uuid);

    /**
     * Gets all transactions of an user.
     *
     * @param user the user
     * @return a collection of user transactions
     */
    Collection<Transaction> getTransactionByUser(User user);
}
