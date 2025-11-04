package com.example.backend;

import com.example.backend.dto.response.AddressResponseDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.dto.response.PhoneNumberResponseDTO;
import com.example.backend.entity.Address;
import com.example.backend.entity.Client;
import com.example.backend.entity.PhoneNumber;
import com.example.backend.exception.ClientNotFoundException;
import com.example.backend.mapper.ClientMapper;
import com.example.backend.repository.ClientRepository;
import com.example.backend.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    private ClientService clientService;

    private Client client;
    private ClientResponseDTO clientResponseDTO;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, clientMapper);
        // Setup Client entity
        client = new Client();
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
        address.setClient(client);

        PhoneNumber phone = new PhoneNumber();
        phone.setId(1L);
        phone.setNumber("123456789");
        phone.setType("mobile");
        phone.setClient(client);

        client.setAddresses(new java.util.HashSet<>(List.of(address)));
        client.setPhoneNumbers(new java.util.HashSet<>(List.of(phone)));        // Setup DTO
        clientResponseDTO = new ClientResponseDTO(
                1L,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                "12345678900",
                List.of(new AddressResponseDTO(1L, "123 Main St", "City", "ST", "12345")),
                List.of(new PhoneNumberResponseDTO(1L, "123456789", "mobile"))
        );
    }

    @Test
    @DisplayName("Should find all clients successfully")
    void shouldFindAllClients() {
        // Given
        List<Client> clients = List.of(client);
        when(clientRepository.findAllWithRelations()).thenReturn(clients);
        when(clientMapper.toResponseDTOList(clients)).thenReturn(List.of(clientResponseDTO));

        // When
        List<ClientResponseDTO> result = clientService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo("John");
        assertThat(result.get(0).addresses()).hasSize(1);
        assertThat(result.get(0).phoneNumbers()).hasSize(1);
        verify(clientRepository, times(1)).findAllWithRelations();
    }

    @Test
    @DisplayName("Should find client by id successfully")
    void shouldFindClientById() {
        // Given
        when(clientRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toResponseDTO(client)).thenReturn(clientResponseDTO);

        // When
        ClientResponseDTO result = clientService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.addresses()).hasSize(1);
        verify(clientRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    @DisplayName("Should throw exception when client not found")
    void shouldThrowExceptionWhenClientNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(clientRepository.findByIdWithRelations(nonExistentId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> clientService.findById(nonExistentId))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Client not found with id: " + nonExistentId);
        
        verify(clientRepository, times(1)).findByIdWithRelations(nonExistentId);
    }

    @Test
    @DisplayName("Should save client successfully")
    void shouldSaveClient() {
        // Given
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toResponseDTO(client)).thenReturn(clientResponseDTO);

        // When
        ClientResponseDTO result = clientService.save(client);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo("John");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Should delete client successfully")
    void shouldDeleteClient() {
        // Given
        when(clientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(1L);

        // When
        clientService.deleteById(1L);

        // Then
        verify(clientRepository, times(1)).existsById(1L);
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent client")
    void shouldThrowExceptionWhenDeletingNonExistentClient() {
        // Given
        when(clientRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> clientService.deleteById(999L))
                .isInstanceOf(ClientNotFoundException.class);
        verify(clientRepository, never()).deleteById(999L);
    }
}