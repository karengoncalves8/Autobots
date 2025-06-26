package com.autobots.automanager.servicos;

import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialSenha;

@Service
public class CredencialServico {
    
    public boolean isPasswordCredentialValid(CredencialSenha credencial){
        boolean validation = false;
		if (credencial != null) {
			if ((credencial.getNomeUsuario() != null) && (credencial.getSenha() != null)) {
				if (!credencial.getNomeUsuario().isBlank() && !credencial.getSenha().isBlank()) {
					validation = true;
				}
			}
		}
		return validation;
    }

     public boolean isBarCodeCredentialValid(CredencialCodigoBarra credencial){
        boolean validation = false;
		if (credencial != null) {
			if (credencial.getCodigo() != 0L) {
				validation = true;
			}
		}
		return validation;
    }
}
