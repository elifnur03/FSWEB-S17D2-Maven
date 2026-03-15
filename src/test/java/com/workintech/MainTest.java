package com.workintech;

import com.workintech.model.*;
import com.workintech.rest.DeveloperController;
import com.workintech.tax.DeveloperTax;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeveloperController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    private DeveloperController controller;

    private final DeveloperTax developerTax = new DeveloperTax();

    @MockBean
    private DeveloperTax mockDeveloperTaxForController;

    @BeforeEach
    void setup() throws Exception {
        controller = new DeveloperController(new DeveloperTax());
        controller.init(); // @PostConstruct simülasyonu
        Developer dev = new Developer(1, "Initial Developer", 5000.0, Experience.JUNIOR);
        mockMvc.perform(post("/workintech/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dev)))
                .andExpect(status().isCreated());
    }

    /*------------------- Developer Class Tests -------------------*/
    @Test
    @DisplayName("Test Developer Creation")
    void testDeveloperCreation() {
        Developer dev = new Developer(1, "John Doe", 1000.0, Experience.JUNIOR);

        assertEquals(1, dev.getId());
        assertEquals("John Doe", dev.getName());
        assertEquals(1000.0, dev.getSalary());
        assertEquals(Experience.JUNIOR, dev.getExperience());
    }

    @Test
    @DisplayName("Test Experience Enum Values")
    void testEnumValuesExist() {
        assertTrue(Arrays.asList(Experience.values()).contains(Experience.JUNIOR));
        assertTrue(Arrays.asList(Experience.values()).contains(Experience.MID));
        assertTrue(Arrays.asList(Experience.values()).contains(Experience.SENIOR));
    }

    @Test
    @DisplayName("Test JuniorDeveloper Inheritance")
    void testJuniorDeveloperInheritance() {
        JuniorDeveloper jd = new JuniorDeveloper(1, "Test", 5000.0);
        assertTrue(jd instanceof Developer);
        assertEquals(Experience.JUNIOR, jd.getExperience());
    }

    @Test
    @DisplayName("Test MidDeveloper Inheritance")
    void testMidDeveloperInheritance() {
        MidDeveloper md = new MidDeveloper(1, "Test", 6000.0);
        assertTrue(md instanceof Developer);
        assertEquals(Experience.MID, md.getExperience());
    }

    @Test
    @DisplayName("Test SeniorDeveloper Inheritance")
    void testSeniorDeveloperInheritance() {
        SeniorDeveloper sd = new SeniorDeveloper(1, "Test", 8000.0);
        assertTrue(sd instanceof Developer);
        assertEquals(Experience.SENIOR, sd.getExperience());
    }

    /*------------------- DeveloperTax Tests -------------------*/
    @Test
    @DisplayName("Test Simple Tax Rate")
    void testSimpleTaxRate() {
        assertEquals(0.15, developerTax.getSimpleTaxRate());
    }

    @Test
    @DisplayName("Test Middle Tax Rate")
    void testMiddleTaxRate() {
        assertEquals(0.25, developerTax.getMiddleTaxRate());
    }

    @Test
    @DisplayName("Test Upper Tax Rate")
    void testUpperTaxRate() {
        assertEquals(0.35, developerTax.getUpperTaxRate());
    }

    /*------------------- DeveloperController Tests -------------------*/
    @Test
    @DisplayName("Developers map initialized")
    @Order(1)
    void testDevelopersMapInitialized() {
        assertNotNull(controller.developers);
    }

    @Test
    @DisplayName("Add Developer")
    @Order(2)
    void testAddDeveloper() throws Exception {
        Developer dev = new Developer(2, "New Developer", 6000.0, Experience.MID);
        mockMvc.perform(post("/workintech/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dev)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Get All Developers")
    @Order(3)
    void testGetAllDevelopers() throws Exception {
        mockMvc.perform(get("/workintech/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @DisplayName("Get Developer By ID")
    @Order(4)
    void testGetDeveloperById() throws Exception {
        mockMvc.perform(get("/workintech/developers/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Developer")
    @Order(5)
    void testUpdateDeveloper() throws Exception {
        Developer updatedDev = new Developer(1, "Updated Developer", 7000.0, Experience.SENIOR);
        mockMvc.perform(put("/workintech/developers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDev)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Developer")
    @Order(6)
    void testDeleteDeveloper() throws Exception {
        mockMvc.perform(delete("/workintech/developers/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Check application.properties values")
    void testApplicationProperties() {
        assertThat(env.getProperty("server.port")).isEqualTo("8585");
        assertThat(env.getProperty("server.servlet.context-path")).isEqualTo("/workintech");
        assertThat(env.getProperty("management.endpoints.web.exposure.include")).isEqualTo("health,info,mappings");
        assertThat(env.getProperty("info.app.name")).isEqualTo("FSWEB-S17D2-Maven");
        assertThat(env.getProperty("info.app.description")).isEqualTo("Spring Boot Dependency Injection projesi");
        assertThat(env.getProperty("info.app.version")).isEqualTo("1.0.0");
        assertThat(env.getProperty("management.info.env.enabled")).isEqualTo("true");
    }
}