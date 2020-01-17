package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.Funcionario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AutenticacaoService {

	@GET("autenticacoes/funcionarios")
    Call<Funcionario> autenticar(@Query("id") long id, @Query("senha") String senha, @Query("idEmpresa")long idEmpresa);
}
