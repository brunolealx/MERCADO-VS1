# MERCADO-VS1 🛒  
Sistema de Gestão de Produtos — Java + Swing + MySQL

O **MERCADO-VS1** é uma aplicação desktop desenvolvida em **Java (Swing)**
 para gerenciamento de produtos de mercado.  
O sistema inicial permite **cadastrar, listar, atualizar produtos**, 
mantendo os dados em um banco **MySQL**.
Novas funcionalidades estão sendo implementadas gradualmente.

---

## 📌 Funcionalidades

- ✔️ Cadastro de produto  
- ✔️ Atualização de dados  
- ✔️ Pesquisa na tabela  
- ✔️ Interface moderna em Swing  
- ✔️ Arquitetura organizada (MVC simplificado)  
- ✔️ Conexão MySQL centralizada

---

## 🏗️ Estrutura do Projeto (Maven)
MERCADO-VS1
├──/src/main/resources/
│            └──img/
│            │  └──logo.png
│            │  └──logoinicial.jpeg
│            └──TABELA_PRODUTOS.sql
src/main/java/com/creativex/
├── db/
│ └── Conexao.java # Gerenciamento de conexão com o banco
├── model/
│    └── produto/
│        └── Produto.java # Classe de domínio
├── dao/
│     └──produto/
│     │  └── ProdutoDAO.java # CRUD completo
│     └──usuario/
│        └── UsuarioDAO.java
├── ui/
│   └──produtos/     # Interface gráfica (Swing)
│   │    └── ProdutoForm.java
│   └──ajuda/
│   │   └── AjudaForm.java
│   └──caixas/
│   │   └──CaixasForm.java
│   └──clientes/
│   │   └──ClientesForm.java
│   └──estoque/
│   │   └──EstoqueForm.java
│   └──fornecedores/
│   │   └──FornecedoresForm.java
│   └──impressoras/
│   │   └──ImpressorasForm.java
│   └──listagens/
│   │   └──listagensForm.java
│   └──login/
│      └──LoginForm.java
└── Main.java # Entrada da aplicação

---

## 🛢️ Banco de Dados

### 📌 Requisitos
- MySQL 5.7+ ou MariaDB
- Driver JDBC (MySQL Connector)


### 📌 Script para criar o banco:
1. Execute o script 
`Usar arquivo Mysql /MERCADO-VS1/src/main/resources/TABELA_PRODUTOS	sql`

### 📌 conexão com jdbc:
private static final String URL  = "jdbc:mysql://localhost:3306/BCO_DADOS_ESTOQUE";
private static final String USER = "root";
private static final String PASS = "root";


### 📌 Como executar no bash:
2. Clone o repositório:
git clone https://github.com/seu-usuario/MERCADO-VS1.git
cd MERCADO-VS1

3. Compile com Maven
mvn clean install

4. Execute:
mvn exec:java -Dexec.mainClass="com.creativex.Main"

ou se preferir

java -jar target/MERCADO-VS1.jar

🖥️ Tecnologias Utilizadas

Java 21

Swing

MySQL

JDBC

Maven

MVC modularizado


🧩 Arquitetura

O projeto segue uma arquitetura simples para facilitar manutenção:

Model

Representa os dados (ex: Produto).

DAO

Acesso ao banco e operações CRUD (ProdutoDAO).

UI

Interface visual construída com JFrame/JPanel (ProdutoForm).

DB

Conexão centralizada com o banco.

🛠️ Melhorias Futuras (roadmap)

📌 Adicionar login de usuário

📌 Suporte a imagens de produtos

📌 Filtragem avançada

📌 Migração para JavaFX

📌 Relatórios em PDF

📌 API REST (Spring Boot)

📚 Autor
Engenheiro de software: Bruno Leal
Projeto educacional e evolutivo para gestão de produtos.

🛠️
Colaboradores: Perácio Dias
