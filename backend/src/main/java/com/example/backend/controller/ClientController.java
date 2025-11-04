package com.example.backend.controller;

import com.example.backend.config.TraceContext;
import com.example.backend.dto.request.ClientRequestDTO;
import com.example.backend.dto.response.ClientResponseDTO;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.mapper.ClientMapper;
import com.example.backend.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@Tag(name = "Clients", description = "API de gerenciamento de clientes")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper)
    {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados, incluindo seus endereços e telefones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ClientResponseDTO>>> getClients() {
        List<ClientResponseDTO> response = clientService.findAll();
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> getClientById(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        ClientResponseDTO response = clientService.findById(id);
        return ResponseEntity.ok(
                SuccessResponse.of(response, TraceContext.getTraceId())
        );
    }

    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente com endereços e telefones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> createClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente a ser criado", required = true)
            @Valid @RequestBody ClientRequestDTO dto) {
        var client = clientMapper.toEntity(dto);
        ClientResponseDTO response = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.of("Client created successfully", response, TraceContext.getTraceId())
        );
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ClientResponseDTO>> updateClient(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do cliente", required = true)
            @Valid @RequestBody ClientRequestDTO dto) {
    var clientDetails = clientMapper.toEntity(dto);
    ClientResponseDTO response = clientService.update(id, clientDetails);
        return ResponseEntity.ok(
                SuccessResponse.of("Client updated successfully", response, TraceContext.getTraceId())
        );
    }

    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteClient(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.ok(
                SuccessResponse.of("Client deleted successfully", null, TraceContext.getTraceId())
        );
    }
}
