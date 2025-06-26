package com.autobots.automanager.entidades;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import lombok.Data;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "tipoCredencial"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CredencialSenha.class, name = "senha"),
  @JsonSubTypes.Type(value = CredencialCodigoBarra.class, name = "codigoBarra")
})
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Credencial {
    
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Date criacao;

	@Column()
	private Date ultimoAcesso;

	@Column(nullable = false)
	private boolean inativo;
	
}
