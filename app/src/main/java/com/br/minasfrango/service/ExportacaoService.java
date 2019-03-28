package com.br.minasfrango.service;

import com.br.minasfrango.dto.ListaPedidoDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExportacaoService {
		
		@POST("exportaPedido")
		Call<Boolean> exportacaoPedido(@Body ListaPedidoDTO pedidoDTOS);
		
}
