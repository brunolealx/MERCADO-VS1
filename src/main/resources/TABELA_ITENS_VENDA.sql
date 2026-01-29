-- Tabela para os itens de cada venda
CREATE TABLE IF NOT EXISTS TABELA_ITENS_VENDA (
    id_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_venda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES TABELA_VENDAS(id_venda) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES TABELA_PRODUTOS(id) -- Verifique se na sua tabela produtos o campo também é apenas 'id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
