package de.besven.kiosk.services;

import de.besven.kiosk.model.Transaction;
import de.besven.kiosk.model.User;
import de.besven.kiosk.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This service manages the transactions specific operations.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Service
public class TransactionService {

    /**
     * The repository we needed as global instance
     */
    private final TransactionRepository transactionRepository;

    /**
     * Class constructor to work with the database.
     *
     * @param transactionRepository the repository for object Transaction
     */
    public TransactionService(final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction the object of a new transaction
     * @return the saved transaction
     */
    public Transaction createTransaction(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves a transaction, found by their uuid.
     *
     * @param uuid the transactions identifier
     * @return the founded transaction or 'null' if there is not
     */
    public Transaction getTransaction(final String uuid) {
        return transactionRepository.getTransactionByUuid(uuid);
    }

    /**
     * Retrieves all transactions, sort by their timestamp.
     *
     * @return the sorted list of transactions
     */
    public List<Transaction> retrieveTransactions() {
        List<Transaction> allTransactions = new ArrayList<>(transactionRepository.findAll());
        allTransactions.sort(Comparator.comparing(Transaction::getTimestamp));
        return allTransactions;
    }

    /**
     * TODO: Überarbeiten -> Transaktionen stornieren/inaktiv setzen
     *
     * @param uuid
     * @return
     */
    public Transaction deleteTransaction(final String uuid) {
        return transactionRepository.deleteTransactionByUuid(uuid);
    }

    /**
     * Get all transactions of a user.
     *
     * @param user the user
     * @return the transactions
     */
    public List<Transaction> getTransactions(final User user) {
        List<Transaction> userTransaction = new ArrayList<>(transactionRepository.getTransactionByUser(user));
        userTransaction.sort(Comparator.comparing(Transaction::getTimestamp));
        return userTransaction;
    }

    /**
     * Calculates the credit of an user.
     *
     * @param user the user
     * @return the actual credit
     */
    public BigDecimal getCredit(final User user) {
        return getTransactions(user).stream()
                .map(credit -> credit.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the summary of money in the bank.
     *
     * @return The total money in bank
     */
    public BigDecimal getBankSummary() {
        BigDecimal bank = new BigDecimal(0);
        ArrayList<Transaction> allIntos = new ArrayList<>(retrieveTransactions());

        for (Transaction transaction : allIntos) {
            if (transaction.getProduct() == null) {
                bank = bank.add(transaction.getAmount());
            }
        }
        return bank;
    }


}
