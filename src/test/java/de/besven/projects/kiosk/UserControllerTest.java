package de.besven.projects.kiosk;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.besven.kiosk.api.UserController;
import de.besven.kiosk.model.User;
import de.besven.kiosk.services.UserService;
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

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is used for testing the http-requests to the
 * UserController
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    /**
     * This test should get an http-Status: created (201)
     * if an new user is created.
     */
    @Test
    public void isPostStatusOk() throws Exception {
        User user1 = new User(
                randomUUID().toString(), "Max", "Mustermann", "mmn");
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(result, mapper.writeValueAsString(user1));
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty user.
     */
    @Test
    public void postAnEmptyUserIsNotOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty nickname-field for a new user.
     */
    @Test
    public void postWithEmptyNicknameIsNotOk() throws Exception {
        String emptyString = "";

        User user1 = new User(randomUUID().toString(), "Max", "Muustermann", emptyString);
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty lastname-field for a new user.
     */
    @Test
    public void postWithEmptyLastNameIsNotOk() throws Exception {
        String emptyString = "";

        User user1 = new User(randomUUID().toString(), "Max", emptyString, "mmn");
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty firstName-field for a new user.
     */
    @Test
    public void postWithEmptyFirstNameIsNotOk() throws Exception {
        String emptyString = "";

        User user1 = new User(randomUUID().toString(), emptyString, "Mustermann", "mmn");
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * This test should get an http-Status: BadRequest (400)
     * due post an empty UUID-field for a new user.
     */
    @Test
    public void postWithEmptyUuidIsNotOk() throws Exception {
        String emptyString = "";

        User user1 = new User(emptyString, "Max", "Mustermann", "mmn");
        ObjectMapper mapper = new ObjectMapper();

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(user1))
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

        this.mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * This test should get an http-status ok (200) and found a
     * user per UUID.
     */
    @Test
    public void shouldFoundAUserPerUuid() throws Exception {
        String testUserUuid = randomUUID().toString();
        User testUser = new User(testUserUuid, "Max", "Mustermann", "mmn");

        when(mockUserService.getUser(testUserUuid)).thenReturn(testUser);

        MvcResult result = (MvcResult) this.mockMvc.perform(get("/users/{uuid}", testUser.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.lastName", is("Mustermann")))
                .andExpect(jsonPath("$.username", is("mmn")))
                .andReturn();
    }

    /**
     * This test should get an 'Not Found' (404) if a UUID
     * not exits.
     */
    @Test
    public void shouldGetA404ifUuidDoesNotExits() throws Exception {
        String testUserUuid = randomUUID().toString();
        String wrongUuid = "12345678910";
        User testUser = new User(testUserUuid, "Max", "Mustermann", "mmn");

        when(mockUserService.getUser(testUserUuid)).thenReturn(testUser);

        MvcResult result = (MvcResult) this.mockMvc.perform(get("/users/{uuid}", wrongUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * This test should update a user.
     */
    @Test
    public void shouldUpdateAUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final User testUser = new User(randomUUID().toString(), "Max", "Muster", "mmr");
        testUser.setAdmin(false);
        testUser.setPin("2222");
        final User testUserNeu = new User(testUser.getUuid().toString(), "Moritz", "Muster", "mm");
        testUserNeu.setPin("1111");
        testUserNeu.setAdmin(true);
        when(mockUserService.getUser(testUser.getUuid())).thenReturn(testUser);
        when(mockUserService.createUser(testUserNeu)).thenReturn(testUserNeu);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/{uuid}", testUser.getUuid())
                .content((mapper.writeValueAsString(testUserNeu)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Moritz"))
                .andExpect(jsonPath("$.lastName").value("Muster"))
                .andExpect(jsonPath("$.pin").value("1111"));
    }

    /**
     * This test should get a BadRequest for update with
     * empty new firstname.
     */
    @Test
    public void emptyNewFirstNameIsNotAllowed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final User testUser = new User(randomUUID().toString(), "Max", "Muster", "mmr");
        testUser.setAdmin(false);
        testUser.setPin("2222");
        final User testUserNeu = new User(testUser.getUuid().toString(), "", "Muster", "mm");
        testUserNeu.setPin("1111");
        testUserNeu.setAdmin(true);
        when(mockUserService.getUser(testUser.getUuid())).thenReturn(testUser);
        when(mockUserService.createUser(testUserNeu)).thenReturn(testUserNeu);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/{uuid}", testUser.getUuid())
                .content((mapper.writeValueAsString(testUserNeu)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get a BadRequest for update with
     * empty new firstname.
     */
    @Test
    public void emptyNewLastNameIsNotAllowed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final User testUser = new User(randomUUID().toString(), "Max", "Muster", "mmr");
        testUser.setAdmin(false);
        testUser.setPin("2222");
        final User testUserNeu = new User(testUser.getUuid().toString(), "Moritz", "", "mm");
        testUserNeu.setPin("1111");
        testUserNeu.setAdmin(true);
        when(mockUserService.getUser(testUser.getUuid())).thenReturn(testUser);
        when(mockUserService.createUser(testUserNeu)).thenReturn(testUserNeu);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/{uuid}", testUser.getUuid())
                .content((mapper.writeValueAsString(testUserNeu)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get a BadRequest for update with
     * empty new firstname.
     */
    @Test
    public void emptyNewUsernameIsNotAllowed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final User testUser = new User(randomUUID().toString(), "Max", "Muster", "mmr");
        testUser.setAdmin(false);
        testUser.setPin("2222");
        final User testUserNeu = new User(testUser.getUuid().toString(), "Moritz", "Muster", "");
        testUserNeu.setPin("1111");
        testUserNeu.setAdmin(true);
        when(mockUserService.getUser(testUser.getUuid())).thenReturn(testUser);
        when(mockUserService.createUser(testUserNeu)).thenReturn(testUserNeu);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/{uuid}", testUser.getUuid())
                .content((mapper.writeValueAsString(testUserNeu)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test should get an 'Accepted'(202) if a user
     * was deleted.
     * @throws Exception
     */
    @Test
    public void shouldDeleteAUser() throws Exception {
        final User testUser = new User(randomUUID().toString(), "Max", "Muster", "mmr");

        when(mockUserService.getUser(testUser.getUuid())).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{uuid}", testUser.getUuid()))
                .andExpect(status().isAccepted());
    }
}
