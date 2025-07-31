# Fluxo da Requisição de Cadastro de Cliente (POST /clientes) na Clean Architecture

---

## 1. A Requisição Chega (Camada de Infraestrutura: Adaptadores da API)

- A requisição HTTP (`POST /clientes`) chega ao **Controller** (`ClienteController.java`).
- O Controller atua como ponto de entrada da API.
- Ele recebe o corpo da requisição e o desserializa para um objeto `CadastrarClienteRequestDTO`, que contém os dados brutos.

---

## 2. Tradução e Delegação (Camada de Infraestrutura → Camada Core)

- Antes de entrar no Core, o Controller faz a tradução.
- Chama o método `toInputModel()` no `RequestDTO`, convertendo os dados em um `CadastrarClienteInputModel`.
- Em seguida, o Controller delega a execução para o **InputPort** (`CadastrarClienteInputPortImpl.java`), passando o InputModel já traduzido.
- O InputPort age como o portão de entrada do Core.

---

## 3. Execução da Lógica de Negócio (Camada Core: Use Cases)

- O InputPortImpl passa o InputModel para o **UseCase** (`CadastrarClienteUseCaseImpl.java`).
- O UseCase contém todas as regras de negócio.
- Ele valida a unicidade dos dados (login, email, CPF) usando a interface `ClienteGateway`.
- Criptografa a senha e cria uma entidade de domínio pura, o `ClienteDomain`.
- Para persistir os dados, chama o método `cadastrarCliente` da interface `ClienteGateway` (sem se preocupar com a implementação JPA).

---

## 4. Preparação da Resposta (Camada Core → Adaptador da Camada infrastructure)

- Após o Gateway retornar o `ClienteDomain` salvo, o UseCase chama o método `presentSuccess` da interface `CadastrarClienteOutputPort` (uma porta de saída).
- O **Presenter** (`CadastrarClientePresenter.java`) é o adaptador que implementa a porta de saída.
- Ele recebe o `ClienteDomain` do Core e o mapeia para um `CadastrarClienteResponseDTO`.

---

## 5. Envio da Resposta (Camada de Infraestrutura: Controladores)

- O Controller recupera o `ResponseDTO` já preparado pelo Presenter (através do método `getViewModel()`).
- O Controller encapsula o `ResponseDTO` em um `ResponseEntity` com o código de status HTTP correto (`HttpStatus.CREATED`).
- Finalmente, envia a resposta de volta ao cliente.
