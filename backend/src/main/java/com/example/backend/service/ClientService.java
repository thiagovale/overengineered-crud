package com.example.backend.service;

import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.entity.Address;
import com.example.backend.entity.Client;
import com.example.backend.entity.PhoneNumber;
import com.example.backend.exception.ClientNotFoundException;
import com.example.backend.mapper.ClientMapper;
import com.example.backend.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;


    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Cacheable(value = "clients", key = "'all'")
    @Transactional
    public List<ClientResponseDTO> findAll() {
        List<Client> clients = clientRepository.findAllWithRelations();
        return clientMapper.toResponseDTOList(clients);
    }

    @Cacheable(value = "clients", key = "#id")
    @Transactional
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toResponseDTO(client);
    }

    @CacheEvict(value = "clients", allEntries = true)
    @Transactional
    public ClientResponseDTO save(Client client) {
        if (client.getAddresses() != null) {
            client.getAddresses().forEach(address -> address.setClient(client));
        }
        if (client.getPhoneNumbers() != null) {
            client.getPhoneNumbers().forEach(phone -> phone.setClient(client));
        }

        Client saved = clientRepository.save(client);
        return clientMapper.toResponseDTO(
                clientRepository.findByIdWithRelations(saved.getId())
                        .orElseThrow(() -> new ClientNotFoundException(saved.getId()))
        );
    }

    @CacheEvict(value = "clients", allEntries = true)
    @Transactional
    public ClientResponseDTO update(Long id, Client clientDetails) {
        Client client = clientRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setDateOfBirth(clientDetails.getDateOfBirth());
        client.setEmail(clientDetails.getEmail());
        client.setDocumentNumber(clientDetails.getDocumentNumber());

        updateAddresses(client, clientDetails.getAddresses());
        updatePhoneNumbers(client, clientDetails.getPhoneNumbers());

        Client updated = clientRepository.save(client);
        return clientMapper.toResponseDTO(
                clientRepository.findByIdWithRelations(updated.getId())
                        .orElseThrow(() -> new ClientNotFoundException(updated.getId()))
        );
    }

    private void updateAddresses(Client client, java.util.Collection<Address> newAddresses) {
        if (newAddresses == null) {
            return;
        }

        client.getAddresses().removeIf(existingAddress ->
                newAddresses.stream()
                        .noneMatch(newAddress -> newAddress.getId() != null &&
                                newAddress.getId().equals(existingAddress.getId()))
        );

        newAddresses.forEach(newAddress -> {
            if (newAddress.getId() != null) {
                client.getAddresses().stream()
                        .filter(existing -> existing.getId().equals(newAddress.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setStreet(newAddress.getStreet());
                            existing.setCity(newAddress.getCity());
                            existing.setState(newAddress.getState());
                            existing.setZipCode(newAddress.getZipCode());
                        });
            } else {
                newAddress.setClient(client);
                client.getAddresses().add(newAddress);
            }
        });
    }

    private void updatePhoneNumbers(Client client, java.util.Collection<PhoneNumber> newPhones) {
        if (newPhones == null) {
            return;
        }

        client.getPhoneNumbers().removeIf(existingPhone ->
                newPhones.stream()
                        .noneMatch(newPhone -> newPhone.getId() != null &&
                                newPhone.getId().equals(existingPhone.getId()))
        );

        newPhones.forEach(newPhone -> {
            if (newPhone.getId() != null) {
                client.getPhoneNumbers().stream()
                        .filter(existing -> existing.getId().equals(newPhone.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setNumber(newPhone.getNumber());
                            existing.setType(newPhone.getType());
                        });
            } else {
                newPhone.setClient(client);
                client.getPhoneNumbers().add(newPhone);
            }
        });
    }

    @CacheEvict(value = "clients", allEntries = true)
    @Transactional
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(id);
    }
}
