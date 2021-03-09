package de.besven.kiosk.api;

import de.besven.kiosk.model.Transaction;
import de.besven.kiosk.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * This controller is the RESTful interface to handle http-requests
 * transaction's concerned. The standard-path will be '/transactions' for all
 * transactions.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    /**
     * The service who is needed as global instance.
     */
    private final TransactionService transactionService;

    /**
     * The constructor.
     *
     * @param transactionService the required manager for transactions.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Save a new transaction in database.
     *
     * @param transaction the transaction as a transaction.
     * @return the http-response inclusive status 201.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }

    /**
     * Put all transactions who found in an ArrayList.
     *
     * @return the sorted ArrayList.
     */
    @GetMapping
    public List<Transaction> retrieveTransactions() {
        return new ArrayList<>(transactionService.retrieveTransactions());
    }

    /**
     * Find a transaction by their unique UUID.
     *
     * @param uuid the transaction's identifier.
     * @return the http-response with status 200 or 404 if UUID was not found
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<Transaction> getTransactions(@PathVariable String uuid) {
        if (transactionService.getTransaction(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Transaction actualTransaction = transactionService.getTransaction(uuid);
            return new ResponseEntity<>(actualTransaction, HttpStatus.OK);
        }
    }

    /**
     * Update a transaction, found by their UUID.
     *
     * @param uuid the transaction's unique identifier.
     * @param transaction the transaction who get an update.
     * @return the http-response with status 201 or 404 if UUID was not found
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<Transaction> updateTransactions(@PathVariable String uuid, @Valid @RequestBody Transaction transaction) {
        if (transactionService.getTransaction(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
        }
    }

    /**
     * A transaction, found by his unique UUID, will be delete.
     *
     * @param uuid the transaction's identifier.
     * @return the entire http-response, inclusive status 2xx or 404 if uuid is not found.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Transaction> deleteTransactions(@PathVariable String uuid) {
        if (transactionService.getTransaction(uuid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Transaction deletedTransaction = transactionService.deleteTransaction(uuid);
            return new ResponseEntity<>(deletedTransaction, HttpStatus.OK);
        }
    }
}