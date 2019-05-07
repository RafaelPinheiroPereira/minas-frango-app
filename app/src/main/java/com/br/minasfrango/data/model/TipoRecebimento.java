package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.TipoRecebimentoORM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TipoRecebimento {

    private long id;

    private String nome;

    public TipoRecebimento(TipoRecebimentoORM tipoRecebimentoORM) {
        this.id = tipoRecebimentoORM.getId();
        this.nome = tipoRecebimentoORM.getNome();

    }

}
