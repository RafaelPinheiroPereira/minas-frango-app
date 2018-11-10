package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
public class TipoRecebimento extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		private String nome;
}
