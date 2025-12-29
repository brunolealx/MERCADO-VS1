package com.creativex.model.usuario;

public class Usuario {

    private long id;         // BIGINT no MySQL
    private String nome;     // Nome completo do funcionário
    private String login;    // Usuário para entrar no sistema
    private String senha;    // Senha (armazenada em texto ou hash)
    private String perfil;   // 'ADMIN' ou 'OPERADOR'
    private boolean ativo;   // Se o usuário pode ou não logar

    // Construtor padrão
    public Usuario() {
    }

    // Construtor auxiliar para facilitar o login/sessão
    public Usuario(long id, String nome, String perfil) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
    }

    // Getters e Setters (Seguindo o padrão do seu projeto)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // Método útil para exibir o nome do usuário em JComboBox ou Logs
    @Override
    public String toString() {
        return this.nome + " (" + this.perfil + ")";
    }
}
