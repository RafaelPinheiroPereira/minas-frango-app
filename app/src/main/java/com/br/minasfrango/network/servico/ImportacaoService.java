package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.Importacao;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImportacaoService {



    @GET("importacoes/funcionarios")
    Call<Importacao> realizarImportacao( @Query("id") long id,@Query("idEmpresa") long idEmpresa,@Query("idNucleo") long idNucleo);

}
