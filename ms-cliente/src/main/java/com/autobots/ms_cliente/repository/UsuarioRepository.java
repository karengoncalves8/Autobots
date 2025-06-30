package com.autobots.ms_cliente.repository;

import com.autobots.ms_cliente.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEmpresaId(Long empresaId);
    List<Usuario> findByEmpresaIdAndPerfisContaining(Long empresaId, String perfil);
} 