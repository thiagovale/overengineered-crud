package com.example.backend;

import com.example.backend.config.TestConfig;
import com.example.backend.dto.request.AddressRequestDTO;
import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.request.PhoneNumberRequestDTO;
import com.example.backend.entity.Address;
import com.example.backend.entity.Client;
import com.example.backend.entity.PhoneNumber;
import com.example.backend.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get all clients")
    @WithMockUser(username = "testuser")
    void shouldGetAllClients() throws Exception {
        // Given
        Client client = createTestClient();
        clientRepository.save(client);

        // When/Then
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[0].addresses", hasSize(1)))
                .andExpect(jsonPath("$.data[0].phoneNumbers", hasSize(1)));
    }

    @Test
    @DisplayName("Should get client by id")
    @WithMockUser(username = "testuser")
    void shouldGetClientById() throws Exception {
        // Given
        Client client = createTestClient();
        Client saved = clientRepository.save(client);

        // When/Then
        mockMvc.perform(get("/client/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.addresses[0].street").value("123 Main St"))
                .andExpect(jsonPath("$.data.phoneNumbers[0].number").value("123456789"));
    }

    @Test
    @DisplayName("Should return 404 when client not found")
    @WithMockUser(username = "testuser")
    void shouldReturn404WhenClientNotFound() throws Exception {
        mockMvc.perform(get("/client/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create client successfully")
    @WithMockUser(username = "testuser")
    void shouldCreateClient() throws Exception {
        // Given
        ClientRequestDTO dto = new ClientRequestDTO(
                "Jane",
                "Smith",
                LocalDate.of(1995, 5, 15),
                "jane@example.com",
                "98765432100",
                List.of(new AddressRequestDTO(null, "456 Oak St", "NewCity", "NC", "54321")),
                List.of(new PhoneNumberRequestDTO(null, "987654321", "home"))
        );

        // When/Then
        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("Jane"))
                .andExpect(jsonPath("$.data.email").value("jane@example.com"));
    }

    @Test
    @DisplayName("Should update client successfully")
    @WithMockUser(username = "testuser")
    void shouldUpdateClient() throws Exception {
        // Given
        Client client = createTestClient();
        Client saved = clientRepository.save(client);

        ClientRequestDTO updateDto = new ClientRequestDTO(
                "John Updated",
                "Doe Updated",
                LocalDate.of(1990, 1, 1),
                "john.updated@example.com",
                "12345678900",
                List.of(),
                List.of()
        );

        // When/Then
        mockMvc.perform(put("/client/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John Updated"))
                .andExpect(jsonPath("$.data.email").value("john.updated@example.com"));
    }

    @Test
    @DisplayName("Should delete client successfully")
    @WithMockUser(username = "testuser")
    void shouldDeleteClient() throws Exception {
        // Given
        Client client = createTestClient();
        Client saved = clientRepository.save(client);

        // When/Then
        mockMvc.perform(delete("/client/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client deleted successfully"));

        // Verify
        mockMvc.perform(get("/client/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john@example.com");
        client.setDocumentNumber("12345678900");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("ST");
        address.setZipCode("12345");
        address.setClient(client);

        PhoneNumber phone = new PhoneNumber();
        phone.setNumber("123456789");
        phone.setType("mobile");
        phone.setClient(client);

        client.setAddresses(new java.util.HashSet<>(List.of(address)));
        client.setPhoneNumbers(new java.util.HashSet<>(List.of(phone)));

        return client;
    }
}