### **Como a Clean Architecture Está Sendo Respeitada em Nosso Projeto**

Nosso projeto foi cuidadosamente estruturado seguindo os princípios da Clean Architecture de Robert C. Martin (Uncle Bob), garantindo um sistema **desacoplado, testável, flexível e resiliente a mudanças**. Os pilares dessa arquitetura são as **camadas concêntricas e a Regra da Dependência**, que estabelece que as dependências devem sempre apontar **para dentro**.

Aqui estão os pontos onde a Clean Architecture é visivelmente respeitada:

1.  **Regra da Dependência (Inversão de Dependência - DIP):**
    * **Direção Única:** A regra fundamental da Clean Architecture é que as dependências fluam sempre das camadas externas para as camadas internas. Nossa arquitetura segue isso rigorosamente:
        * `Infrastructure` (detalhes técnicos: Spring, JPA, REST, Swagger) depende do `Core`.
        * `Core` (regras de negócio: Entidades, Use Cases, Gateways/OutputPorts/InputPorts como interfaces) **NÃO depende** da `Infrastructure`.
    * **Exemplos Práticos:**
        * `Use Cases` (Core) dependem de interfaces `Gateways` (Core) e `OutputPorts` (Core), não de implementações concretas de repositórios JPA ou Presenters Spring.
        * `Controllers` (Infraestrutura/API) dependem de interfaces `InputPorts` (Core), não diretamente de `Use Cases` (embora os `InputPorts` os chamem).

2.  **Isolamento do Domínio (Entidades Puras - Camada 1):**
    * Nossas classes `UsuarioDomain`, `ClienteDomain`, `EnderecoDomain`, `ProprietarioDomain` e os `Enums de Domínio` (`GeneroEnum`, `TiposComidaEnum`, etc.) são **puras**.
    * Elas não contêm **nenhuma anotação ou dependência de frameworks externos** (Spring, JPA, Lombok `@Entity`, `@Table`, etc.).
    * Isso significa que as regras de negócio intrínsecas ao nosso restaurante (como um cliente se comporta, o que define um proprietário, os tipos de comida) podem ser entendidas e testadas isoladamente, sem um banco de dados, servidor web ou qualquer outra tecnologia.

3.  **Casos de Uso (Application Business Rules - Camada 2):**
    * As interfaces `CadastrarClienteUseCase`, `BuscarClientePorIdUseCase`, `BuscarTodosClientesUseCase`, `DeletarClienteUseCase` (e suas implementações `*UseCaseImpl`) residem no Core.
    * Elas encapsulam a **lógica de negócio específica da aplicação** (e.g., "cadastrar cliente deve verificar CPF único", "deletar cliente se ele existir").
    * Dependem apenas de `Entidades de Domínio`, interfaces de `Gateways` e interfaces de `OutputPorts` (tudo no Core). Isso as torna agnósticas à infraestrutura.

4.  **Portas e Adaptadores (Interfaces e Implementações - Camada 3):**
    * **Input Ports (`core/controllers`):** Interfaces como `CadastrarClienteInputPort`, `BuscarClientePorIdInputPort`, `BuscarTodosClientesInputPort`, `DeletarClienteInputPort` atuam como contratos que o Core expõe para ser invocado por drivers externos (como a API REST).
    * **Output Ports (`core/domain/presenters`):** Interfaces como `CadastrarClienteOutputPort`, `BuscarClienteOutputPort`, `BuscarTodosClientesOutputPort`, `DeletarClienteOutputPort` atuam como contratos que o Core utiliza para "falar" com os adaptadores de apresentação.
    * **Adapters (Implementações na `infrastructure`):**
        * **Controladores REST (`infrastructure/api/controllers`):** Classes como `ClienteController` são adaptadores do mundo web (HTTP) para o Core, chamando os `InputPorts`. Eles contêm anotações Spring (`@RestController`, `@GetMapping`, etc.) e lidam com DTOs de transporte.
        * **Implementações de Gateways (`infrastructure/data/repositories`):** Classes como `ClienteGatewayImpl`, `UsuarioGatewayImpl` são adaptadores para a persistência de dados. Elas implementam as interfaces de `Gateway` do Core e usam frameworks como Spring Data JPA (`ClienteSpringRepository`, `UsuarioSpringRepository`) e Entidades JPA (`UsuarioEntity`, `ClienteEntity`, `EnderecoEntity`).
        * **Implementações de Presenters (`infrastructure/api/presenters`):** Classes como `CadastrarClientePresenter`, `BuscarClientePorIdPresenter`, `BuscarTodosClientesPresenter`, `DeletarClientePresenter` são adaptadores para a apresentação dos resultados. Elas implementam as interfaces `OutputPort` do Core e convertem `Domain` para `ResponseDTOs` (que são específicos da API).
        * **Mappers (`infrastructure/data/datamappers`):** Classes como `ClienteDataMapperImpl` são adaptadores que convertem entre `Domain Entities` (Core) e `JPA Entities` (Infraestrutura).
        * **Serviços de Infraestrutura (`infrastructure/security`):** Implementações como `PasswordServiceImpl`, `TokenServiceImpl`, `CustomUserDetailsService` são adaptadores para serviços externos (BCrypt, JWT), implementando interfaces do Core ou de frameworks externos.

5.  **DTOs Bem Definidos:**
    * **Input Models (`core/dtos`):** DTOs como `CadastrarClienteInputModel`, `EnderecoInputModel` são puros, sem anotações de framework, usados pelos Use Cases.
    * **Mensagem DTO (`core/dtos`):** `MensagemResponseDTO` é um record simples e puro para mensagens genéricas.
    * **Response DTOs (`infrastructure/api/dtos/response`):** DTOs como `CadastrarClienteResponseDTO`, `ClienteResponseDTO`, `EnderecoResponseDTO`, `DeletarClienteResponseDTO` são específicos da API, contêm anotações de validação e Swagger, e são retornados pelos Controladores.

6.  **Tratamento Centralizado de Erros:**
    * **Exceções de Domínio:** O Core lança `BusinessException` (e suas subclasses como `ResourceNotFoundException`) para indicar falhas de negócio.
    * **`GlobalExceptionHandler` (`infrastructure/api/exceptions`):** Este adaptador de infraestrutura captura as exceções de domínio (e outras exceções de framework/validação) e as mapeia para respostas HTTP (`HttpStatus`) e DTOs de erro (`ExcecaoDTO`) apropriadas. O Core não precisa saber sobre HTTP Status Codes.

Ao seguir esses princípios, nosso projeto garante que a lógica de negócio principal (`Core`) seja **independente de frameworks, bancos de dados e interfaces de usuário/API**, tornando-o mais fácil de testar, manter e adaptar a futuras mudanças tecnológicas ou de requisitos.

# Guia de Criação de Novas Funcionalidades na Clean Architecture

