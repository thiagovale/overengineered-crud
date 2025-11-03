package com.example.backend.service;

import com.example.backend.entity.Address;
import com.example.backend.entity.Client;
import com.example.backend.entity.PhoneNumber;
import com.example.backend.exception.ClientNotFoundException;
import com.example.backend.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Transactional
    public Client save(Client client) {
        if (client.getAddresses() != null) {
            client.getAddresses().forEach(address -> address.setClient(client));
        }
        if (client.getPhoneNumbers() != null) {
            client.getPhoneNumbers().forEach(phone -> phone.setClient(client));
        }

        return clientRepository.save(client);
    }


    @Transactional
    public Client update(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setDateOfBirth(clientDetails.getDateOfBirth());
        client.setEmail(clientDetails.getEmail());
        client.setDocumentNumber(clientDetails.getDocumentNumber());


        updateAddresses(client, clientDetails.getAddresses());

        updatePhoneNumbers(client, clientDetails.getPhoneNumbers());

        return clientRepository.save(client);
    }

    private void updateAddresses(Client client, List<Address> newAddresses) {
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

    private void updatePhoneNumbers(Client client, List<PhoneNumber> newPhones) {
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

    @Transactional
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(id);
    }
}
