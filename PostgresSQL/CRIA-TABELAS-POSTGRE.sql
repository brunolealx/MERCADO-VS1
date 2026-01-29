-- 1. Criação dos tipos ENUM (Necessário para Usuários e Movimentações)
CREATE TYPE tipo_perfil AS ENUM ('ADMIN', 'OPERADOR');
CREATE TYPE tipo_movimentacao AS ENUM ('ENTRADA', 'SAIDA');

-- 2. TABELA_USUARIOS
CREATE TABLE TABELA_USUARIOS (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil tipo_perfil DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE
);

INSERT INTO TABELA_USUARIOS (nome, login, senha, perfil) 
VALUES ('Admin', 'admin', 'psw123', 'ADMIN');

-- 3. TABELA_FORNECEDORES
CREATE TABLE TABELA_FORNECEDORES (
    id BIGSERIAL PRIMARY KEY,
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
);
CREATE INDEX idx_fornecedor_razao ON TABELA_FORNECEDORES(razao_social);
CREATE INDEX idx_fornecedor_contato ON TABELA_FORNECEDORES(contato);
CREATE INDEX idx_fornecedor_cidade ON TABELA_FORNECEDORES(cidade);

-- 4. TABELA_PRODUTOS
CREATE TABLE TABELA_PRODUTOS (
    id BIGSERIAL PRIMARY KEY,
    codigo_barra VARCHAR(20) NOT NULL UNIQUE,
    descricao VARCHAR(50) NOT NULL,
    marca VARCHAR(50),
    atributos VARCHAR(50),
    unidade_medida VARCHAR(30),
    categoria VARCHAR(50),
    cod_grupo INTEGER, -- PostgreSQL não possui UNSIGNED, usamos INTEGER ou BIGINT
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
    loja VARCHAR(50) DEFAULT 'Sede'
);

-- 5. TABELA_CLIENTES (Pessoa Física)
CREATE TABLE TABELA_CLIENTES (
    id BIGSERIAL PRIMARY KEY,
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
);

-- 6. TABELA_CLIENTES_PJ
CREATE TABLE TABELA_CLIENTES_PJ (
    id BIGSERIAL PRIMARY KEY,
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
);

-- 7. TABELA_VENDAS
CREATE TABLE TABELA_VENDAS (
    id_venda BIGSERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL,
    total_venda DECIMAL(10,2) NOT NULL,
    metodo_pagamento VARCHAR(50),
    CONSTRAINT fk_usuario_venda FOREIGN KEY (id_usuario) REFERENCES TABELA_USUARIOS(id)
);

-- 8. TABELA_ITENS_VENDA
CREATE TABLE TABELA_ITENS_VENDA (
    id_item BIGSERIAL PRIMARY KEY,
    id_venda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venda_item FOREIGN KEY (id_venda) REFERENCES TABELA_VENDAS(id_venda) ON DELETE CASCADE,
    CONSTRAINT fk_produto_item FOREIGN KEY (id_produto) REFERENCES TABELA_PRODUTOS(id)
);

-- 9. TABELA_MOVIMENTACOES_ESTOQUE
CREATE TABLE TABELA_MOVIMENTACOES_ESTOQUE (
    id SERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL, 
    tipo tipo_movimentacao NOT NULL,
    quantidade DECIMAL(10,2) NOT NULL,
    motivo VARCHAR(255),
    id_usuario BIGINT,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_produto_mov FOREIGN KEY (id_produto) REFERENCES TABELA_PRODUTOS(id),
    CONSTRAINT fk_usuario_mov FOREIGN KEY (id_usuario) REFERENCES TABELA_USUARIOS(id)
);