Este guia detalha o processo de criação de uma nova funcionalidade em nossa aplicação, explicando cada etapa, sua camada correspondente na Clean Architecture e como interage com as demais. O objetivo é garantir consistência, desacoplamento e aderência aos princípios de Responsabilidade Única (SRP) e Inversão de Dependência (DIP).

**Exemplo Prático:** Implementação da funcionalidade de **Deletar Cliente por ID**.

---

## 1. Visão Geral do Fluxo (`Deletar Cliente por ID`)

Quando uma requisição HTTP para "deletar cliente" chega à aplicação, ela passará por um fluxo bem definido entre as camadas:

1.  **Infraestrutura (Controller):** Ponto de entrada HTTP.
2.  **Core (Input Port):** Abstração da entrada para o Core.
3.  **Core (Use Case):** A lógica de negócio principal.
4.  **Core (Gateway Interface):** Contrato para a operação de dados.
5.  **Infraestrutura (Gateway Implementação):** Execução da operação no banco de dados.
6.  **Core (Output Port Interface):** Contrato para o Use Case notificar o Presenter.
7.  **Infraestrutura (Presenter Implementação):** Formatação da resposta.
8.  **Infraestrutura (Controller / Global Exception Handler):** Envio da resposta HTTP.

---

## 2. Etapas Detalhadas de Implementação

Seguiremos um fluxo de dentro para fora no Core, e de fora para dentro na Infraestrutura, com as interfaces no Core e suas implementações na Infra.

---

### **CAMADA: Core - Domain - Entities (O Coração da Lógica de Negócio)**

*(As entidades de domínio já devem estar modeladas antes de iniciar uma nova funcionalidade, mas são a base para os Use Cases operarem.)*

* **`UsuarioDomain.java`**
* **`ClienteDomain.java`**
* **`EnderecoDomain.java`**
* **`ProprietarioDomain.java`**
* **Enums de Domínio**

---

### **CAMADA: Core - Exceptions (Exceções de Negócio do Domínio)**

*(Exceções de negócio devem ser definidas no Core para que o domínio não dependa de exceções de infraestrutura.)*

* **`BusinessException.java`**
    * Exceção genérica para erros de negócio.
* **`ResourceNotFoundException.java`**
    * Extensão de `BusinessException` para indicar que um recurso não foi encontrado.

---

### **CAMADA: Core - Domain - Use Cases (Onde a Lógica de Negócio Mora)**

Aqui definimos o "o quê" da funcionalidade.

1.  **Criar a Interface do Use Case: `DeletarClienteUseCase.java`**
    * **Localização:** `core/domain/usecases/cliente/DeletarClienteUseCase.java`
    * **Propósito:** Define o contrato da operação de negócio de deleção.
    * **Interação:** Será implementada pela classe `DeletarClienteUseCaseImpl`.
    * **Código:**
        ```java
        // core/domain/usecases/cliente/DeletarClienteUseCase.java
        package com.postechfiap.meumenu.core.domain.usecases.cliente;

        public interface DeletarClienteUseCase {
            void execute(UUID id);
        }
        ```

2.  **Criar a Implementação do Use Case: `DeletarClienteUseCaseImpl.java`**
    * **Localização:** `core/domain/usecases/cliente/impl/DeletarClienteUseCaseImpl.java`
    * **Propósito:** Contém a lógica de negócio central para deletar um cliente. Ela orquestra as interações com Gateways e Presenters.
    * **Interação:**
        * Implementa `DeletarClienteUseCase`.
        * Injeta `ClienteGateway` (Core) para operações de dados.
        * Injeta `DeletarClienteOutputPort` (Core) para notificar o Presenter sobre o resultado.
        * Lança `ResourceNotFoundException` (Core) se o cliente não for encontrado.
    * **Código:**
        ```java
        // core/domain/usecases/cliente/impl/DeletarClienteUseCaseImpl.java
        package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

        @Service 
        @RequiredArgsConstructor 
        public class DeletarClienteUseCaseImpl implements DeletarClienteUseCase {

            private final ClienteGateway clienteGateway;
            private final DeletarClienteOutputPort deletarClienteOutputPort;

            @Override
            public void execute(UUID id) {
                // 1. Verificar se o cliente existe antes de deletar (regra de negócio)
                Optional<ClienteDomain> clienteOptional = clienteGateway.buscarClientePorId(id);

                if (clienteOptional.isEmpty()) {
                    // Lança uma exceção de domínio. O GlobalExceptionHandler na Infraestrutura a converterá para 404 Not Found.
                    throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
                }

                // 3. Delegar a exclusão ao Gateway
                clienteGateway.deletarCliente(id);

                // 4. Notificar o Presenter sobre o sucesso
                deletarClienteOutputPort.presentSuccess("Cliente com ID " + id + " excluído com sucesso.");
            }
        }
        ```

---

### **CAMADA: Core - Domain - Presenters (As Portas de Saída do Use Case para Apresentação)**

Aqui definimos como o Use Case "fala" sobre seus resultados.

3.  **Criar a Interface do Output Port: `DeletarClienteOutputPort.java`**
    * **Localização:** `core/domain/presenters/DeletarClienteOutputPort.java`
    * **Propósito:** Define o contrato que o Use Case usa para "falar" com a camada de apresentação sobre o resultado da operação (sucesso, em caso de exceções, o GlobalExceptionHandler lida).
    * **Interação:** Será implementada pelo `DeletarClientePresenter` na Infraestrutura e chamada pelo `DeletarClienteUseCaseImpl`.
    * **Código:**
        ```java
        // core/domain/presenters/DeletarClienteOutputPort.java
        package com.postechfiap.meumenu.core.domain.presenters;

        public interface DeletarClienteOutputPort {
            void presentSuccess(String message);
        }
        ```

---

### **CAMADA: Core - Gateways (As Portas de Saída do Use Case para Dados/Serviços Externos)**

Aqui definimos o "contrato" para interação com sistemas externos (banco de dados, outras APIs).

4.  **Adicionar Método à Interface do Gateway: `ClienteGateway.java`**
    * **Localização:** `core/gateways/ClienteGateway.java`
    * **Propósito:** Adiciona o contrato para a operação de deleção de dados. O Core define "o que" o acesso a dados deve fazer.
    * **Interação:** Implementado pelo `ClienteGatewayImpl` na Infraestrutura e chamado pelo `DeletarClienteUseCaseImpl`.
    * **Código (APENAS O MÉTODO NOVO. Mantenha os outros métodos existentes):**
        ```java
        // core/gateways/ClienteGateway.java (apenas o método novo)
        package com.postechfiap.meumenu.core.gateways;

        public interface ClienteGateway {
            ClienteDomain cadastrarCliente(ClienteDomain clienteDomain);
            boolean existsByCpf(String cpf);
            Optional<ClienteDomain> buscarClientePorId(UUID id);
            List<ClienteDomain> buscarTodosClientes();

            void deletarCliente(UUID id);
        }
        ```

---

### **CAMADA: Core - Controllers (As Portas de Entrada do Core para Controladores/Drivers)**

Aqui definimos como os controladores externos devem "chamar" o Core.

