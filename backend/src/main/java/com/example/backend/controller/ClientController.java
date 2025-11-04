package com.example.backend.controller;

import com.example.backend.config.TraceContext;
import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.dto.response.SuccessResponse;
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
        List<ClientResponseDTO> response = clientService.findAll();
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> getClientById(@PathVariable Long id) {
        ClientResponseDTO response = clientService.findById(id);
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        var client = clientMapper.toEntity(dto);
        ClientResponseDTO response = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.of("Client created successfully", response, TraceContext.getTraceId())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto) {
    var clientDetails = clientMapper.toEntity(dto);
    ClientResponseDTO response = clientService.update(id, clientDetails);
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
