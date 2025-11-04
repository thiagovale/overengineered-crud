package com.example.backend.mapper;

import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.request.AddressRequestDTO;
import com.example.backend.dto.request.PhoneNumberRequestDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.dto.response.AddressResponseDTO;
import com.example.backend.dto.response.PhoneNumberResponseDTO;
import com.example.backend.entity.Client;
import com.example.backend.entity.Address;
import com.example.backend.entity.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    public ClientResponseDTO toResponseDTO(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getDateOfBirth(),
                client.getEmail(),
                client.getDocumentNumber(),
                client.getAddresses() != null
                        ? client.getAddresses().stream().map(this::toAddressResponseDTO).toList()
                        : Collections.emptyList(),
                client.getPhoneNumbers() != null
                        ? client.getPhoneNumbers().stream().map(this::toPhoneResponseDTO).toList()
                        : Collections.emptyList()
        );
    }

    public AddressResponseDTO toAddressResponseDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }

    public PhoneNumberResponseDTO toPhoneResponseDTO(PhoneNumber phone) {
        return new PhoneNumberResponseDTO(
                phone.getId(),
                phone.getNumber(),
                phone.getType()
        );
    }

    public Client toEntity(ClientRequestDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setDateOfBirth(dto.dateOfBirth());
        client.setEmail(dto.email());
        client.setDocumentNumber(dto.documentNumber());

        if (dto.addresses() != null) {
            java.util.Set<Address> addresses = dto.addresses().stream()
                    .map(this::toAddressEntity)
                    .collect(Collectors.toSet());
            addresses.forEach(address -> address.setClient(client));
            client.setAddresses(addresses);
        }

        if (dto.phoneNumbers() != null) {
            java.util.Set<PhoneNumber> phones = dto.phoneNumbers().stream()
                    .map(this::toPhoneEntity)
                    .collect(Collectors.toSet());
            phones.forEach(phone -> phone.setClient(client));
            client.setPhoneNumbers(phones);
        }

        return client;
    }

    public Address toAddressEntity(AddressRequestDTO dto) {
        Address address = new Address();
        address.setId(dto.id());
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        return address;
    }

    public PhoneNumber toPhoneEntity(PhoneNumberRequestDTO dto) {
        PhoneNumber phone = new PhoneNumber();
        phone.setId(dto.id());
        phone.setNumber(dto.number());
        phone.setType(dto.type());
        return phone;
    }

    public List<ClientResponseDTO> toResponseDTOList(List<Client> clients) {
        return clients.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}