5.  **Criar a Interface do Input Port: `DeletarClienteInputPort.java`**
    * **Localização:** `core/controllers/DeletarClienteInputPort.java`
    * **Propósito:** Serve como a interface que a camada de apresentação (o REST Controller na Infraestrutura) deve chamar para iniciar a operação de deleção. Isso desacopla o Controller diretamente do Use Case.
    * **Interação:** Será implementada pelo `DeletarClienteInputPortImpl` e chamada pelo `ClienteController` (Infraestrutura).
    * **Código:**
        ```java
        // core/controllers/DeletarClienteInputPort.java
        package com.postechfiap.meumenu.core.controllers;

        import java.util.UUID;

        public interface DeletarClienteInputPort {
            void execute(UUID id);
        }
        ```

6.  **Criar a Implementação do Input Port: `DeletarClienteInputPortImpl.java`**
    * **Localização:** `core/controllers/impl/DeletarClienteInputPortImpl.java`
    * **Propósito:** Implementar `DeletarClienteInputPort` e, por sua vez, chamar `DeletarClienteUseCase`.
    * **Interação:** Implementa `DeletarClienteInputPort` e injeta `DeletarClienteUseCase`.
    * **Código:**
        ```java
        // core/controllers/impl/DeletarClienteInputPortImpl.java
        package com.postechfiap.meumenu.core.controllers.impl;

        @Component 
        @RequiredArgsConstructor
        public class DeletarClienteInputPortImpl implements DeletarClienteInputPort {

            private final DeletarClienteUseCase deletarClienteUseCase;

            @Override
            public void execute(UUID id) {
                deletarClienteUseCase.execute(id); 
            }
        }
        ```

---

#### **CAMADA: Infrastructure (Detalhes de Implementação, Frameworks, Adapters)**

Aqui implementamos os "como" (acesso a dados, web, segurança, etc.).

##### **CAMADA: Infrastructure - Data - Repositories (Implementação dos Gateways)**

7.  **Atualizar a Implementação do Gateway: `ClienteGatewayImpl.java`**
    * **Localização:** `infrastructure/data/repositories/ClienteGatewayImpl.java`
    * **Propósito:** Fornecer a implementação concreta do `ClienteGateway`, utilizando o Spring Data JPA para interagir com o banco de dados.
    * **Interação:** Implementa `ClienteGateway` e utiliza `ClienteSpringRepository` (o JpaRepository).
    * **Código (APENAS O MÉTODO NOVO. Mantenha os outros métodos existentes):**
        ```java
        // infrastructure/data/repositories/ClienteGatewayImpl.java (apenas o método novo)
        package com.postechfiap.meumenu.infrastructure.data.repositories;

        @Component
        @RequiredArgsConstructor
        public class ClienteGatewayImpl implements ClienteGateway {

            private final ClienteSpringRepository clienteSpringRepository;
            private final ClienteDataMapper clienteDataMapper; // ... e outros que você já tem

            // ... outros métodos existentes (cadastrarCliente, existsByCpf, buscarClientePorId, buscarTodosClientes) ...

            @Override
            public void deletarCliente(UUID id) {
                clienteSpringRepository.deleteById(id);
            }
        }
        ```

##### **CAMADA: Infrastructure - API - DTOs - Response (Modelos de Resposta da API)**

Aqui definimos os formatos de dados retornados pela API (View Models).

8.  **Criar o DTO de Resposta para Mensagens Simples: `MensagemResponseDTO.java`**
    * **Localização:** `core/dtos/MensagemResponseDTO.java`
    * **Propósito:** Ser um DTO simples e puro do Core para mensagens de sucesso/erro. Embora usado por DTOs de Infraestrutura, ele é genérico e não tem dependências de framework.
    * **Interação:** Será "herdado" semanticamente por DTOs de resposta da Infraestrutura.
    * **Código:**
        ```java
        // core/dtos/MensagemResponseDTO.java
        package com.postechfiap.meumenu.core.dtos;

        public record MensagemResponseDTO(
                String message,
                String status // Ex: "SUCCESS", "FAIL", "ERROR", "NOT_FOUND"
        ) {
        }
        ```

9.  **Criar o DTO de Resposta de Deleção: `DeletarClienteResponseDTO.java`**
    * **Localização:** `infrastructure/api/dtos/response/DeletarClienteResponseDTO.java`
    * **Propósito:** Definir a estrutura dos dados retornados pela API especificamente para a operação de deleção.
    * **Interação:** Utilizado pelo `DeletarClientePresenter` e retornado pelo `ClienteController`.
    * **Código:**
        ```java
        // infrastructure/api/dtos/response/DeletarClienteResponseDTO.java
        package com.postechfiap.meumenu.infrastructure.api.dtos.response;

        import io.swagger.v3.oas.annotations.media.Schema;

        @Schema(description = "DTO para resposta de deleção de Cliente")
        public record DeletarClienteResponseDTO(
                String message,
                String status
        ) {
        }
        ```

##### **CAMADA: Infrastructure - API - Presenters (Implementação dos Output Ports)**

Aqui formatamos a saída do Core para a apresentação.

10. **Criar a Implementação do Presenter: `DeletarClientePresenter.java`**
    * **Localização:** `infrastructure/api/presenters/DeletarClientePresenter.java`
    * **Propósito:** Formatar a mensagem de sucesso da operação de deleção para o `DeletarClienteResponseDTO`.
    * **Interação:** Implementa `DeletarClienteOutputPort` e interage com `DeletarClienteResponseDTO`.
    * **Código:**
        ```java
        // infrastructure/api/presenters/DeletarClientePresenter.java
        package com.postechfiap.meumenu.infrastructure.api.presenters;

        @Component
        @Getter 
        @Setter 
        @NoArgsConstructor
        public class DeletarClientePresenter implements DeletarClienteOutputPort {

            private DeletarClienteResponseDTO viewModel;

            @Override
            public void presentSuccess(String message) {
                this.viewModel = new DeletarClienteResponseDTO(
                        message,
                        "SUCCESS"
                );
            }

            public DeletarClienteResponseDTO getViewModel() {
                return viewModel;
            }
        }
        ```

##### **CAMADA: Infrastructure - API - Controllers (Pontos de Entrada da API)**

Aqui expomos a funcionalidade via HTTP.

