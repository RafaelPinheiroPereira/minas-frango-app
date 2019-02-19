package com.br.minasfrango.model.com.br.minasfrango.dto;

import com.br.minasfrango.model.ItemPedidoID;
import com.br.minasfrango.model.Pedido;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoIDDTO implements Serializable {
		
		private long id;
		
		private double idVenda;
		
		private double idProduto;
		
		private String idUnidade;
		
		private Date dataVenda;
		
		private String vendaMae;
		
		private String tipoVenda;
		
		private double nucleoCodigo;
		
		public static ItemPedidoIDDTO transformaEmItemPedidoIDDTO(ItemPedidoID itemPedidoID) {
				return new ItemPedidoIDDTO(itemPedidoID.getId(),
								itemPedidoID.getIdVenda(),
								itemPedidoID.getIdProduto(), itemPedidoID.getIdUnidade(), itemPedidoID.getDataVenda(), itemPedidoID.getVendaMae(), itemPedidoID.getTipoVenda(), itemPedidoID.getNucleoCodigo());
		}
}
