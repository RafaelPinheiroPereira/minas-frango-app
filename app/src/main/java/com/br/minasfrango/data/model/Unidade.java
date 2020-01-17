package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.UnidadeORM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Unidade {

    UnidadeProdutoID chavesUnidade;

    private String id;

    private String nome;

    private String unidadePadrao;
    private long idEmpresa;

    public Unidade(UnidadeORM unidadeORM) {
        this.id = unidadeORM.getId();
        this.chavesUnidade = new UnidadeProdutoID(unidadeORM.getChavesUnidadeORM());
        this.nome = unidadeORM.getNome();
        this.unidadePadrao = unidadeORM.getUnidadePadrao();
        this.idEmpresa=unidadeORM.getIdEmpresa();

    }

}
