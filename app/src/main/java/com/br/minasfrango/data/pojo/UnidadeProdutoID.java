package com.br.minasfrango.data.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class UnidadeProdutoID  extends RealmObject implements Serializable {
		
		
		@PrimaryKey
		private long idProduto;
		private String idUnidade;
}
