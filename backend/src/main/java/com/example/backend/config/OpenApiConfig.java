package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Overengineered CRUD API")
                        .version("1.0.0")
                        .description("API REST completa para gerenciamento de clientes com endereços e telefones. " +
                                "Inclui cache Redis, validações, testes unitários e de integração.\n\n" +
                                "**Como autenticar:**\n" +
                                "1. Crie um usuário em POST `/auth/register`\n" +
                                "2. Faça login em POST `/auth/login` para receber o token JWT\n" +
                                "3. Clique no botão 'Authorize' (🔓) no topo da página\n" +
                                "4. Cole o token JWT no campo 'Value' (apenas o token, sem 'Bearer ')\n" +
                                "5. Clique em 'Authorize' e depois 'Close'\n" +
                                "6. Agora você pode usar todos os endpoints protegidos!")
                        .contact(new Contact()
                                .name("Thiago Vale")
                                .email("thiagopvale@hotmail.com")
                                .url("https://www.linkedin.com/in/thvale/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT obtido no endpoint /auth/login")));
    }
}
