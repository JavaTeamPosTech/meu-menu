# MeuMenu 🍽️

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

Um sistema de gerenciamento de menus e pedidos para restaurantes, desenvolvido com **Java**, **Spring Boot**, **PostgreSQL** e **Docker**.

---

## 📋 Funcionalidades

- **Gerenciamento de Clientes**: Cadastro, edição e exclusão de clientes.
- **Gerenciamento de Pedidos**: Criação e acompanhamento de pedidos em tempo real.
- **Integração com Banco de Dados**: Persistência de dados utilizando PostgreSQL.
- **APIs RESTful**: Endpoints para interação com o sistema.
- **Containerização**: Configuração com Docker para fácil replicação do ambiente.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**: Linguagem principal do projeto.
- **Spring Boot**: Framework para desenvolvimento rápido e eficiente.
- **PostgreSQL**: Banco de dados relacional.
- **Docker**: Containerização para padronização do ambiente.
- **Maven**: Gerenciamento de dependências e build.

---

## 🛠️ Configuração do Ambiente

### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven](https://maven.apache.org/)

### Passos para Configuração

1. Clone o repositório:
   ```bash
   git clone https://github.com/JavaTeamPosTech/meu-menu.git
   cd meu-menu
   ```
2. Configure o arquivo .env:

- POSTGRES_DB=meuMenu
- POSTGRES_USER=postgres
- POSTGRES_PASSWORD=postgres
- POSTGRES_PORT=5432
- POSTGRES_HOST=localhost
- SERVER_PORT=8080
- JWT_SECRET_KEY="minhaChaveUltraSecretaComMaisDe32Bytes"
- JWT_EXPIRATION_TIME=3600000
- DEFAULT_ADMIN_LOGIN = "admin"
- DEFAULT_ADMIN_EMAIL = "admin@meumenu.com"
- DEFAULT_ADMIN_PASSWORD = "SenhaForte123!"
- SPRING_PROFILES_ACTIVE=dev

3. Inicie os contêineres com Docker Compose:
   ```bash
   docker-compose up --build
   ```
4. Acesse a aplicação:

- API: http://localhost:8080
- Banco de Dados (via DBeaver ou outro clienteDomain): jdbc:postgresql://localhost:5432/meuMenu

## 📂 Estrutura do projeto

```plaintext
src/
└── main/
    └── java/
        └── com/postechfiap/meumenu/
            ├── core/                     # Camadas Internas: O Coração da Aplicação (Framework-Agnóstica)
            │   ├── controllers/          # Interfaces dos Input Ports (Portas de Entrada)
            │   ├── domain/
            │   │   ├── entities/         # Entidades de Domínio (POJOs puros)
            │   │   ├── gateways/         # Interfaces dos Gateways (Portas de Saída para o DB)
            │   │   ├── presenters/       # Interfaces dos Output Ports (Portas de Saída para a API)
            │   │   ├── services/         # Interfaces de Serviços de Domínio
            │   │   └── usecases/         # Implementações dos Casos de Uso
            │   └── dtos/                 # DTOs puros para comunicação interna (Input Models)
            │   └── exceptions/           # Exceções de Domínio
            └── infrastructure/           # Camada Externa: Os Adaptadores e Detalhes de Implementação
                ├── api/
                │   ├── controllers/      # Adaptadores da API (Controladores REST)
                │   ├── dtos/             # DTOs de Requisição e Resposta (Swagger, Validação)
                │   └── presenters/       # Implementações dos Output Ports
                ├── config/               # Raiz de Composição (Gerenciamento de Beans)
                ├── data/
                │   ├── datamappers/      # Adaptadores de Mapeamento Domain <-> Entity
                │   ├── model/            # Entidades JPA (@Entity)
                │   └── repositories/     # Adaptadores de Persistência (Gateways Impl)
                └── security/             # Adaptadores de Segurança
```

## 🧪 Testes

Para executar os testes unitários, utilize o comando:

```bash
 mvn test
```

## 📖 Documentação da API

Acesse a documentação da API gerada automaticamente pelo Swagger em:

http://localhost:8080/swagger-ui.html

## 🐳 Docker Subir os Contêineres

```bash
docker-compose up --build
```

## 👥 Contribuidores

- Rafael Caxixi Fuzeti
- Gustavo Soares Bomfim
- Francisco Aguiar Barreto de Souza Lima
- Fernanda de Oliveira Ferreira

# 📦 Comandos úteis para projetos Java com Maven e Jacoco

Este documento reúne comandos Maven importantes para facilitar o desenvolvimento, execução de testes e geração de relatórios de cobertura de testes com Jacoco.

---

## ✅ Comandos disponíveis

### 🔍 Gerar relatório Jacoco

Executa a limpeza, os testes e gera o relatório de cobertura de testes com Jacoco.

```bash
mvn clean test jacoco:report

```
