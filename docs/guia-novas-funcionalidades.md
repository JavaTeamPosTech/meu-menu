# Guia de Criação de Novas Funcionalidades na Clean Architecture

Este guia detalha o processo de criação de uma nova funcionalidade em nossa aplicação, explicando cada etapa, sua camada correspondente na Clean Architecture e como interage com as demais. O objetivo é garantir consistência, desacoplamento e aderência aos princípios de Responsabilidade Única (SRP) e Inversão de Dependência (DIP).

**Exemplo Prático:** Implementação da funcionalidade de **Deletar Cliente por ID**.

---

## 1. Visão Geral do Fluxo (`Deletar Cliente por ID`)

Quando uma requisição HTTP para "deletar cliente" chega à aplicação, ela passará por um fluxo bem definido entre as camadas:

1.  **Infraestrutura (Controller):** Ponto de entrada HTTP.
2.  **Infraestrutura (Request DTO):** O Controller desserializa a requisição para um `RequestDTO` (se houver um corpo necessário). Para a deleção por ID, o ID vem da URL.
3.  **Infraestrutura (Controller):** O Controller **converte os dados necessários (como o ID da URL) para um formato que o Input Port do Core entenda** (neste caso, apenas o `UUID` do ID).
4.  **Core (Input Port Interface):** Define o contrato para a operação de deleção (`execute(UUID id)`).
5.  **Core (Input Port Implementação):** Recebe a chamada do Controller e **chama o Use Case correspondente**, passando os dados.
6.  **Core (Use Case):** Contém a lógica de negócio para deletar o cliente:
    * Busca o cliente usando o `Gateway`.
    * Se o cliente não existir, lança uma `ResourceNotFoundException`.
    * Chama o `Gateway` para deletar o cliente.
    * Chama o `Output Port` para notificar o sucesso (opcional para deleção).
7.  **Core (Gateway Interface):** Define o contrato para a operação de dados (`deletarCliente(UUID id)`).
8.  **Infraestrutura (Gateway Implementação):** Implementa a interface `Gateway` usando o Spring Data JPA (`clienteSpringRepository.deleteById(id)`). Realiza a interação com o banco de dados.
9.  **Core (Output Port Interface - Opcional para Deleção):** Define o contrato para o Use Case notificar a camada de apresentação.
10. **Infraestrutura (Presenter Implementação - Opcional para Deleção):** Implementa o `Output Port` para formatar uma resposta (se necessário).
11. **Infraestrutura (Controller / Global Exception Handler):** Retorna a resposta HTTP adequada (`204 No Content` em caso de sucesso, `404 Not Found` se a `ResourceNotFoundException` for capturada).

---

## 2. Etapas Detalhadas de Implementação

Seguiremos um fluxo que percorre as camadas da arquitetura, construindo as interfaces e suas implementações nas camadas apropriadas.

---

### **CAMADA: Core - Domain - Entities**

*(As entidades de domínio já devem existir.)*

* `ClienteDomain.java`
* `UsuarioDomain.java`
* `EnderecoDomain.java`
* `ProprietarioDomain.java`

### **CAMADA: Core - Exceptions**

* `BusinessException.java`
* `ResourceNotFoundException.java`

### **CAMADA: Core - Domain - Use Cases**

1.  **Criar a Interface do Use Case: `DeletarClienteUseCase.java`**
    * **Localização:** `core/domain/usecases/cliente/DeletarClienteUseCase.java`
    * **Propósito:** Define o contrato da operação de deleção.
    * **Código:**
        ```java
        package com.postechfiap.meumenu.core.domain.usecases.cliente;

        import java.util.UUID;

        public interface DeletarClienteUseCase {
            void execute(UUID id);
        }
        ```

2.  **Criar a Implementação do Use Case: `DeletarClienteUseCaseImpl.java`**
    * **Localização:** `core/domain/usecases/cliente/impl/DeletarClienteUseCaseImpl.java`
    * **Propósito:** Contém a lógica de negócio para deletar um cliente.
    * **Código (sem anotações de framework no Core agora):**
        ```java
        package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

        import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
        import com.postechfiap.meumenu.core.domain.presenters.cliente.DeletarClienteOutputPort;
        import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
        import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
        import com.postechfiap.meumenu.core.gateways.ClienteGateway;
        import lombok.RequiredArgsConstructor;

        import java.util.Optional;
        import java.util.UUID;

        @RequiredArgsConstructor
        public class DeletarClienteUseCaseImpl implements DeletarClienteUseCase {

            private final ClienteGateway clienteGateway;
            private final DeletarClienteOutputPort deletarClienteOutputPort;

            @Override
            public void execute(UUID id) {
                Optional<ClienteDomain> clienteOptional = clienteGateway.buscarClientePorId(id);
                if (clienteOptional.isEmpty()) {
                    throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
                }
                clienteGateway.deletarCliente(id);
                deletarClienteOutputPort.presentSuccess("Cliente com ID " + id + " excluído com sucesso.");
            }
        }
        ```

### **CAMADA: Core - Domain - Presenters**

3.  **Criar a Interface do Output Port (Opcional):**
    * `core/domain/presenters/cliente/DeletarClienteOutputPort.java`

    * Código:

        ```java
        package com.postechfiap.meumenu.core.domain.presenters.cliente;

        public interface DeletarClienteOutputPort {
            void presentSuccess(String message);
        }
        ```

### **CAMADA: Core - Gateways**

4.  **Adicionar Método à Interface do Gateway:**
    * `core/gateways/ClienteGateway.java`

    * Código (apenas o novo método):

        ```java
        package com.postechfiap.meumenu.core.gateways;

        import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
        import java.util.List;
        import java.util.Optional;
        import java.util.UUID;

        public interface ClienteGateway {
            ClienteDomain cadastrarCliente(ClienteDomain clienteDomain);
            boolean existsByCpf(String cpf);
            Optional<ClienteDomain> buscarClientePorId(UUID id);
            List<ClienteDomain> buscarTodosClientes();
            void deletarCliente(UUID id);
        }
        ```

