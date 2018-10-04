package com.br.minasfrango.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Cliente implements Serializable {
		
		private static final long serialVersionUID = -5835341501816588891L;
		
		private double id;
		
		private String nome;
		
		private String razaoSocial;
		
		private Localidade localidade;
		
		private String endereco;
		
		private String numero;
		
		private String bairro;
		
		private String cidade;
		
		private String cep;
		
		private String referencia;
		
		private String telefone;
		
		private String cpf;
}
