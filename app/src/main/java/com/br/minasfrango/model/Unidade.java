package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Unidade extends RealmObject implements Serializable {
		
		@PrimaryKey
		private String id;
		UnidadeProdutoID chavesUnidade;
		private String nome;
}

