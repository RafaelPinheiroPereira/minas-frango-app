package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.ListaPedido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExportacaoService {
		
		@POST("exportacoes/pedidos")
        Call<Boolean> realizarExportacao(@Body ListaPedido pedidoDTOS);
		
}
