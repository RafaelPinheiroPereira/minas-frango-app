package com.br.minasfrango.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Localidade implements Serializable {
		
		
		private double id;
		
		private String nome;
		
		private Rota rota;
		
}
