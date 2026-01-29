-- Tabela para registrar a venda (Cabeçalho)
CREATE TABLE IF NOT EXISTS TABELA_VENDAS (
    id_venda BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL, -- Referencia o seu campo 'id'
    total_venda DECIMAL(10,2) NOT NULL,
    metodo_pagamento VARCHAR(50),
    FOREIGN KEY (id_usuario) REFERENCES TABELA_USUARIOS(id) -- Corrigido para 'id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
