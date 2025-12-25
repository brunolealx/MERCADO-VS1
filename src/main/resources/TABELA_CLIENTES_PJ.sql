CREATE TABLE TABELA_CLIENTES_PJ (
    id BIGINT NOT NULL AUTO_INCREMENT,

    razao_social     VARCHAR(150) NOT NULL,
    nome_fantasia    VARCHAR(150),
    cnpj             VARCHAR(14) NOT NULL,
    ie               VARCHAR(30),
    telefone         VARCHAR(11),
    email            VARCHAR(150),

    endereco         VARCHAR(150),
    numero           VARCHAR(20),
    complemento      VARCHAR(20),
    bairro           VARCHAR(100),
    cidade           VARCHAR(100) NOT NULL,
    uf               CHAR(2),
    cep              VARCHAR(8),

    limite_credito   DECIMAL(12,2) DEFAULT 0.00,

    data_cadastro    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    UNIQUE KEY uk_fornecedor_cnpj (cnpj),
    INDEX idx_fornecedor_razao (razao_social),
    INDEX idx_fornecedor_cidade (cidade)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