11. **Atualizar o REST Controller: `ClienteController.java`**
    * **Localização:** `infrastructure/api/controllers/ClienteController.java`
    * **Propósito:** Expor o endpoint HTTP para a deleção de clientes.
    * **Interação:**
        * Injeta `DeletarClienteInputPort` (Core).
        * Injeta `DeletarClientePresenter` (Infraestrutura).
        * Define o `@DeleteMapping`.
        * **Não** contém `try-catch` para exceções de negócio, pois o `GlobalExceptionHandler` (Infraestrutura) se encarregará de `ResourceNotFoundException` ou `BusinessException`.
    * **Código (APENAS O MÉTODO NOVO E INJEÇÕES. Mantenha os outros métodos existentes):**
        ```java
        // infrastructure/api/controllers/ClienteController.java (apenas o método novo e injeções)
        package com.postechfiap.meumenu.infrastructure.api.controllers;

        // ... imports existentes ...

        @RestController
        @Tag(name = "Cliente Controller", description = "Operações relacionadas ao Cliente")
        @RequestMapping("/clientes")
        @RequiredArgsConstructor
        public class ClienteController {

            private final CadastrarClienteInputPort cadastrarClienteInputPort;
            private final BuscarClientePorIdInputPort buscarClientePorIdInputPort;
            private final BuscarTodosClientesInputPort buscarTodosClientesInputPort;
            private final DeletarClienteInputPort deletarClienteInputPort; // NOVO: Injetar o InputPort

            private final CadastrarClientePresenter cadastrarClientePresenter;
            private final BuscarClientePorIdPresenter buscarClientePorIdPresenter;
            private final BuscarTodosClientesPresenter buscarTodosClientesPresenter;
            private final DeletarClientePresenter deletarClientePresenter; // NOVO: Injetar o Presenter

            // ... métodos POST e GET existentes ...

            @Operation(
                    summary = "Deleta um cliente por ID",
                    description = "Este endpoint deleta um cliente específico pelo seu ID."
            )
            @DeleteMapping("/{id}")
            public ResponseEntity<Void> deletarCliente(@PathVariable UUID id) {
                deletarClienteInputPort.execute(id);
                
                return ResponseEntity.noContent().build(); // Retorna 204 No Content (padrão RESTful para deleção bem-sucedida sem corpo)

                // Se você quiser retornar a mensagem de sucesso no corpo da resposta (200 OK), use:
                // DeletarClienteResponseDTO responseDTO = deletarClientePresenter.getViewModel();
                // return ResponseEntity.ok(responseDTO);
            }
        }
        ```

## Funcionalidade: Cadastro de Proprietário

Este guia detalha o processo de implementação da funcionalidade de "Cadastro de Proprietário", explicando cada etapa, sua camada correspondente na Clean Architecture e como interage com as demais. O objetivo é garantir consistência, desacoplamento e aderência aos princípios de Responsabilidade Única (SRP) e Inversão de Dependência (DIP).

---

### **VISÃO GERAL DO FLUXO (`Cadastro de Proprietário`)**

Quando uma requisição para "cadastrar proprietário" chega, ela passará pelas seguintes camadas:

1.  **Infraestrutura (Controller):** Ponto de entrada HTTP.
2.  **Core (Input Port):** Abstração da entrada para o Core.
3.  **Core (Use Case):** A lógica de negócio principal (validações, criptografia, criação da entidade).
4.  **Core (Gateway Interface):** Contrato para a persistência.
5.  **Infraestrutura (Gateway Implementação):** Realiza a operação no banco de dados.
6.  **Core (Output Port Interface):** Contrato para o Use Case notificar o Presenter.
7.  **Infraestrutura (Presenter Implementação):** Formatação da resposta.
8.  **Infraestrutura (Controller):** Envia a resposta HTTP.
9.  **Infraestrutura (Global Exception Handler):** Captura e trata exceções lançadas por qualquer camada, retornando respostas padronizadas.

---

### **ETAPAS DETALHADAS DE IMPLEMENTAÇÃO**

Seguiremos um fluxo que percorre as camadas da arquitetura, construindo as interfaces e suas implementações nas camadas apropriadas.

---

### **CAMADA: Core - Domain - Entities (O Coração da Lógica de Negócio)**

*(As entidades de domínio são a representação pura das regras de negócio e devem ser definidas antes de iniciar a funcionalidade.)*

* **`ProprietarioDomain.java`**: (Com campo `cpf` e construtores atualizados)
    * **Localização:** `core/domain/entities/ProprietarioDomain.java`
    * **Propósito:** Representa a entidade de negócio "Proprietário", com seus atributos e comportamento.
    * **Interação:** Utilizada pelo `CadastrarProprietarioUseCaseImpl` e mapeada pelo `ProprietarioDataMapperImpl`.
    * **Código:**
        ```java
        // core/domain/entities/ProprietarioDomain.java
        package com.postechfiap.meumenu.core.domain.entities;

        import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        import java.time.LocalDateTime;
        import java.util.List;
        import java.util.UUID;

        @Getter
        @Setter
        @NoArgsConstructor
        public class ProprietarioDomain extends UsuarioDomain {

            private String cpf; // NOVO: Campo CPF
            private String whatsapp;
            private StatusContaEnum statusConta;

            // Construtor completo para mapeamento do banco de dados
            public ProprietarioDomain(UUID id, String cpf, String whatsapp, StatusContaEnum statusConta,
                                      String nome, String email, String login, String senha,
                                      LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoDomain> enderecos) {
                super(id, nome, email, login, senha, dataCriacao, dataAtualizacao, enderecos);
                this.cpf = cpf;
                this.whatsapp = whatsapp;
                this.statusConta = statusConta;
            }

            // Construtor para CRIAÇÃO de um NOVO Proprietário (a partir do InputModel)
            public ProprietarioDomain(String nome, String email, String login, String senha,
                                      String cpf, String whatsapp) {
                super(nome, email, login, senha); // Chama o construtor da superclasse que gera ID e datas
                this.cpf = cpf;
                this.whatsapp = whatsapp;
                this.statusConta = StatusContaEnum.ATIVO; // Exemplo: padrão para novo proprietário
            }
        }
        ```

---

### **CAMADA: Core - DTOs (Modelos de Dados Puros para o Core)**

Aqui definimos os modelos de dados que o Core opera, sem dependências de frameworks.

1.  **`CadastrarProprietarioInputModel.java`**
    * **Localização:** `core/dtos/proprietario/CadastrarProprietarioInputModel.java`
    * **Propósito:** Receber os dados de entrada para o caso de uso de cadastro de proprietário.
    * **Interação:** Preenchido pelo `CadastrarProprietarioRequestDTO.toInputModel()` (Infraestrutura) e consumido pelo `CadastrarProprietarioUseCase`.
    * **Código:**
        ```java
        // core/dtos/proprietario/CadastrarProprietarioInputModel.java
        package com.postechfiap.meumenu.core.dtos.proprietario;

        import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        import java.util.List;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class CadastrarProprietarioInputModel {

            private String nome;
            private String email;
            private String login;
            private String senha;
            private String cpf; // NOVO: Campo CPF
            private String whatsapp;
            private List<EnderecoInputModel> enderecos;
        }
        ```

---

### **CAMADA: Core - Domain - Use Cases (Onde a Lógica de Negócio Mora)**

Aqui definimos o "o quê" da funcionalidade.

