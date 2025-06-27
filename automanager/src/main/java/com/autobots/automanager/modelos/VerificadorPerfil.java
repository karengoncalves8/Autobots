package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enums.PerfilUsuario;

public class VerificadorPerfil {
    public boolean hasPerfil(Usuario usuario, PerfilUsuario perfil) {
		return usuario.getPerfis().contains(perfil);
    }
}
