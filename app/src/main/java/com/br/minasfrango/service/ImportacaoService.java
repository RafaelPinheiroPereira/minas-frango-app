package com.br.minasfrango.service;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ImportacaoService {
		
		@POST("importaCliente")
		Call<Cliente> importacaoCliente(@Body Funcionario funcionario);
}