2.  **Criar a Interface do Use Case: `CadastrarProprietarioUseCase.java`**
    * **Localização:** `core/domain/usecases/proprietario/CadastrarProprietarioUseCase.java`
    * **Propósito:** Define o contrato da operação de negócio de cadastro de proprietário.
    * **Interação:** Será implementada pela classe `CadastrarProprietarioUseCaseImpl` e chamada pelo `CadastrarProprietarioInputPort`.
    * **Código:**
        ```java
        // core/domain/usecases/proprietario/CadastrarProprietarioUseCase.java
        package com.postechfiap.meumenu.core.domain.usecases.proprietario;

        import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;

        public interface CadastrarProprietarioUseCase {
            void execute(CadastrarProprietarioInputModel input);
        }
        ```

3.  **Criar a Implementação do Use Case: `CadastrarProprietarioUseCaseImpl.java`**
    * **Localização:** `core/domain/usecases/proprietario/impl/CadastrarProprietarioUseCaseImpl.java`
    * **Propósito:** Contém a lógica de negócio central para cadastrar um proprietário. Orquestra interações com Gateways e Presenters.
    * **Interação:**
        * Implementa `CadastrarProprietarioUseCase`.
        * Injeta `ProprietarioGateway` (Core) para persistência.
        * Injeta `UsuarioGateway` (Core) para validações de unicidade de `login`/`email`.
        * Injeta `PasswordService` (Core) para criptografia de senha.
        * Injeta `CadastrarProprietarioOutputPort` (Core) para notificar o Presenter.
        * Lança `BusinessException` (Core) em caso de falha de negócio (e.g., login já cadastrado).
    * **Código:**
        ```java
        // core/domain/usecases/proprietario/impl/CadastrarProprietarioUseCaseImpl.java
        package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
        import com.postechfiap.meumenu.core.domain.presenters.CadastrarProprietarioOutputPort;
        import com.postechfiap.meumenu.core.domain.services.PasswordService;
        import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
        import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
        import com.postechfiap.meumenu.core.exceptions.BusinessException;
        import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
        import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.stream.Collectors;

        @Service // Marca como um Bean do Spring
        @RequiredArgsConstructor // Gera construtor com campos final
        public class CadastrarProprietarioUseCaseImpl implements CadastrarProprietarioUseCase {

            private final ProprietarioGateway proprietarioGateway;
            private final UsuarioGateway usuarioGateway;
            private final PasswordService passwordService;
            private final CadastrarProprietarioOutputPort cadastrarProprietarioOutputPort;

            @Override
            public void execute(CadastrarProprietarioInputModel input) {
                // 1. Validações de negócio (unicidade de login/email/CPF)
                if (usuarioGateway.existsByLogin(input.getLogin())) {
                    cadastrarProprietarioOutputPort.presentError("Login já cadastrado.");
                    throw new BusinessException("Login já cadastrado.");
                }
                if (usuarioGateway.existsByEmail(input.getEmail())) {
                    cadastrarProprietarioOutputPort.presentError("Email já cadastrado.");
                    throw new BusinessException("Email já cadastrado.");
                }
                // Validação de CPF para Proprietário
                if (proprietarioGateway.existsByCpf(input.getCpf())) {
                    cadastrarProprietarioOutputPort.presentError("CPF já cadastrado.");
                    throw new BusinessException("CPF já cadastrado.");
                }

                // 2. Criptografar a senha
                String senhaCriptografada = passwordService.encryptPassword(input.getSenha());

                // 3. Criar a Entidade ProprietarioDomain
                ProprietarioDomain novoProprietario = new ProprietarioDomain(
                        input.getNome(), input.getEmail(), input.getLogin(), senhaCriptografada,
                        input.getCpf(), // Passando o CPF
                        input.getWhatsapp()
                );

                // 4. Mapear Endereços de InputModel para EnderecoDomain
                List<EnderecoDomain> enderecosDomain = input.getEnderecos().stream()
                        .map(enderecoInput -> new EnderecoDomain(
                                enderecoInput.getEstado(),
                                enderecoInput.getCidade(),
                                enderecoInput.getBairro(),
                                enderecoInput.getRua(),
                                enderecoInput.getNumero(),
                                enderecoInput.getComplemento(),
                                enderecoInput.getCep()
                        ))
                        .collect(Collectors.toList());

                // 5. Associar cada EnderecoDomain ao novoProprietario (relação bidirecional)
                enderecosDomain.forEach(endereco -> endereco.setUsuario(novoProprietario));

                // 6. Setar a lista de endereços no ProprietarioDomain (que herda de UsuarioDomain)
                novoProprietario.setEnderecos(enderecosDomain);

                // 7. Persistir a entidade via Gateway
                ProprietarioDomain proprietarioSalvo = proprietarioGateway.cadastrarProprietario(novoProprietario);

                // 8. Notificar o Presenter sobre o sucesso
                cadastrarProprietarioOutputPort.presentSuccess(proprietarioSalvo);
            }
        }
        ```

---

### **CAMADA: Core - Domain - Presenters (As Portas de Saída do Use Case para Apresentação)**

Aqui definimos como o Use Case "fala" sobre seus resultados para a camada de apresentação.

4.  **Criar a Interface do Output Port: `CadastrarProprietarioOutputPort.java`**
    * **Localização:** `core/domain/presenters/CadastrarProprietarioOutputPort.java`
    * **Propósito:** Define o contrato que o Use Case usa para "falar" com a camada de apresentação sobre o resultado do cadastro de proprietário.
    * **Interação:** Será implementada pelo `CadastrarProprietarioPresenter` na Infraestrutura e chamada pelo `CadastrarProprietarioUseCaseImpl`.
    * **Código:**
        ```java
        // core/domain/presenters/CadastrarProprietarioOutputPort.java
        package com.postechfiap.meumenu.core.domain.presenters;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

        public interface CadastrarProprietarioOutputPort {
            void presentSuccess(ProprietarioDomain proprietario); // Para sucesso no cadastro
            void presentError(String message); // Para erro no cadastro (BusinessException)
        }
        ```

---

### **CAMADA: Core - Gateways (As Portas de Saída do Use Case para Dados/Serviços Externos)**

Aqui definimos o "contrato" para interação com sistemas externos (banco de dados, outras APIs).

5.  **Criar a Interface do Gateway: `ProprietarioGateway.java`**
    * **Localização:** `core/gateways/ProprietarioGateway.java`
    * **Propósito:** Define o contrato para as operações de acesso a dados de Proprietário que o Use Case precisará.
    * **Interação:** Implementada pelo `ProprietarioGatewayImpl` na Infraestrutura e chamada pelo `CadastrarProprietarioUseCaseImpl`.
    * **Código:**
        ```java
        // core/gateways/ProprietarioGateway.java
        package com.postechfiap.meumenu.core.gateways;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import java.util.Optional;
        import java.util.UUID;

        public interface ProprietarioGateway {
            ProprietarioDomain cadastrarProprietario(ProprietarioDomain proprietarioDomain); // Para persistência
            Optional<ProprietarioDomain> buscarProprietarioPorId(UUID id); // Para busca por ID
            boolean existsByCpf(String cpf); // Para verificação de unicidade
        }
        ```

---

