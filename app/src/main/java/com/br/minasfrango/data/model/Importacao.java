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
    
    List<Produto> produtos;
    List<Unidade> unidades;
    List<Preco> precos;
    List<Recebimento> recebimentosDTO;
    List<Conta> contas;
    List<ClienteGrupo> clientesGrupos;




}
