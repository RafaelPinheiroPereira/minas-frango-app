package com.br.minasfrango.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.Data;

@Data
public class Produto extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String nome;
		
		private String unidade;
		
		private double quantidade;
}
