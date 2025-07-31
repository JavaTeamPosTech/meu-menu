# API para Gestão de Restaurantes: Projeto "Meu Menu"

Este projeto é uma API RESTful para gerenciar a operação de restaurantes, clientes e pedidos, seguindo uma arquitetura robusta e escalável.

---

## 1. Princípios Arquiteturais: Clean Architecture

O projeto "Meu Menu" foi cuidadosamente estruturado seguindo os princípios da Clean Architecture de Robert C. Martin (Uncle Bob). Isso garante um sistema desacoplado, testável, flexível e resiliente a mudanças tecnológicas.

### Pilar Central: A Regra da Dependência

A regra fundamental é que as dependências devem sempre apontar para dentro, das camadas externas para as internas.

### Estrutura de Camadas:

Nosso projeto está organizado em camadas concêntricas, onde a lógica de negócio central (o Core) é independente de frameworks e tecnologias:

- **Camada 1: Entidades de Domínio (`core/domain/entities`)**

    - Função: Contém as regras de negócio mais importantes e invariantes.
    - Classes como `ClienteDomain`, `ProprietarioDomain`,  `RestauranteDomain`, `AdminDomain` e os Enums de Domínio são POJOs puros, sem anotações de frameworks como Spring ou JPA.

- **Camada 2: Casos de Uso (`core/domain/usecases`)**

    - Função: Orquestra a lógica de negócio específica da aplicação.
    - Interfaces como `CadastrarRestauranteUseCase` e suas implementações `*UseCaseImpl` residem aqui.
    - Regra da Dependência: Esta camada depende apenas de Entidades de Domínio (camada 1) e de abstrações (Gateways e OutputPorts, também na camada 2).

- **Camada 3: Portas e Adaptadores**

    - Função: Esta é a camada de "adaptadores".
    - Ela converte dados do formato externo (HTTP, JSON) para o formato interno (Domínio) e vice-versa.
    - Portas (`core/controllers` e `core/domain/presenters`): Interfaces que definem os contratos de comunicação.
    - Adaptadores (`infrastructure`): As implementações concretas das portas, que dependem diretamente de frameworks externos.

---

## 2. Estrutura de Pastas e Componentes

A organização do projeto reflete a arquitetura limpa, com o pacote `core` contendo a lógica agnóstica e o pacote `infrastructure` os detalhes de implementação:

```plaintext
src/
└── main/
    └── java/
        └── com/postechfiap/meumenu/
            ├── core/                     # Camadas Internas: O Coração da Aplicação
            │   ├── controllers/          # Interfaces dos Input Ports
            │   │   ├── admin/
            │   │   │   └── *AdminInputPort.java
            │   │   ├── cliente/
            │   │   │   └── *ClienteInputPort.java
            │   │   └── ...
            │   ├── domain/
            │   │   ├── entities/         # Entidades de Domínio (POJOs)
            │   │   │   ├── AdminDomain.java
            │   │   │   ├── ClienteDomain.java
            │   │   │   └── ...
            │   │   ├── gateways/         # Interfaces dos Gateways
            │   │   │   ├── AdminGateway.java
            │   │   │   └── ...
            │   │   ├── presenters/       # Interfaces dos Output Ports
            │   │   │   ├── admin/
            │   │   │   │   └── *AdminOutputPort.java
            │   │   │   └── ...
            │   │   ├── services/         # Interfaces de Serviços de Domínio
            │   │   └── usecases/         # Implementações dos Casos de Uso (POJOs)
            │   │       ├── admin/impl/
            │   │       │   └── *AdminUseCaseImpl.java
            │   │       └── cliente/impl/
            │   │           └── *ClienteUseCaseImpl.java
            │   └── ...
            └── infrastructure/           # Camada Externa: Os Adaptadores e Detalhes
                ├── api/
                │   ├── controllers/        # Adaptadores da API (Controladores REST)
                │   │   ├── AdminController.java
                │   │   ├── ClienteController.java
                │   │   └── ...
                │   ├── dtos/               # DTOs de Requisição e Resposta (Swagger, Validation)
                │   │   ├── request/
                │   │   └── response/
                │   └── presenters/         # Implementações dos Output Ports
                │       ├── admin/
                │       │   └── *AdminPresenter.java
                │       └── ...
                ├── config/                 # Raiz de Composição e Configurações Spring
                │   ├── ..Config.java      # Classe que declara todos os @Bean
                │   └── AdminSeeder.java    # CommandLineRunner para dados iniciais
                ├── data/                   # Adaptadores de Persistência
                │   ├── datamappers/        # Mapeamento Domain <-> Entity
                │   │   └── impl/
                │   │       └── ... ClienteDataMapperImpl.java
                │   ├── model/              # Entidades JPA (@Entity)
                │   │   ├── AdminEntity.java
                │   │   └── ClienteEntity.java
                │   └── repositories/       # Implementações dos Gateways e Repositórios JPA
                │       ├── AdminGatewayImpl.java
                │       └── ...
                └── security/               # Configurações de Segurança
```


---

## 3. Principais Alterações e Vantagens da Nova Arquitetura

- **Agnosticismo do Core (Refatoração Crucial):**  
  Removemos todas as anotações Spring (`@Service`, `@Component`, `@Repository`) das implementações de Use Cases, Input Ports, Data Mappers e Presenters.  
  Essas classes agora são POJOs puros, e a responsabilidade de instanciá-las e injetar suas dependências foi transferida para a classe `@Configuration` (`InputPortsConfig.java e UseCaseConfigs.java`).  
  Isso resolveu o problema de dependência circular e acoplamento.

- **Implementação do Perfil de Administrador (Admin):**
    - Criamos as entidades `AdminDomain` e `AdminEntity`, que estendem a base `Usuario`.
    - A role de segurança é inferida automaticamente (`ROLE_ADMINENTITY`) e usada para restringir o acesso a um novo `AdminController.java`.
    - O `AdminController` atua como um portão de segurança para operações privilegiadas (listar todos, deletar outros usuários/restaurantes).
    - Um `AdminSeeder.java` foi implementado para criar o primeiro usuário admin na inicialização da aplicação, usando a `CommandLineRunner`.

- **Modelagem de Domínio Rica:**  
  O domínio `Restaurante` foi expandido para incluir relacionamentos complexos com `Proprietario`, `EnderecoRestaurante`, `HorarioFuncionamento`, `TipoCozinha` e `ItemCardapio`.

---

## 4. Testes e Qualidade de Código

- **Testes Unitários (JaCoCo):**  
  A lógica de negócio principal (Use Cases) e os adaptadores de mapeamento (DataMappers, Presenters) têm cobertura de código acima de 80%,  garantindo a correção de cada unidade isolada.

- **Testes de Integração (Testcontainers):**
    - Utilizamos Testcontainers para executar um banco de dados PostgreSQL real em um contêiner Docker para testes de integração.
    - A anotação `@Transactional` em nossos testes garante que o banco de dados seja limpo (via rollback) após cada método de teste, assegurando o isolamento completo.
    - A propriedade `spring.jpa.hibernate.ddl-auto=create-drop` garante que o esquema do banco de dados seja recriado do zero para cada classe de teste, evitando problemas de dados remanescentes.
