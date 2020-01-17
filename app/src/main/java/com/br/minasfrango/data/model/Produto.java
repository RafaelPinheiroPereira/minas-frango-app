package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ProdutoORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Produto implements Serializable {

    private long id;

    private String nome;

    private double quantidade;

    private String unidade;

    private long idEmpresa;
    private long idNucleo;

    public Produto(ProdutoORM produtoORM) {
        this.id = produtoORM.getId();
        this.nome = produtoORM.getNome();
        this.unidade = produtoORM.getUnidade();
        this.quantidade = produtoORM.getQuantidade();
        this.idEmpresa=produtoORM.getIdEmpresa();
        this.idNucleo=produtoORM.getIdNucleo();
    }

}
