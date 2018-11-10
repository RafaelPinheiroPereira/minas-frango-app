package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Funcionario extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String senha;
		
		private String nome;
		
		private String tipoFuncionario;
		
		
		
		
}
