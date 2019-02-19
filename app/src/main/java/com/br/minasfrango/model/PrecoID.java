package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

@Data
public class PrecoID extends RealmObject implements Serializable {
	  @PrimaryKey
		private long id;
		
		private long idProduto;
		
		private String unidadeProduto;
		
		private double idCliente;
}
