CREATE TABLE TABELA_USUARIOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, -- Em sistemas reais, use BCrypt para a senha
    perfil ENUM('ADMIN', 'OPERADOR') DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inserir um utilizador inicial para teste
INSERT INTO TABELA_USUARIOS (nome, login, senha, perfil) 
VALUES ('Admin', 'admin', 'psw123', 'ADMIN');
