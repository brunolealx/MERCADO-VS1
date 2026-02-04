package com.creativex.util;

import com.creativex.model.usuario.Usuario;

public class Sessao {

    private static Usuario usuarioLogado;

    private Sessao() {
        // impede instanciação
    }

    public static void login(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }

    public static void logout() {
        usuarioLogado = null;
    }
}