### **CAMADA: Core - Controllers (As Portas de Entrada do Core para Controladores/Drivers)**

Aqui definimos como os controladores externos devem "chamar" o Core, garantindo o desacoplamento.

6.  **Criar a Interface do Input Port: `CadastrarProprietarioInputPort.java`**
    * **Localização:** `core/controllers/CadastrarProprietarioInputPort.java`
    * **Propósito:** Serve como a interface que a camada de apresentação (o REST Controller na Infraestrutura) deve chamar para iniciar a operação de cadastro de proprietário. Desacopla o Controller diretamente do Use Case.
    * **Interação:** Será implementada pelo `CadastrarProprietarioInputPortImpl` e chamada pelo `ProprietarioController` (Infraestrutura).
    * **Código:**
        ```java
        // core/controllers/CadastrarProprietarioInputPort.java
        package com.postechfiap.meumenu.core.controllers;

        import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;

        public interface CadastrarProprietarioInputPort {
            void execute(CadastrarProprietarioInputModel input);
        }
        ```

7.  **Criar a Implementação do Input Port: `CadastrarProprietarioInputPortImpl.java`**
    * **Localização:** `core/controllers/impl/CadastrarProprietarioInputPortImpl.java`
    * **Propósito:** Implementa `CadastrarProprietarioInputPort` e, por sua vez, chama `CadastrarProprietarioUseCase`.
    * **Interação:** Implementa `CadastrarProprietarioInputPort` e injeta `CadastrarProprietarioUseCase`.
    * **Código:**
        ```java
        // core/controllers/impl/CadastrarProprietarioInputPortImpl.java
        package com.postechfiap.meumenu.core.controllers.impl;

        import com.postechfiap.meumenu.core.controllers.CadastrarProprietarioInputPort;
        import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
        import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Component;

        @Component // Marca como um Bean do Spring
        @RequiredArgsConstructor
        public class CadastrarProprietarioInputPortImpl implements CadastrarProprietarioInputPort {

            private final CadastrarProprietarioUseCase cadastrarProprietarioUseCase;

            @Override
            public void execute(CadastrarProprietarioInputModel input) {
                cadastrarProprietarioUseCase.execute(input); // Delega para o Use Case.
            }
        }
        ```

---

### **CAMADA: Infrastructure (Detalhes de Implementação, Frameworks, Adapters)**

Aqui implementamos os "como" (acesso a dados, web, segurança, etc.).

##### **CAMADA: Infrastructure - Data - Mappers (Mapeamento entre Domínio e Persistência)**

8.  **Criar a Interface do Mapper: `ProprietarioDataMapper.java`**
    * **Localização:** `infrastructure/data/datamappers/ProprietarioDataMapper.java`
    * **Propósito:** Definir o contrato para o mapeamento bidirecional entre `ProprietarioDomain` (Core) e `ProprietarioEntity` (Infraestrutura).
    * **Interação:** Implementada pelo `ProprietarioDataMapperImpl`.
    * **Código:**
        ```java
        // infrastructure/data/datamappers/ProprietarioDataMapper.java
        package com.postechfiap.meumenu.infrastructure.data.datamappers;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
        import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
        import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;

        import java.util.List;

        public interface ProprietarioDataMapper {
            ProprietarioEntity toEntity(ProprietarioDomain domain);
            ProprietarioDomain toDomain(ProprietarioEntity entity);

            // Métodos auxiliares para mapeamento de Endereço (se não houver um EnderecoDataMapper genérico)
            EnderecoEntity toEnderecoEntity(EnderecoDomain domain);
            EnderecoDomain toEnderecoDomain(EnderecoEntity entity);
            List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList);
            List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList);
        }
        ```

9.  **Criar a Implementação do Mapper: `ProprietarioDataMapperImpl.java`**
    * **Localização:** `infrastructure/data/datamappers/impl/ProprietarioDataMapperImpl.java`
    * **Propósito:** Implementar a `ProprietarioDataMapper` e realizar o mapeamento entre `ProprietarioDomain` e `ProprietarioEntity`, incluindo a lista de endereços.
    * **Interação:** Implementa `ProprietarioDataMapper` e é injetada no `ProprietarioGatewayImpl`.
    * **Código:**
        ```java
        // infrastructure/data/datamappers/impl/ProprietarioDataMapperImpl.java
        package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
        import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
        import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
        import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
        import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
        import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
        import org.springframework.stereotype.Component;

        import java.util.List;
        import java.util.stream.Collectors;

        @Component // Marca como um Bean do Spring
        public class ProprietarioDataMapperImpl implements ProprietarioDataMapper {

            @Override
            public ProprietarioEntity toEntity(ProprietarioDomain domain) {
                if (domain == null) return null;

                ProprietarioEntity entity = new ProprietarioEntity();
                // Mapear campos de UsuarioDomain para UsuarioEntity (superclasse)
                entity.setId(domain.getId());
                entity.setNome(domain.getNome());
                entity.setEmail(domain.getEmail());
                entity.setLogin(domain.getLogin());
                entity.setSenha(domain.getSenha());
                entity.setDataCriacao(domain.getDataCriacao());
                entity.setDataAtualizacao(domain.getDataAtualizacao());

                // Mapear campos específicos de ProprietarioDomain
                entity.setCpf(domain.getCpf());
                entity.setWhatsapp(domain.getWhatsapp());
                entity.setStatusConta(domain.getStatusConta());

                // Mapear Endereços e setar a relação inversa
                List<EnderecoEntity> enderecosEntities = toEnderecoEntityList(domain.getEnderecos());
                if (enderecosEntities != null) {
                    enderecosEntities.forEach(e -> e.setUsuario(entity));
                }
                entity.setEnderecos(enderecosEntities);

                return entity;
            }

            @Override
            public ProprietarioDomain toDomain(ProprietarioEntity entity) {
                if (entity == null) return null;

                // Cria o ProprietarioDomain usando o construtor completo
                ProprietarioDomain domain = new ProprietarioDomain(
                        entity.getId(),
                        entity.getCpf(),
                        entity.getWhatsapp(),
                        entity.getStatusConta(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getLogin(),
                        entity.getSenha(),
                        entity.getDataCriacao(),
                        entity.getDataAtualizacao(),
                        toEnderecoDomainList(entity.getEnderecos())
                );
                return domain;
            }

            @Override
            public EnderecoEntity toEnderecoEntity(EnderecoDomain domain) {
                if (domain == null) return null;
                EnderecoEntity entity = new EnderecoEntity();
                entity.setId(domain.getId());
                entity.setEstado(domain.getEstado());
                entity.setCidade(domain.getCidade());
                entity.setBairro(domain.getBairro());
                entity.setRua(domain.getRua());
                entity.setNumero(domain.getNumero());
                entity.setComplemento(domain.getComplemento());
                entity.setCep(domain.getCep());
                return entity;
            }

            @Override
            public EnderecoDomain toEnderecoDomain(EnderecoEntity entity) {
                if (entity == null) return null;

                UsuarioDomain usuarioDomain = null;
                if (entity.getUsuario() != null) {
                    usuarioDomain = new UsuarioDomain(
                            entity.getUsuario().getId(),
                            entity.getUsuario().getNome(),
                            entity.getUsuario().getEmail(),
                            entity.getUsuario().getLogin(),
                            entity.getUsuario().getSenha(),
                            entity.getUsuario().getDataCriacao(),
                            entity.getUsuario().getDataAtualizacao(),
                            null // Importante: null para endereços aqui para evitar recursão infinita
                    );
                }

                EnderecoDomain domain = new EnderecoDomain(
                        entity.getId(),
                        entity.getEstado(),
                        entity.getCidade(),
                        entity.getBairro(),
                        entity.getRua(),
                        entity.getNumero(),
                        entity.getComplemento(),
                        entity.getCep(),
                        usuarioDomain
                );
                return domain;
            }

            @Override
            public List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList) {
                if (domainList == null) return null;
                return domainList.stream()
                        .map(this::toEnderecoEntity)
                        .collect(Collectors.toList());
            }

            @Override
            public List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList) {
                if (entityList == null) return null;
                return entityList.stream()
                        .map(this::toEnderecoDomain)
                        .collect(Collectors.toList());
            }
        }
        ```

