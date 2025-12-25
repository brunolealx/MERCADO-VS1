CREATE TABLE TABELA_CLIENTES (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf_cnpj VARCHAR(20),
    rg_ie VARCHAR(20),
    telefone VARCHAR(11),
    email VARCHAR(120),
    endereco VARCHAR(150),
    numero VARCHAR(10),
    bairro VARCHAR(60),
    cidade VARCHAR(60),
    uf CHAR(2),
    cep VARCHAR(8),
    limite_credito DECIMAL(10,2),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
