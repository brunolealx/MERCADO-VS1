# MERCADO-VS1

Sistema desktop de PDV (Swing + Java 21) com persistência JDBC em PostgreSQL.

## Funcionalidades
- Cadastro e manutenção de produtos
- Cadastro de clientes PF
- Cadastro de clientes PJ
- Cadastro de fornecedores
- Cadastro/autenticação de usuários
- Operação de caixa (finalização e cancelamento de venda)
- Consulta de vendas
- Controle de estoque via movimentações

## Arquitetura Atual (Clean Architecture)
O projeto foi reorganizado para separar regras de negócio, casos de uso, adapters e interface.

### Fluxo de dependência
`ui -> presentation -> application -> domain`

`infrastructure` implementa portas do `domain` e é montada pelo `config/AppFactory`.

### Estrutura de pacotes
# Estrutura do Projeto

## Application
- Caixa
- Cliente
- Cliente PJ
- Fornecedor
- Produto
- UseCase
  - Core
- Usuário

## Config
- Configurações gerais do sistema

## DB
- Banco de dados e scripts

## Domain

### Entity
- Cliente
- Cliente PJ
- Fornecedor
- Produto
- Usuário
- Venda

### Repository
- Interfaces de repositório

### Transaction
- Controle transacional

## Infrastructure

### Persistence

#### Repository
- Caixa
- Cliente
- Cliente PJ
- Fornecedor
- Produto
- Usuário

### Transaction
- Gerenciamento de transações

## Presentation

### Controller
- Controladores da aplicação

## UI
- Ajuda
- Cadastro de Usuário
- Caixas
- Cliente PJ
- Clientes
- Estoque
- Fornecedor
- Impressoras
- Listagens
- Login
- Produtos

## Util
- Utilitários e funções auxiliares
```

## Tecnologias
- Java 21
- Swing
- PostgreSQL
- JDBC
- Maven
- JUnit 5 / Mockito

## Requisitos
- JDK 21
- Maven 3.9+
- PostgreSQL ativo

## Banco de dados
Scripts SQL ficam em:

- `src/main/resources/CRIA-TABELAS-POSTGRE.sql`
- `src/main/resources/alteração-tabela_vendas.sql`
- `src/main/resources/CRIA-tabela_caixas.TXT`

A conexão JDBC está centralizada em:

- `src/main/java/br/com/creativex/db/Conexao.java`

## Como executar
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="br.com.creativex.Main"
```

Opcional:
```bash
mvn package
java -jar target/MERCADO-VS1-1.0-SNAPSHOT.jar
```

## Testes
```bash
mvn test
```
Observação: em alguns ambientes Linux/JDK 21 o Mockito pode exigir ajuste de mock maker/agent para execução dos testes.

## Autores

Bruno Leal e Peracio Dias
