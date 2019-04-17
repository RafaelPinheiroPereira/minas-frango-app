package com.br.minasfrango.service;

import com.br.minasfrango.data.dto.RecebimentoDTO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Funcionario;
import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.data.pojo.TipoRecebimento;
import com.br.minasfrango.data.pojo.Unidade;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ImportacaoService {
		
		@POST("importaCliente")
		Call<List<Cliente>>importacaoCliente(@Body Funcionario funcionario);
		
		@GET("importaProduto")
		Call<List<Produto>>importacaoProduto();
		
		@GET("importaFormaDePagamento")
		Call<List<TipoRecebimento>>importacaoTipoRecebimento();
		@GET("importaUnidade")
		Call<List<Unidade>>importacaoUnidade();
		@GET("importaPreco")
		Call<List<Preco>>importacaoPreco();
		
		@POST("importaRecebimento")
		Call<List<RecebimentoDTO>>importacaoRecebimentos(@Body Funcionario funcionario);
}
