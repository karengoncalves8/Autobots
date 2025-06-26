package com.autobots.automanager.servicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServico {
    @Autowired
    UsuarioRepositorio repositorio;

    public Map<String, Object> getUsuarioByUsername(String userName) {
		List<Usuario> users = repositorio.findAll();
		Usuario target = null;
        Credencial credencialSenha = null;
		for (Usuario user : users) {
			if (user.getCredenciais() != null) {
                List<Credencial> credencias = new ArrayList<>(user.getCredenciais());
                for(Credencial credencial : credencias){
                    if(credencial instanceof CredencialSenha){
                        if (((CredencialSenha) credencial).getNomeUsuario().equals(userName)) {
                            target = user;
                            credencialSenha = credencial;
                            break;
                        }
                    }
                }
			}
		}

        Map<String, Object> result = new HashMap<>();
        result.put("usuario", target);
        result.put("credencial", credencialSenha);

        return result;
	}

    public Usuario getUsuarioByCodigoBarra(Long codigo){
        List<Usuario> users = repositorio.findAll();
		Usuario target = null;
		for (Usuario user : users) {
			if (user.getCredenciais() != null) {
                for (Credencial credencial : user.getCredenciais()) {
                    if (credencial instanceof CredencialCodigoBarra) {
                        CredencialCodigoBarra ccb = (CredencialCodigoBarra) credencial;
                        if (ccb.getCodigo() == codigo) {
                            target = user; 
                        }
                    }
                }
			}
		}
		return target;
    }
}
