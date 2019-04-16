package com.br.minasfrango.data.dto;

import com.br.minasfrango.data.model.ItemPedido;
import java.io.Serializable;
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
