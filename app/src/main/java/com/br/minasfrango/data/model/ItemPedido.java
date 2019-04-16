package com.br.minasfrango.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
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
