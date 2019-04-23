package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrecoID extends RealmObject implements Serializable {
	  @PrimaryKey
		private long id;
		
		private long idProduto;
		
		private String unidadeProduto;
		
		private double idCliente;
}
