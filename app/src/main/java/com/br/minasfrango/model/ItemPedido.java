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
public class ItemPedido extends RealmObject implements Serializable {
		@PrimaryKey
		private long id;
	    ItemPedidoID chavesItemPedido;
		private String descricao;
		private Double valorUnitario;
		private double valorTotal;
		private int quantidade;
		private int bicos;
		
		
}
