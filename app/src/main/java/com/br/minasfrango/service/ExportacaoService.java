package com.br.minasfrango.service;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.com.br.minasfrango.dto.ListaPedidoDTO;
import com.br.minasfrango.model.com.br.minasfrango.dto.PedidoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExportacaoService {
		
		@POST("exportaPedido")
		Call<Boolean> exportacaoPedido(@Body ListaPedidoDTO pedidoDTOS);
		
}
