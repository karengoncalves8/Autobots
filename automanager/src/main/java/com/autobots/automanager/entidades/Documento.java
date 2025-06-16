package com.autobots.automanager.entidades;

import java.util.Date;

import com.autobots.automanager.enums.TipoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Documento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private TipoDocumento tipo;

	@Column(nullable = false)
	private Date dataEmissao;

	@Column(unique = true, nullable = false)
	private String numero;
	
}
