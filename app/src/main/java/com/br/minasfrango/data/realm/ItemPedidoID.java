package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ItemPedidoID extends RealmObject implements Serializable {
		
		
		@PrimaryKey
		
		private long id;
		
		private double idVenda;
		
		private double idProduto;
		
		private String idUnidade;
	
		private Date dataVenda;
	
		private String vendaMae;
		
		private String tipoVenda;
	
		private double nucleoCodigo;
}
