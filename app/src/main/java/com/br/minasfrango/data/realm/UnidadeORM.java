package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Unidade;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UnidadeORM extends RealmObject implements Serializable {

    UnidadeProdutoIDORM chavesUnidadeORM;

    @PrimaryKey
    private String id;

    private String nome;

    private String unidadePadrao;
    private long idEmpresa;

    public UnidadeORM(Unidade unidade) {
        this.id = unidade.getId();
        this.chavesUnidadeORM = new UnidadeProdutoIDORM(unidade.getChavesUnidade());
        this.nome = unidade.getNome();
        this.unidadePadrao = unidade.getUnidadePadrao();
        this.idEmpresa=unidade.getIdEmpresa();
    }
}
