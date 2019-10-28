package com.br.minasfrango.data.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Importacao {
    List<Cliente> clientes;
    List<Rota> rotas;
    List<Produto> produtos;
    List<TipoRecebimento> tiposRecebimento;
    List<Unidade> unidades;
    List<Preco> precos;
    List<Recebimento> recebimentosDTO;
    List<Conta> contas;

}
