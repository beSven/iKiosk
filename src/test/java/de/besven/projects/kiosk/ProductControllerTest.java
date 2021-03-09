package de.besven.projects.kiosk;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.besven.kiosk.api.ProductController;
import de.besven.kiosk.model.Product;
import de.besven.kiosk.services.ProductService;
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

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is used for testing the http-requests to the
 * ProductController
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService mockProductService;


    /**
     * This test should get an http-Status: created (201)
     * if an new product is created.
     */
    @Test
    public void isPostStatusOk() throws Exception {
        String testProduct1Uuid = randomUUID().toString();
        Product testProduct1 = new Product("SNICKERS", testProduct1Uuid);
        String testProduct2Uuid = randomUUID().toString();
        Product testProduct2 = new Product("Coke", testProduct2Uuid);

        when(mockProductService.createProduct(testProduct1)).thenReturn(testProduct1);
        when(mockProductService.getProduct(testProduct1.getUuid())).thenReturn(testProduct1);

        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(mapper.writeValueAsString(testProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty product.
     */
    @Test
    public void postAnEmptyProductIsNotOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty productName-field for a new product.
     */
    @Test
    public void postWithEmptyProductNameIsNotOk() throws Exception {
        String testProduct1Uuid = randomUUID().toString();
        String emptyString = "";
        Product testProduct1 = new Product(emptyString, testProduct1Uuid);

        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(mapper.writeValueAsString(testProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty UUID-field for a new product.
     */
    @Test
    public void postWithEmptyUuidIsNotOk() throws Exception {
        String emptyString = "";
        Product testProduct1 = new Product("SNICKERS", emptyString);
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(mapper.writeValueAsString(testProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-status ok (200) for a get-request
     */
    @Test
    public void isGetStatusOk() throws Exception {

        this.mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * This test should get an http-status ok (200) and found a
     * product per UUID.
     */
    @Test
    public void shouldFoundAProductPerUuid() throws Exception {
        String testProduct1Uuid = randomUUID().toString();
        Product testProduct1 = new Product(testProduct1Uuid, "SNICKERS");

        when(mockProductService.getProduct(testProduct1Uuid)).thenReturn(testProduct1);

        MvcResult result = this.mockMvc.perform(get("/products/{uuid}", testProduct1.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", Matchers.is("SNICKERS")))
                .andExpect(jsonPath("$.uuid", Matchers.is(testProduct1Uuid)))
                .andReturn();
    }

    /**
     * This test should get an 'Not Found' (404) if a UUID
     * not exits.
     */
    @Test
    public void shouldGetA404ifUuidDoesNotExits() throws Exception {
        String testProduct1Uuid = randomUUID().toString();
        String wrongUuid = "12345678910";
        Product testProduct = new Product(testProduct1Uuid, "SNICKERS");

        when(mockProductService.getProduct(testProduct1Uuid)).thenReturn(testProduct);

        MvcResult result = this.mockMvc.perform(get("/products/{uuid}", wrongUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * This test should update a product.
     */
    @Test
    public void shouldUpdateAProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String testProductUuid = randomUUID().toString();
        final Product testProduct = new Product(testProductUuid, "RAIDER");
        testProduct.setPrice(new BigDecimal(0.69));
        testProduct.setTotal(500);

        String updateProductUuid = randomUUID().toString();
        final Product updateProduct = new Product(updateProductUuid, "TWIX");
        updateProduct.setPrice(new BigDecimal(0.99));
        updateProduct.setTotal(200);
        when(mockProductService.getProduct(testProduct.getUuid())).thenReturn(testProduct);
        when(mockProductService.createProduct(updateProduct)).thenReturn(updateProduct);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/products/{uuid}", testProduct.getUuid())
                .content((mapper.writeValueAsString(updateProduct)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("TWIX"))
                .andExpect(jsonPath("$.price").value(0.99))
                .andExpect(jsonPath("$.total").value(200));
    }

    /**
     * This test should get a BadRequest for update with
     * empty new productName.
     */
    @Test
    public void emptyNewProductNameIsNotAllowed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String testProductUuid = randomUUID().toString();
        final Product testProduct = new Product(testProductUuid, "RAIDER");
        testProduct.setPrice(new BigDecimal(0.69));
        testProduct.setTotal(500);

        String updateProductUuid = randomUUID().toString();
        final Product updateProduct = new Product(updateProductUuid, "");
        updateProduct.setPrice(new BigDecimal(0.99));
        updateProduct.setTotal(200);

        when(mockProductService.getProduct(testProduct.getUuid())).thenReturn(testProduct);
        when(mockProductService.createProduct(updateProduct)).thenReturn(updateProduct);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/products/{uuid}", testProduct.getUuid())
                .content((mapper.writeValueAsString(updateProduct)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

