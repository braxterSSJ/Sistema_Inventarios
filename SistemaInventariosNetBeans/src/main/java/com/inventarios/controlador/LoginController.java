package com.inventarios.controlador;

import com.inventarios.dao.UsuarioDAO;
import com.inventarios.modelo.Usuario;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String usuario, String password) {
        if (usuario == null || usuario.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        return usuarioDAO.validarLogin(usuario.trim(), password);
    }
}
