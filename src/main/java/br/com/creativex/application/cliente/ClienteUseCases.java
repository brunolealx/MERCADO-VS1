// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
// creativex sistemas
package br.com.creativex.application.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;

import java.util.List;

/**
 * Casos de uso unificados para gerenciamento de clientes (PF e PJ).
 * 
 * Centraliza a lógica de negócio e orquestra chamadas ao repositório.
 * 
 * @author Peracio Dias
 * @version 2.0
 * @since 2026-05-26
 */
public class ClienteUseCases {

    private final ClienteRepository repo;

    public ClienteUseCases(ClienteRepository repo) {
        this.repo = repo;
    }

    public Cliente save(Cliente cliente) {
        return repo.save(cliente);
    }

    public Cliente findById(long id) {
        return repo.findById(id);
    }

    public Cliente findByDocumento(String documento) {
        return repo.findByDocumento(documento);
    }

    public List<Cliente> findByNomeRazaoSocial(String nome) {
        return repo.findByNomeRazaoSocial(nome);
    }

    public List<Cliente> listByIdLimit(long idInicial, int limite) {
        return repo.findByIdLimit(idInicial, limite);
    }
    
    public void deleteById(long id) {
        repo.deleteById(id);
    }
}
