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
- ✔️ Cadastro e gerenciamento de fornecedores
- ✔️ Cadastro e gerenciamento de Clientes Pessoa física
- ✔️ Cadastro e gerenciamento de de Clientes Pessoa jurídica
- ✔️ Cadastro e gerenciamento de usuarios
- ✔️ Controle de estoque 
- ✔️ Gerenciamento de Caixas
- ✔️ Relatórios
- ✔️ Configuração de impressoaras
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
│            ├────TABELA_PRODUTOS.sql
│            ├────TABELA_CLIENTES.sql
│            ├────TABELA_CLIENTES_PJ.sql
│            ├────TABELA_FORNECEDORES.sql
│            ├────TABELA_MOVIMENTACOES_ESTOQUE.sql
│            ├────TABELA_TABELA-USUARIOS.sql
│      

src/main/java/com/creativex/
├── db/
│ └── Conexao.java # Gerenciamento de conexão com o banco
├── model/
│    └── produto/
│    │   └── Produto.java # Classe de domínio
│    └──cliente/
│    │   └──Cliente.java
│    └──clientepj/
│    │   └──Clientepj.java
│    └──fornecedor/
│    │   └──Fornecedor.java
│    └──usuario/
│        └──Usuario.java
├── dao/
│     └──produto/
│     │  └── ProdutoDAO.java # CRUD completo
│     └──usuario/
│     │  └── UsuarioDAO.java
│     └──clientes/
│     │   └──ClientesDAO.java
│     └──clientepj/
│     │   └──ClientespjDAO.java
│     └──fornecedor/
│         └──FornecedorDAO.java 
├── ui/
│   │ 
│   HomeScreen.java
│   │ 
│   MainWIndow.java
│   └──produtos/     # Interface gráfica (Swing)
│   │   └── ProdutoForm.java
│   └──ajuda/
│   │   └── AjudaForm.java
│   └──caixas/
│   │   └──CaixasForm.java
│   └──clientes/
│   │   └──ClientesForm.java
│   └──clientepj/
│   │   └──ClientespjForm.java
│   └──estoque/
│   │   └──EstoqueForm.java
│   └──fornecedores/
│   │   └──FornecedoresForm.java
│   └──impressoras/
│   │   └──ImpressorasForm.java
│   └──listagens/
│   │   └──listagensForm.java
│   └──login/
│         ├──LoginForm.java
├── util/
│	 └──Sessão
│
└── Main.java # Entrada da aplicação

## 🛢️ Banco de Dados

### 📌 Requisitos
- MySQL 5.7+ ou MariaDB
- Driver JDBC (MySQL Connector)

### 📌 Script para criar o banco:
-- 1. CRIAR O BANCO DE DADOS
CREATE DATABASE IF NOT EXISTS BCO_DADOS_MERCADO;
USE BCO_DADOS_MERCADO;

-- 2. TABELA DE USUÁRIOS (Referência para movimentações e auditoria)
CREATE TABLE IF NOT EXISTS TABELA_USUARIOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'OPERADOR') DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. TABELA DE PRODUTOS
CREATE TABLE IF NOT EXISTS TABELA_PRODUTOS (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_barra VARCHAR(20) NOT NULL,
    descricao VARCHAR(50) NOT NULL,
    marca VARCHAR(50),
    atributos VARCHAR(50),
    unidade_medida VARCHAR(30),
    categoria VARCHAR(50),
    cod_grupo SMALLINT UNSIGNED,
    grupo VARCHAR(80),
    tipo_balanca CHAR(1) CHECK (tipo_balanca IN ('B', 'C', 'N')),
    quantidade_estoque DECIMAL(10, 3) NOT NULL DEFAULT 0.000,
    preco_custo DECIMAL(10, 2),
    preco_venda DECIMAL(10, 2) NOT NULL,
    ncm VARCHAR(10),
    cest VARCHAR(7),
    cfop_padrao VARCHAR(4),
    unidade_tributavel VARCHAR(10),
    cean_tributavel VARCHAR(20),
    cst_icms VARCHAR(3),
    aliquota_icms DECIMAL(5, 2) DEFAULT 0.00,
    cst_pis VARCHAR(2),
    ppis DECIMAL(5, 2) DEFAULT 0.00,
    cst_cofins VARCHAR(2),
    pcofins DECIMAL(5, 2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    loja VARCHAR(50) DEFAULT 'Sede',
    UNIQUE INDEX idx_codigo_barra (codigo_barra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. TABELA DE MOVIMENTAÇÕES DE ESTOQUE (Depende de Produtos e Usuários)
CREATE TABLE IF NOT EXISTS TABELA_MOVIMENTACOES_ESTOQUE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    -- Alterado de INT para BIGINT para ser compatível com TABELA_PRODUTOS
    id_produto BIGINT NOT NULL, 
    tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
    quantidade DECIMAL(10,2) NOT NULL,
    motivo VARCHAR(255),
    id_usuario BIGINT,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_produto_mov FOREIGN KEY (id_produto) REFERENCES TABELA_PRODUTOS(id),
    CONSTRAINT fk_usuario_mov FOREIGN KEY (id_usuario) REFERENCES TABELA_USUARIOS(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. TABELA DE CLIENTES (Pessoa Física)
CREATE TABLE IF NOT EXISTS TABELA_CLIENTES (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    rg VARCHAR(20),
    telefone VARCHAR(15),
    email VARCHAR(100),
    endereco VARCHAR(150),
    numero VARCHAR(10),
    bairro VARCHAR(50),
    cidade VARCHAR(50) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(8),
    limite_credito DECIMAL(10, 2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. TABELA DE CLIENTES PJ (Empresas)
CREATE TABLE IF NOT EXISTS TABELA_CLIENTES_PJ (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    ie VARCHAR(30),
    telefone VARCHAR(11),
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(8),
    limite_credito DECIMAL(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. TABELA DE FORNECEDORES
CREATE TABLE IF NOT EXISTS TABELA_FORNECEDORES (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    ie VARCHAR(30),
    contato VARCHAR(120) NOT NULL,
    telefone VARCHAR(11),
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(8),
    limite_credito DECIMAL(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. DADOS INICIAIS (Opcional - para você conseguir logar pela primeira vez)
INSERT INTO TABELA_USUARIOS (nome, login, senha, perfil) 
VALUES ('Administrador', 'admin', 'psw123', 'ADMIN');


1. Execute o script 
`Usar arquivo Mysql /MERCADO-VS1/src/main/resources/TABELA_PRODUTOS	sql`

### 📌 conexão com jdbc:
private static final String URL  = "jdbc:mysql://localhost:3306/BCO_DADOS_MERCADO";
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
