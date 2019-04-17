package com.br.minasfrango.data.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Unidade extends RealmObject implements Serializable {
		
		@PrimaryKey
		private String id;
		UnidadeProdutoID chavesUnidade;
		private String nome;
		private String unidadePadrao;
		
}

