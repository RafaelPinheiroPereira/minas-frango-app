package com.br.minasfrango.model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
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
