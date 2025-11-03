package com.example.backend.controller;

import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.entity.Client;
import com.example.backend.mapper.ClientMapper;
import com.example.backend.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper)
    {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getClients() {
        List<Client> clients = clientService.findAll();
        List<ClientResponseDTO> response = clientMapper.toResponseDTOList(clients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        return clientService.findById(id)
                .map(clientMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientService.save(client);
        ClientResponseDTO response = clientMapper.toResponseDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto) {
        try {
            Client clientDetails = clientMapper.toEntity(dto);
            Client updated = clientService.update(id, clientDetails);
            ClientResponseDTO response = clientMapper.toResponseDTO(updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
