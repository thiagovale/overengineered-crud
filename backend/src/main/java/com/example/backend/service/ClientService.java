package com.example.backend.service;

import com.example.backend.entity.Client;
import com.example.backend.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;


    public ClientService(ClientRepository clientRepository)
    {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }


    @Transactional
    public Client update(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setDateOfBirth(clientDetails.getDateOfBirth());
        client.setEmail(clientDetails.getEmail());
        client.setDocumentNumber(clientDetails.getDocumentNumber());

        client.getAddresses().clear();
        if (clientDetails.getAddresses() != null) {
            clientDetails.getAddresses().forEach(address -> {
                address.setClient(client);
                client.getAddresses().add(address);
            });
        }

        client.getPhoneNumbers().clear();
        if (clientDetails.getPhoneNumbers() != null) {
            clientDetails.getPhoneNumbers().forEach(phone -> {
                phone.setClient(client);
                client.getPhoneNumbers().add(phone);
            });
        }

        return clientRepository.save(client);
    }

    @Transactional
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
