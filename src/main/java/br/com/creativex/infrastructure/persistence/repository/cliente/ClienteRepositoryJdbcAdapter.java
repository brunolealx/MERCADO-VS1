// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Adapter que implementa o ClienteRepository utilizando JDBC via ClienteDAO.
 * 
 * Faz a ponte entre o Domínio e a Infraestrutura de persistência.
 * 
 * @author Peracio Dias
 * @version 2.1
 * @since 2026-05-26
 */
public class ClienteRepositoryJdbcAdapter implements ClienteRepository {

    private final ClienteDAO dao;

    public ClienteRepositoryJdbcAdapter(ClienteDAO dao) {
        this.dao = dao;
    }

    @Override
    public Cliente save(Cliente cliente) {
        try {
            if (cliente.getId() == null || cliente.getId() == 0) {
                dao.inserir(cliente);
            } else {
                dao.atualizar(cliente);
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Erro no banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            dao.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente findById(long id) {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente findByDocumento(String documento) {
        try {
            return dao.buscarPorDocumento(documento);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por documento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> findByNomeRazaoSocial(String nome) {
        try {
            return dao.buscarPorNomeRazaoSocial(nome);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes por nome: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> findByIdLimit(long idInicial, int limite) {
        try {
            return dao.listarPorIdLimite(idInicial, limite);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
