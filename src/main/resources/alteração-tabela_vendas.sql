ALTER TABLE tabela_vendas
ADD COLUMN IF NOT EXISTS id_caixa BIGINT;

ALTER TABLE tabela_vendas
ADD CONSTRAINT fk_venda_caixa
FOREIGN KEY (id_caixa)
REFERENCES tabela_caixas(id)
ON DELETE RESTRICT;