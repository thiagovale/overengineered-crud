package com.example.backend.controller;

import com.example.backend.config.TraceContext;
import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.dto.response.SuccessResponse;
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
    public ResponseEntity<SuccessResponse<List<ClientResponseDTO>>> getClients() {
        List<Client> clients = clientService.findAll();
        List<ClientResponseDTO> response = clientMapper.toResponseDTOList(clients);
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        ClientResponseDTO response = clientMapper.toResponseDTO(client);
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientService.save(client);
        ClientResponseDTO response = clientMapper.toResponseDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.of("Client created successfully", response, TraceContext.getTraceId())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto) {
        Client clientDetails = clientMapper.toEntity(dto);
        Client updated = clientService.update(id, clientDetails);
        ClientResponseDTO response = clientMapper.toResponseDTO(updated);
        return ResponseEntity.ok(
                SuccessResponse.of("Client updated successfully", response, TraceContext.getTraceId())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.ok(
                SuccessResponse.of("Client deleted successfully", null, TraceContext.getTraceId())
        );
    }
}
