# Reserva de Salas

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de salas, usuários e reservas.

Projeto desenvolvido durante os estudos de **Java Backend na Alura**, com foco em boas práticas de desenvolvimento, organização em camadas, persistência de dados e testes automatizados.

## Tecnologias

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven
* PostgreSQL
* JUnit 5
* Mockito
* JaCoCo
* Git / GitHub

## Funcionalidades

* Cadastro, consulta, atualização e exclusão de salas
* Cadastro e consulta de usuários
* Criação de reservas
* Validação de conflitos de horário
* Validação de disponibilidade de salas
* Validação de conflitos de reservas por usuário
* Cancelamento de reservas
* Conclusão de reservas
* Atualização de reservas
* Consulta de reservas por sala e período
* Paginação de resultados
* Tratamento global de exceções
* Testes unitários dos serviços

## Arquitetura

O projeto utiliza uma arquitetura organizada em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Também são utilizados **DTOs**, **Records**, **Enums** e classes específicas para tratamento de exceções.

## Testes

Os testes unitários foram implementados utilizando:

* JUnit 5
* Mockito
* Spring Test

A cobertura dos testes é acompanhada através do **JaCoCo**.

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

Para gerar o relatório de cobertura:

```bash
./mvnw test jacoco:report
```

O relatório pode ser encontrado em:

```text
target/site/jacoco/index.html
```

## Como executar

### Pré-requisitos

* Java 21+
* Maven
* PostgreSQL

Configure as informações do banco de dados no `application.properties` ou `application.yml`.

Depois execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

## Objetivo

Este projeto tem como objetivo aplicar na prática conceitos de desenvolvimento backend com Java e Spring Boot, incluindo:

* Desenvolvimento de APIs REST
* JPA e persistência de dados
* Validações de regras de negócio
* Tratamento de exceções
* Testes automatizados
* Cobertura de testes
* Versionamento com Git e GitHub
* Organização e boas práticas de código
