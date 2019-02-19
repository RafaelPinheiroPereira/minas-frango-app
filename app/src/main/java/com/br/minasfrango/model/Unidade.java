package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unidade extends RealmObject implements Serializable {
		
		@PrimaryKey
		private String id;
		UnidadeProdutoID chavesUnidade;
		private String nome;
		private String unidadePadrao;
		
}

