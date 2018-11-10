package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Rota  extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private Funcionario funcionario;
		
		private String nome;
		
}
