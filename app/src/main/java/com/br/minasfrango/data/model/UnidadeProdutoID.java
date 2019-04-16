package com.br.minasfrango.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.Data;

@Data
public class UnidadeProdutoID  extends RealmObject implements Serializable {
		
		
		@PrimaryKey
		private long idProduto;
		private String idUnidade;
}
