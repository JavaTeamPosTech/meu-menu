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