##### **CAMADA: Infrastructure - Data - Repositories (Implementação dos Gateways)**

Aqui as interfaces de Gateway do Core são implementadas usando tecnologias de persistência.

10. **Criar a Interface do Repositório Spring Data JPA: `ProprietarioSpringRepository.java`**
    * **Localização:** `infrastructure/data/repositories/ProprietarioSpringRepository.java`
    * **Propósito:** Fornecer a interface de repositório do Spring Data JPA para a entidade `ProprietarioEntity`, com operações CRUD e métodos de busca customizados.
    * **Interação:** Utilizada pelo `ProprietarioGatewayImpl`.
    * **Código:**
        ```java
        // infrastructure/data/repositories/ProprietarioSpringRepository.java
        package com.postechfiap.meumenu.infrastructure.data.repositories;

        import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.Optional;
        import java.util.UUID;

        public interface ProprietarioSpringRepository extends JpaRepository<ProprietarioEntity, UUID> {
            Optional<ProprietarioEntity> findByCpf(String cpf); // Método para buscar por CPF
        }
        ```

11. **Criar a Implementação do Gateway: `ProprietarioGatewayImpl.java`**
    * **Localização:** `infrastructure/data/repositories/ProprietarioGatewayImpl.java`
    * **Propósito:** Implementa `ProprietarioGateway` (Core) e realiza a persistência de Proprietários utilizando `ProprietarioSpringRepository` e `ProprietarioDataMapper`.
    * **Interação:** Implementa `ProprietarioGateway`, injeta `ProprietarioSpringRepository` e `ProprietarioDataMapper`.
    * **Código:**
        ```java
        // infrastructure/data/repositories/ProprietarioGatewayImpl.java
        package com.postechfiap.meumenu.infrastructure.data.repositories;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
        import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
        import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Component;

        import java.util.Optional;
        import java.util.UUID;

        @Component // Marca como um Bean do Spring
        @RequiredArgsConstructor
        public class ProprietarioGatewayImpl implements ProprietarioGateway {

            private final ProprietarioSpringRepository proprietarioSpringRepository;
            private final ProprietarioDataMapper proprietarioDataMapper;

            @Override
            public ProprietarioDomain cadastrarProprietario(ProprietarioDomain proprietarioDomain) {
                ProprietarioEntity proprietarioEntity = proprietarioDataMapper.toEntity(proprietarioDomain);
                ProprietarioEntity savedEntity = proprietarioSpringRepository.save(proprietarioEntity);
                return proprietarioDataMapper.toDomain(savedEntity);
            }

            @Override
            public Optional<ProprietarioDomain> buscarProprietarioPorId(UUID id) {
                Optional<ProprietarioEntity> proprietarioEntityOptional = proprietarioSpringRepository.findById(id);
                return proprietarioEntityOptional.map(proprietarioDataMapper::toDomain);
            }

            @Override
            public boolean existsByCpf(String cpf) {
                return proprietarioSpringRepository.findByCpf(cpf).isPresent();
            }
        }
        ```

---

##### **CAMADA: Infrastructure - API - DTOs - Request (Modelos de Requisição da API)**

Aqui definimos os formatos de dados esperados nas requisições HTTP.

12. **Criar o DTO de Requisição: `CadastrarProprietarioRequestDTO.java`**
    * **Localização:** `infrastructure/api/dtos/request/CadastrarProprietarioRequestDTO.java`
    * **Propósito:** Receber os dados da requisição HTTP para o cadastro de proprietário, incluindo validações e anotações Swagger.
    * **Interação:** Recebido pelo `ProprietarioController` e convertido para `CadastrarProprietarioInputModel`.
    * **Código:**
        ```java
        // infrastructure/api/dtos/request/CadastrarProprietarioRequestDTO.java
        package com.postechfiap.meumenu.infrastructure.api.dtos.request;

        import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
        import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
        import com.postechfiap.meumenu.infrastructure.api.dtos.endereco.EnderecoRequestDTO; // Se EnderecoRequestDTO estiver em um pacote diferente
        import io.swagger.v3.oas.annotations.media.Schema;
        import jakarta.validation.Valid;
        import jakarta.validation.constraints.Email;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Pattern;
        import jakarta.validation.constraints.Size;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.stream.Collectors;

        @Schema(description = "DTO para requisição de cadastro de um novo Proprietário")
        public record CadastrarProprietarioRequestDTO(

                @Schema(description = "Nome completo do proprietário. Precisa estar preenchido.", example = "Maria Silva")
                @NotBlank(message = "Nome é obrigatório")
                @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
                String nome,

                @Schema(description = "Endereço de e-mail do proprietário. Precisa estar preenchido e ser válido.", example = "maria.silva@example.com")
                @NotBlank(message = "Email é obrigatório")
                @Email(message = "Email inválido")
                @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
                String email,

                @Schema(description = "Login para acesso. Precisa estar preenchido.", example = "maria.silva")
                @NotBlank(message = "Login é obrigatório")
                @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
                String login,

                @Schema(description = "Senha do proprietário. Precisa estar preenchida e ser forte.", example = "SenhaForte456!")
                @NotBlank(message = "Senha é obrigatória")
                @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
                String senha,

                @Schema(description = "CPF do proprietário (apenas números). Precisa estar preenchido.", example = "12345678900")
                @NotBlank(message = "CPF é obrigatório")
                @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
                String cpf,

                @Schema(description = "Número de WhatsApp do proprietário (apenas números, com ou sem DDI).", example = "5511987654321")
                @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de WhatsApp inválido")
                @NotBlank(message = "WhatsApp é obrigatório")
                String whatsapp,

                @Schema(description = "Lista de endereços do proprietário (pelo menos 1).", minLength = 1)
                @NotNull(message = "A lista de endereços não pode ser nula")
                @Size(min = 1, message = "Pelo menos um endereço deve ser informado")
                @Valid
                List<EnderecoRequestDTO> enderecos
        ) {
            public CadastrarProprietarioInputModel toInputModel() {
                List<EnderecoInputModel> safeEnderecos = enderecos != null
                        ? enderecos.stream().map(EnderecoRequestDTO::toInputModel).collect(Collectors.toList())
                        : new ArrayList<>();

                return new CadastrarProprietarioInputModel(
                        nome,
                        email,
                        login,
                        senha,
                        cpf,
                        whatsapp,
                        safeEnderecos
                );
            }
        }
        ```

