package com.br.minasfrango.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Funcionario implements Serializable {
		
		private long id;
		
		private String senha;
		
		private String nome;
		
		
}
