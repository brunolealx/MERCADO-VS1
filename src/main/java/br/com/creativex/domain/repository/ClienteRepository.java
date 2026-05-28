// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.cliente.Cliente;
import java.util.List;

/**
 * Interface que define contrato para operações de persistência unificada de clientes.
 * 
 * Abstrai a implementação específica de banco de dados para a tabela unificada.
 * 
 * @author Peracio Dias
 * @version 2.0
 * @since 2026-05-26
 */
public interface ClienteRepository {
    
    /**
     * Salva ou atualiza um cliente.
     * @param cliente Cliente a ser salvo
     * @return Cliente salvo com ID preenchido
     */
    Cliente save(Cliente cliente);
    
    /**
     * Remove um cliente pelo ID.
     * @param id Identificador do cliente
     */
    void deleteById(long id);
    
    /**
     * Busca um cliente pelo ID.
     * @param id Identificador do cliente
     * @return Cliente encontrado ou null
     */
    Cliente findById(long id);
    
    /**
     * Busca um cliente pelo documento (CPF ou CNPJ).
     * @param documento CPF ou CNPJ
     * @return Cliente encontrado ou null
     */
    Cliente findByDocumento(String documento);
    
    /**
     * Busca clientes pelo nome ou razão social (busca parcial).
     * @param nomeRazaoSocial Nome ou Razão Social
     * @return Lista de clientes que atendem ao critério
     */
    List<Cliente> findByNomeRazaoSocial(String nomeRazaoSocial);
    
    /**
     * Lista clientes com paginação baseada no ID.
     * @param idInicial ID inicial para a listagem
     * @param limite Quantidade máxima de registros
     * @return Lista de clientes
     */
    List<Cliente> findByIdLimit(long idInicial, int limite);
}
