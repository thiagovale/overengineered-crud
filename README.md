## Tasks

### Back end

- [x] Criar setup inicial
- [x] Criar conexão com banco de dados
- [x] Criar migrations do banco de dados
- [x] Criar entities(models) para JPA
- [x] Criar repositories
- [x] Criar services
- [x] Criar controllers
  - [x] Create
  - [x] Read One
  - [x] Read Many
  - [x] Update
  - [x] Delete
- [x] Adicionar logs com persistencia no banco de dados
- [x] Adicionar validação de input nas rotas
- [x] Adicionar autenticacao com JWT
- [x] Adicionar handlers de error e success
- [x] Adicionar documentacao com Swagger
- [x] Implementar fila com RabbitMQ
- [x] Implementar cache com Redis
- [x] Adicionar testes unitarios
- [x] Adicionar testes end to end
- [x] Criar config de docker-compose para redis, rabbitmq e postgresql

### Front end

- [x] Criar setup inicial
- [x] Configurar serviço HTTP e interceptors
- [x] Criar models/interfaces TypeScript
- [x] Criar componente de botão
- [x] Criar componente de input
- [x] Criar componente de formulário reativo
- [x] Criar componente de tabela de listagem
- [x] Criar componente de linha da tabela com ações
- [x] Criar componente modal de client detail
- [x] Implementar client service
- [x] Criar guard de rota para proteção
- [ ] Implementar filtros e ordenação na tabela
- [ ] Adicionar paginação
- [x] Criar componente de login e registro
- [x] Criar rotas

## 📚 Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Como usar o Swagger

1. **Inicie o backend**: `cd backend && ./mvnw spring-boot:run`
2. **Acesse**: http://localhost:8080/swagger-ui/index.html

### Autenticação no Swagger

A maioria dos endpoints requer autenticação JWT. Siga estes passos:

1. **Registre um usuário**:

   - Vá até `Authentication` > `POST /auth/register`
   - Clique em "Try it out"
   - Preencha o JSON:
     ```json
     {
       "username": "joao",
       "password": "senha123"
     }
     ```
   - Clique em "Execute"

2. **Faça login**:

   - Vá até `Authentication` > `POST /auth/login`
   - Clique em "Try it out"
   - Use as mesmas credenciais do registro
   - Clique em "Execute"
   - **Copie o token** retornado (sem as aspas)

3. **Autorize no Swagger**:
   - Clique no botão **"Authorize"** 🔓 no topo da página
   - Cole o token no campo "Value" (apenas o token, sem "Bearer ")
   - Clique em "Authorize" e depois "Close"
   - Pronto! Agora você pode usar todos os endpoints 🎉

### Principais Endpoints

**Authentication** (sem autenticação necessária):

- `POST /auth/register` - Criar novo usuário
- `POST /auth/login` - Fazer login e receber token JWT

**Clients** (requer autenticação):

- `GET /client` - Listar todos os clientes
- `GET /client/{id}` - Buscar cliente por ID
- `POST /client` - Criar novo cliente
- `PUT /client/{id}` - Atualizar cliente
- `DELETE /client/{id}` - Deletar cliente
