package de.besven.projects.kiosk;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.besven.kiosk.api.TransactionController;
import de.besven.kiosk.model.Product;
import de.besven.kiosk.model.Transaction;
import de.besven.kiosk.model.User;
import de.besven.kiosk.services.TransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Date;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is used for testing the http-request with
 * the TransactionsController.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = TransactionController.class)
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService mockTransactionService;

    /**
     * Global variables to use in this class.
     */
    private String testTransactionUuid;
    private String emptyField;
    private User testUser, updateUser;
    private Product testProduct, updateProduct;
    private Transaction testTransaction;
    private ObjectMapper mapper;
    private Date now = new Date();

    /**
     * These are testdata for each testmethode.
     */
    public void getTestdata() {
        mapper = new ObjectMapper();
        testTransactionUuid = randomUUID().toString();
        String testProductUuid = randomUUID().toString();
        String testUserUuid = randomUUID().toString();
        String updateUserUuid = randomUUID().toString();
        String updateProductUuid = randomUUID().toString();
        emptyField = "";

        testUser = new User(
                testUserUuid,
                "Max",
                "Muster",
                "mmr");
        updateUser = new User(
                updateUserUuid,
                "John",
                "Lennon",
                "jln");

        testProduct = new Product(testProductUuid, "SNICKERS");
        testProduct.setPrice(new BigDecimal("0.90"));
        updateProduct = new Product(updateProductUuid, "Club Mate");
        updateProduct.setPrice(new BigDecimal("1.50"));

        testTransaction = new Transaction(
                testTransactionUuid,
                now,
                new BigDecimal("0.90"),
                testUser);
        testTransaction.setProduct(testProduct);
        when(mockTransactionService.createTransaction(testTransaction)).thenReturn(testTransaction);
        when(mockTransactionService.getTransaction(testTransaction.getUuid())).thenReturn(testTransaction);
    }

    /**
     * These test should get an HTTP-status created (201) for create
     * a new transaction.
     *
     * @throws Exception
     */
    @Test
    public void postStatusShouldBeOkay() throws Exception {
        getTestdata();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .content(mapper.writeValueAsString(testTransaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * These test should get an HTTP-status BadRequest(400) for create
     * a new transaction without UUID.
     *
     * @throws Exception
     */
    @Test
    public void postAnEmptyUuidShouldBeBadRequest() throws Exception {
        getTestdata();

        testTransaction = new Transaction(
                emptyField,
                now,
                new BigDecimal("0.90"),
                testUser);

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .content(mapper.writeValueAsString(testTransaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * These test should get an HTTP-status BadRequest(400) for create
     * a new transaction without timestamp.
     *
     * @throws Exception
     */
    @Test
    public void postAnEmptyTimestampShouldBeBadRequest() throws Exception {
        getTestdata();

        testTransaction = new Transaction(
                testTransactionUuid,
                null,
                new BigDecimal("0.90"),
                testUser);

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .content(mapper.writeValueAsString(testTransaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * These test should get an HTTP-status BadRequest(400) for create
     * a new transaction without user.
     *
     * @throws Exception
     */
    @Test
    public void postAnEmptyUserShouldBeBadRequest() throws Exception {
        getTestdata();

        Transaction testTransaction1 = new Transaction(
                testTransactionUuid,
                now,
                new BigDecimal("0.90"),
                null);

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .content(mapper.writeValueAsString(testTransaction1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-status ok (200) for a get-request
     */
    @Test
    public void getTransactionStatusShouldBeOk() throws Exception {

        this.mockMvc.perform(get("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * This test should get an http-status ok (200) and found a
     * transaction per UUID.
     */
    @Test
    public void getTransactionPerUuidStatusShouldBeOk() throws Exception {
        getTestdata();

        MvcResult result = this.mockMvc.perform(get("/transactions/{uuid}", testTransaction.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", Matchers.is(testTransactionUuid)))
                .andReturn();
    }

    /**
     * This test should get an 'Not Found' (404) if a UUID
     * not exits.
     */
    @Test
    public void getTransactionPerUuidStatusShouldBeNotFound() throws Exception {
        getTestdata();
        String wrongUuid = "12345678910";

        MvcResult result = this.mockMvc.perform(get("/transactions/{uuid}", wrongUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * This test should get an http-status OK (200) if update a transaction.
     */
    @Test
    public void updateTransactionStatusShouldBeOk() throws Exception {
        getTestdata();

        Transaction updateTransaction = new Transaction(
                testTransaction.getUuid(),
                now,
                new BigDecimal("0.90"),
                updateUser);

        when(mockTransactionService.createTransaction(testTransaction)).thenReturn(testTransaction);
        when(mockTransactionService.getTransaction(updateTransaction.getUuid())).thenReturn(updateTransaction);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/transactions/{uuid}", testTransaction.getUuid())
                .content((mapper.writeValueAsString(updateTransaction)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-status BadRequest(400) if an transaction
     * to update has no timestamp.
     */
    @Test
    public void updateTransactionStatusWithDeleteTimestampShouldBeBadRequest() throws Exception {
        getTestdata();

        Transaction updateTransaction = new Transaction(
                testTransaction.getUuid(),
                null,
                new BigDecimal("0.90"),
                updateUser);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/transactions/{uuid}", testTransaction.getUuid())
                .content((mapper.writeValueAsString(updateTransaction)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get an http-status BadRequest(400) if update an transaction
     * delete a customer.
     */
    @Test
    public void updateTransactionStatusWithDeleteUserShouldBeBadRequest() throws Exception {
        getTestdata();

        Transaction updateTransaction = new Transaction(
                testTransaction.getUuid(),
                now,
                new BigDecimal("0.90"),
                null);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/transactions/{uuid}", testTransaction.getUuid())
                .content((mapper.writeValueAsString(updateTransaction)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get an http-status ok (200) if a transaction
     * was deleted.
     */
    @Test
    public void deleteATransactionWasSuccessful() throws Exception {
        getTestdata();

        MvcResult result = this.mockMvc.perform(delete("/transactions/{uuid}", testTransaction.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "");
    }

    /**
     * This test should get an http-status NotFount (404) if a transaction'
     * doesn't exists.
     */
    @Test
    public void deleteTransactionNotFoundShouldBeStatusNotFound() throws Exception {
        getTestdata();
        String wrongUuid = "123456";
        MvcResult result = this.mockMvc.perform(delete("/transactions/{uuid}", wrongUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
