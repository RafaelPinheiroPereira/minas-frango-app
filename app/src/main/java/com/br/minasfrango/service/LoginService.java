package com.br.minasfrango.service;

import android.os.AsyncTask;

import com.br.minasfrango.model.Funcionario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService  {

	@GET("autenticacaoLogin")
	Call <Funcionario>autenticaLogin(@Query("senha")String senha,@Query("id") long id);
}