---

##### **CAMADA: Infrastructure - API - DTOs - Response (Modelos de Resposta da API)**

Aqui definimos os formatos de dados retornados pela API (View Models).

13. **Criar o DTO de Resposta: `CadastrarProprietarioResponseDTO.java`**
    * **Localização:** `infrastructure/api/dtos/response/CadastrarProprietarioResponseDTO.java`
    * **Propósito:** Definir a estrutura dos dados retornados pela API como resposta a uma operação de cadastro de proprietário.
    * **Interação:** Utilizado pelo `CadastrarProprietarioPresenter` e retornado pelo `ProprietarioController`.
    * **Código:**
        ```java
        // infrastructure/api/dtos/response/CadastrarProprietarioResponseDTO.java
        package com.postechfiap.meumenu.infrastructure.api.dtos.response;

        import io.swagger.v3.oas.annotations.media.Schema;

        import java.time.LocalDateTime;
        import java.util.UUID;

        @Schema(description = "DTO para resposta de cadastro de um Proprietário")
        public record CadastrarProprietarioResponseDTO(
                @Schema(description = "ID único do proprietário cadastrado")
                UUID id,

                @Schema(description = "Nome do proprietário")
                String nome,

                @Schema(description = "Email do proprietário")
                String email,

                @Schema(description = "Login do proprietário")
                String login,

                @Schema(description = "Data de criação do registro do proprietário")
                LocalDateTime dataCriacao,

                @Schema(description = "Mensagem de status da operação")
                String message,

                @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
                String status
        ) {
            // Construtor compacto opcional se precisar de lógica extra
        }
        ```

---

##### **CAMADA: Infrastructure - API - Presenters (Implementação dos Output Ports)**

Aqui formatamos a saída do Core para a apresentação.

14. **Criar a Implementação do Presenter: `CadastrarProprietarioPresenter.java`**
    * **Localização:** `infrastructure/api/presenters/CadastrarProprietarioPresenter.java`
    * **Propósito:** Formatar o resultado do cadastro de proprietário para o `CadastrarProprietarioResponseDTO`.
    * **Interação:** Implementa `CadastrarProprietarioOutputPort` e interage com `CadastrarProprietarioResponseDTO`.
    * **Código:**
        ```java
        // infrastructure/api/presenters/CadastrarProprietarioPresenter.java
        package com.postechfiap.meumenu.infrastructure.api.presenters;

        import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
        import com.postechfiap.meumenu.core.domain.presenters.CadastrarProprietarioOutputPort;
        import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarProprietarioResponseDTO;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import org.springframework.stereotype.Component;

        @Component // Marca a classe como um Bean do Spring
        @Getter // Para o getViewModel()
        @Setter // Para o Spring poder setar o viewModel, se necessário
        @NoArgsConstructor // Para o Spring poder instanciar
        public class CadastrarProprietarioPresenter implements CadastrarProprietarioOutputPort {

            private CadastrarProprietarioResponseDTO viewModel; // O ViewModel que será retornado ao Controller

            @Override
            public void presentSuccess(ProprietarioDomain proprietario) {
                // Cria o DTO de resposta de cadastro bem-sucedido
                this.viewModel = new CadastrarProprietarioResponseDTO(
                        proprietario.getId(),
                        proprietario.getNome(),
                        proprietario.getEmail(),
                        proprietario.getLogin(),
                        proprietario.getDataCriacao(),
                        "Proprietário cadastrado com sucesso!",
                        "SUCCESS"
                );
            }

            @Override
            public void presentError(String message) {
                // Preenche o ViewModel com a mensagem de erro e status FAIL
                this.viewModel = new CadastrarProprietarioResponseDTO(
                        null, null, null, null, null, // Campos nulos para erro
                        message,
                        "FAIL"
                );
            }

            // Método que o Controller vai chamar para obter o ViewModel
            public CadastrarProprietarioResponseDTO getViewModel() {
                return viewModel;
            }
        }
        ```

---

##### **CAMADA: Infrastructure - API - Controllers (Pontos de Entrada da API)**

Aqui expomos a funcionalidade via HTTP.

15. **Criar o REST Controller: `ProprietarioController.java`**
    * **Localização:** `infrastructure/api/controllers/ProprietarioController.java`
    * **Propósito:** Expor o endpoint HTTP para o cadastro de proprietários.
    * **Interação:**
        * Injeta `CadastrarProprietarioInputPort` (Core).
        * Injeta `CadastrarProprietarioPresenter` (Infraestrutura).
        * Define o `@PostMapping`.
        * **Não** contém `try-catch` para exceções de negócio, pois o `GlobalExceptionHandler` (Infraestrutura) se encarregará delas.
    * **Código:**
        ```java
        // infrastructure/api/controllers/ProprietarioController.java
        package com.postechfiap.meumenu.infrastructure.api.controllers;

        import com.postechfiap.meumenu.core.controllers.CadastrarProprietarioInputPort;
        import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarProprietarioRequestDTO;
        import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarProprietarioResponseDTO;
        import com.postechfiap.meumenu.infrastructure.api.presenters.CadastrarProprietarioPresenter;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.tags.Tag;
        import jakarta.validation.Valid;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

        @RestController
        @Tag(name = "Proprietario Controller", description = "Operações relacionadas ao Proprietário")
        @RequestMapping("/proprietarios")
        @RequiredArgsConstructor
        public class ProprietarioController {

            private final CadastrarProprietarioInputPort cadastrarProprietarioInputPort;
            private final CadastrarProprietarioPresenter cadastrarProprietarioPresenter;

            @Operation(
                    summary = "Realiza o cadastro de um novo usuário do tipo Proprietário",
                    description = "Este endpoint cria um novo usuário do tipo Proprietário no sistema."
            )
            @PostMapping
            public ResponseEntity<CadastrarProprietarioResponseDTO> cadastrarProprietario(@RequestBody @Valid CadastrarProprietarioRequestDTO requestDTO) {
                // Converte o RequestDTO (da infra) para o InputModel (do core)
                cadastrarProprietarioInputPort.execute(requestDTO.toInputModel());

                // Obtém o ViewModel formatado do Presenter da infra
                CadastrarProprietarioResponseDTO responseDTO = cadastrarProprietarioPresenter.getViewModel();
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            }
        }
        ```
````