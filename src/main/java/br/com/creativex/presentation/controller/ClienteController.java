// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.cliente.ClienteUseCases;
import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;

import java.util.List;

/**
 * Controller unificado para gerenciamento de clientes.
 * 
 * Atua como intermediário entre a UI e os Casos de Uso.
 * 
 * @author Peracio Dias
 * @version 2.0
 * @since 2026-05-26
 */
public class ClienteController {

    private final ClienteUseCases useCases;

    public ClienteController(ClienteRepository repository) {
        this.useCases = new ClienteUseCases(repository);
    }

    public Cliente save(Cliente c) { return useCases.save(c); }
    public Cliente findById(long id) { return useCases.findById(id); }
    public Cliente findByDocumento(String documento) { return useCases.findByDocumento(documento); }
    public List<Cliente> findByNomeRazaoSocial(String nome) { return useCases.findByNomeRazaoSocial(nome); }
    public List<Cliente> listByIdLimit(long idInicial, int limite) { return useCases.listByIdLimit(idInicial, limite); }
    public void deleteById(long id) { useCases.deleteById(id); }
}
