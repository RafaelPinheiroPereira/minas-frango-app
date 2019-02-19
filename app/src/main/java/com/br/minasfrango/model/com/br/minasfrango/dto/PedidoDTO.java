package com.br.minasfrango.model.com.br.minasfrango.dto;

import com.br.minasfrango.model.Pedido;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PedidoDTO implements Serializable

{
		
		private long id;
		private long codigoCliente;
		private long codigoFuncionario;
		private List<ItemPedidoDTO> itens;
		private double valorTotal;
		private long tipoRecebimento;
		private Date dataPedido;
	
		
		public PedidoDTO(long id,long idFuncionario, long codigoCliente, double valorTotal, long tipoRecebimento, Date dataPedido) {
	           this.id=id;
	           this.codigoCliente=codigoCliente;
	           this.valorTotal=valorTotal;
	           this.tipoRecebimento=tipoRecebimento;
	           this.dataPedido=dataPedido;
	           this.codigoFuncionario =idFuncionario;
	           
		}
		
		public static PedidoDTO transformaEmPedidoDTO(Pedido pedido){
				return new PedidoDTO(pedido.getId(),pedido.getCodigoFuncionario(),pedido.getCodigoCliente(),pedido.getValorTotal(),pedido.getTipoRecebimento(),pedido.getDataPedido());
		}
	
}
