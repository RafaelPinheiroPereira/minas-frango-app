package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.Funcionario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService  {

	@GET("autenticacaoLogin")
    Call<Funcionario> autenticaLogin(@Query("id") long id, @Query("senha") String senha);
}
