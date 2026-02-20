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

CREATE INDEX idx_produto_cod_barra ON tabela_produtos(codigo_barra);

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

DROP TABLE IF EXISTS tabela_itens_venda CASCADE;
DROP TABLE IF EXISTS tabela_movimentacoes_estoque CASCADE;
DROP TABLE IF EXISTS tabela_vendas CASCADE;

-- ==========================================================
-- 6. VENDAS E PDV (ESTRUTURA PROFISSIONAL)
-- ==========================================================
CREATE TABLE tabela_vendas (
    id_venda BIGSERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL,
    id_cliente BIGINT,
    
    total_bruto DECIMAL(10,2) NOT NULL,
    total_desconto DECIMAL(10,2) DEFAULT 0.00,
    total_liquido DECIMAL(10,2) NOT NULL,
    
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    metodo_pagamento VARCHAR(50),
    valor_pago DECIMAL(10,2),
    troco DECIMAL(10,2),
    chave_nfe VARCHAR(44),

    CONSTRAINT fk_usuario_venda FOREIGN KEY (id_usuario) REFERENCES tabela_usuarios(id),
    CONSTRAINT fk_cliente_venda FOREIGN KEY (id_cliente) REFERENCES tabela_clientes(id)
);

-- ==========================================================
-- 7-ITENS DA VENDA
-- ==========================================================
CREATE TABLE tabela_itens_venda (
    id_item BIGSERIAL PRIMARY KEY,
    id_venda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    
    quantidade DECIMAL(10,3) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    desconto_item DECIMAL(10,2) DEFAULT 0.00,
    subtotal DECIMAL(10,2) NOT NULL,
    
    preco_custo_momento DECIMAL(10,2),
    cst_fiscal_momento VARCHAR(3),

    CONSTRAINT fk_venda_item FOREIGN KEY (id_venda)
        REFERENCES tabela_vendas(id_venda) ON DELETE CASCADE,
    CONSTRAINT fk_produto_item FOREIGN KEY (id_produto)
        REFERENCES tabela_produtos(id)
);

-- ==========================================================
-- 8-MOVIMENTAÇÕES DE ESTOQUE
-- ==========================================================
CREATE TABLE tabela_movimentacoes_estoque (
    id BIGSERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL,

    saldo_anterior DECIMAL(10,3),
    saldo_posterior DECIMAL(10,3),

    motivo VARCHAR(255),
    id_usuario BIGINT,
    id_venda_origem BIGINT,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto_mov FOREIGN KEY (id_produto) REFERENCES tabela_produtos(id),
    CONSTRAINT fk_usuario_mov FOREIGN KEY (id_usuario) REFERENCES tabela_usuarios(id),
    CONSTRAINT fk_venda_mov FOREIGN KEY (id_venda_origem) REFERENCES tabela_vendas(id_venda)
);

-- ==========================================================
-- ÍNDICES
-- ==========================================================
CREATE INDEX idx_venda_data ON tabela_vendas(data_venda);
CREATE INDEX idx_movimentacao_produto ON tabela_movimentacoes_estoque(id_produto);

