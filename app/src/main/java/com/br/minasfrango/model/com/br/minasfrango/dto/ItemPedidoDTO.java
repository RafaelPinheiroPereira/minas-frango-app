package com.br.minasfrango.model.com.br.minasfrango.dto;

import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.ItemPedidoID;
import com.br.minasfrango.model.Pedido;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO  implements Serializable {
		private long id;
		ItemPedidoIDDTO chavesItemPedido;
		private String descricao;
		private Double valorUnitario;
		private double valorTotal;
		private double quantidade;
		
		public static ItemPedidoDTO transformaEmItemPedidoDTO(ItemPedido itemPedido){
				return new ItemPedidoDTO(itemPedido.getId(),ItemPedidoIDDTO.transformaEmItemPedidoIDDTO(itemPedido.getChavesItemPedido()),itemPedido.getDescricao(),itemPedido.getValorUnitario(),itemPedido.getValorTotal(),itemPedido.getQuantidade());
		}
		
	
}