### **CAMADA: Core - Controllers (Input Ports)**

5.  **Criar a Interface do Input Port:**
    * `core/controllers/cliente/DeletarClienteInputPort.java`

    * Código:

        ```java
        package com.postechfiap.meumenu.core.controllers.cliente;

        import java.util.UUID;

        public interface DeletarClienteInputPort {
            void execute(UUID id);
        }
        ```

6.  **Criar a Implementação do Input Port:**
    * `core/controllers/impl/cliente/impl/DeletarClienteInputPortImpl.java`

    * Código (sem anotações de framework no Core agora):

        ```java
        package com.postechfiap.meumenu.core.controllers.impl.cliente.impl;

        import com.postechfiap.meumenu.core.controllers.cliente.DeletarClienteInputPort;
        import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
        import lombok.RequiredArgsConstructor;

        @RequiredArgsConstructor
        public class DeletarClienteInputPortImpl implements DeletarClienteInputPort {

            private final DeletarClienteUseCase deletarClienteUseCase;

            @Override
            public void execute(UUID id) {
                deletarClienteUseCase.execute(id);
            }
        }
        ```

### **CAMADA: Infrastructure - Data - Repositories**

7.  **Atualizar a Implementação do Gateway:**
    * `infrastructure/data/repositories/ClienteGatewayImpl.java`

    * Código (apenas o novo método):

        ```java
        package com.postechfiap.meumenu.infrastructure.data.repositories;

        import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
        import com.postechfiap.meumenu.core.gateways.ClienteGateway;
        import com.postechfiap.meumenu.infrastructure.data.datamappers.ClienteDataMapper;
        import com.postechfiap.meumenu.infrastructure.data.repositories.spring.ClienteSpringRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Component;

        import java.util.List;
        import java.util.Optional;
        import java.util.UUID;

        @Component
        @RequiredArgsConstructor
        public class ClienteGatewayImpl implements ClienteGateway {

            private final ClienteSpringRepository clienteSpringRepository;
            private final ClienteDataMapper clienteDataMapper;

            // ... outros métodos ...

            @Override
            public void deletarCliente(UUID id) {
                clienteSpringRepository.deleteById(id);
            }
        }
        ```

### **CAMADA: Infrastructure - API - Presenters**

8.  **Criar a Implementação do Presenter (Opcional):**
    * `infrastructure/api/presenters/cliente/DeletarClientePresenter.java`

    * Código:

        ```java
        package com.postechfiap.meumenu.infrastructure.api.presenters.cliente;

        import com.postechfiap.meumenu.core.domain.presenters.cliente.DeletarClienteOutputPort;
        import com.postechfiap.meumenu.infrastructure.api.dtos.response.MensagemResponseDTO;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import org.springframework.stereotype.Component;

        @Component
        @Getter
        @Setter
        @NoArgsConstructor
        public class DeletarClientePresenter implements DeletarClienteOutputPort {

            private MensagemResponseDTO viewModel;

            @Override
            public void presentSuccess(String message) {
                this.viewModel = new MensagemResponseDTO(message, "SUCCESS");
            }

            public MensagemResponseDTO getViewModel() {
                return viewModel;
            }
        }
        ````

### **CAMADA: Infrastructure - API - Controllers**

9.  **Atualizar o REST Controller:**
    * `infrastructure/api/controllers/ClienteController.java`

    * Código (apenas o novo método e injeções):

        ````java
        package com.postechfiap.meumenu.infrastructure.api.controllers;

        import com.postechfiap.meumenu.core.controllers.cliente.BuscarClientePorIdInputPort;
        import com.postechfiap.meumenu.core.controllers.cliente.BuscarTodosClientesInputPort;
        import com.postechfiap.meumenu.core.controllers.cliente.CadastrarClienteInputPort;
        import com.postechfiap.meumenu.core.controllers.cliente.DeletarClienteInputPort;
        import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
        import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarClienteRequestDTO;
        import com.postechfiap.meumenu.infrastructure.api.dtos.response.BuscarClientePorIdResponseDTO;
        import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarClienteResponseDTO;
        import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.BuscarClientePorIdPresenter;
        import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.BuscarTodosClientesPresenter;
        import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.CadastrarClientePresenter;
        import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.DeletarClientePresenter;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.tags.Tag;
        import jakarta.validation.Valid;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;
        import java.util.UUID;
        import java.util.stream.Collectors;

        @RestController
        @Tag(name = "Cliente Controller", description = "Operações relacionadas ao Cliente")
        @RequestMapping("/clientes")
        @RequiredArgsConstructor
        public class ClienteController {

            private final CadastrarClienteInputPort cadastrarClienteInputPort;
            private final BuscarClientePorIdInputPort buscarClientePorIdInputPort;
            private final BuscarTodosClientesInputPort buscarTodosClientesInputPort;
            private final DeletarClienteInputPort deletarClienteInputPort;
            private final CadastrarClientePresenter cadastrarClientePresenter;
            private final BuscarClientePorIdPresenter buscarClientePorIdPresenter;
            private final BuscarTodosClientesPresenter buscarTodosClientesPresenter;
            private final DeletarClientePresenter deletarClientePresenter;

            //  ...

            @DeleteMapping("/{id}")
            @Operation(summary = "Deleta um cliente por ID")
            public ResponseEntity<Void> deletarCliente(@PathVariable UUID id) {
                deletarClienteInputPort.execute(id);
                return ResponseEntity.noContent().build();
            }
        }
        ````