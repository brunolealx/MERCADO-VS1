CREATE TABLE TABELA_PRODUTOS (
	id INT AUTO_INCREMENT PRIMARY KEY, 
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
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
