package com.example.backend;

import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.entity.Address;
import com.example.backend.entity.Client;
import com.example.backend.entity.PhoneNumber;
import com.example.backend.mapper.ClientMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClientMapperTest {

    private final ClientMapper mapper = new ClientMapper();

    @Test
    @DisplayName("Should map Client entity to DTO with all relationships")
    void shouldMapClientToDTO() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john@example.com");
        client.setDocumentNumber("12345678900");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Address address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("ST");
        address.setZipCode("12345");

        PhoneNumber phone = new PhoneNumber();
        phone.setId(1L);
        phone.setNumber("123456789");
        phone.setType("mobile");

        client.setAddresses(new java.util.HashSet<>(List.of(address)));
        client.setPhoneNumbers(new java.util.HashSet<>(List.of(phone)));        // When
        ClientResponseDTO dto = mapper.toResponseDTO(client);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.addresses()).hasSize(1);
        assertThat(dto.addresses().get(0).street()).isEqualTo("123 Main St");
        assertThat(dto.phoneNumbers()).hasSize(1);
        assertThat(dto.phoneNumbers().get(0).number()).isEqualTo("123456789");
    }

    @Test
    @DisplayName("Should map Client with empty lists")
    void shouldMapClientWithEmptyLists() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john@example.com");
        client.setDocumentNumber("12345678900");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));
        client.setAddresses(new java.util.HashSet<>());
        client.setPhoneNumbers(new java.util.HashSet<>());        // When
        ClientResponseDTO dto = mapper.toResponseDTO(client);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.addresses()).isEmpty();
        assertThat(dto.phoneNumbers()).isEmpty();
    }

    @Test
    @DisplayName("Should map Client with null lists to empty lists")
    void shouldMapClientWithNullListsToEmptyLists() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john@example.com");
        client.setDocumentNumber("12345678900");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));
        client.setAddresses(null);
        client.setPhoneNumbers(null);

        // When
        ClientResponseDTO dto = mapper.toResponseDTO(client);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.addresses()).isNotNull().isEmpty();
        assertThat(dto.phoneNumbers()).isNotNull().isEmpty();
    }
}