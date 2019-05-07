package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.ListaPedido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExportacaoService {
		
		@POST("exportaPedido")
        Call<Boolean> exportacaoPedido(@Body ListaPedido pedidoDTOS);
		
}
