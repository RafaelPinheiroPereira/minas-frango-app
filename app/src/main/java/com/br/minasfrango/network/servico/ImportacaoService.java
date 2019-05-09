package com.br.minasfrango.network.servico;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ImportacaoService {

    @POST("importaCliente")
    Call<List<Cliente>> importarCliente(@Body Funcionario funcionario);

    @GET("importaPreco")
    Call<List<Preco>> importarPreco();

    @GET("importaProduto")
    Call<List<Produto>> importarProduto();

    @POST("importaRecebimento")
    Call<List<Recebimento>> importarRecebimentos(@Body Funcionario funcionario);

    @GET("importaFormaDePagamento")
    Call<List<TipoRecebimento>> importarTipoRecebimento();

    @GET("importaUnidade")
    Call<List<Unidade>> importarUnidade();
}
