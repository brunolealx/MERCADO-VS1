-- 1. tabela_USUARIOS (Alterado de tipo_perfil para VARCHAR)
CREATE TABLE tabela_usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) DEFAULT 'OPERADOR', -- Alterado para VARCHAR
    ativo BOOLEAN DEFAULT TRUE
);

INSERT INTO tabela_usuarios (nome, login, senha, perfil) 
VALUES ('Admin', 'admin', 'psw123', 'ADMIN');

-- 2. tabela_fornecedores (Sem alterações)
CREATE TABLE tabela_fornecedores (
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
CREATE INDEX idx_fornecedor_razao ON tabela_fornecedores(razao_social);
CREATE INDEX idx_fornecedor_contato ON tabela_fornecedores(contato);
CREATE INDEX idx_fornecedor_cidade ON tabela_fornecedores(cidade);

-- 3. tabela_PRODUTOS (Sem alterações)
CREATE TABLE tabela_produtos (
    id BIGSERIAL PRIMARY KEY,
    codigo_barra VARCHAR(20) NOT NULL UNIQUE,
    descricao VARCHAR(50) NOT NULL,
    marca VARCHAR(50),
    atributos VARCHAR(50),
    unidade_medida VARCHAR(30),
    categoria VARCHAR(50),
    cod_grupo INTEGER, 
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

-- 4. tabela_CLIENTES (Sem alterações)
CREATE TABLE tabela_clientes (
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

-- 5. tabela_CLIENTES_PJ (Sem alterações)
CREATE TABLE tabela_clientes_pj (
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

-- 6. tabela_VENDAS (Sem alterações)
CREATE TABLE tabela_vendas (
    id_venda BIGSERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL,
    total_venda DECIMAL(10,2) NOT NULL,
    metodo_pagamento VARCHAR(50),
    CONSTRAINT fk_usuario_venda FOREIGN KEY (id_usuario) REFERENCES tabela_USUARIOS(id)
);

-- 7. tabela_ITENS_VENDA
CREATE TABLE tabela_itens_venda (
    id_item BIGSERIAL PRIMARY KEY,
    id_venda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venda_item FOREIGN KEY (id_venda) REFERENCES tabela_VENDAS(id_venda) ON DELETE CASCADE,
    CONSTRAINT fk_produto_item FOREIGN KEY (id_produto) REFERENCES tabela_PRODUTOS(id)
);

-- 8. tabela_movimentacoes_estoque (Alterado tipo_movimentacao para VARCHAR)
CREATE TABLE tabela_movimentacoes_estoque (
    id SERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL, 
    tipo VARCHAR(20) NOT NULL, -- Alterado para VARCHAR
    quantidade DECIMAL(10,2) NOT NULL,
    motivo VARCHAR(255),
    id_usuario BIGINT,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_produto_mov FOREIGN KEY (id_produto) REFERENCES tabela_PRODUTOS(id),
    CONSTRAINT fk_usuario_mov FOREIGN KEY (id_usuario) REFERENCES tabela_USUARIOS(id)
);
