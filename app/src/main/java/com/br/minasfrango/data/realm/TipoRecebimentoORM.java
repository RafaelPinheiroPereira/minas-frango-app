package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.TipoRecebimento;
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
public class TipoRecebimentoORM extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;

    private String nome;

    public TipoRecebimentoORM(TipoRecebimento tipoRecebimento) {
        this.id = tipoRecebimento.getId();
        this.nome = tipoRecebimento.getNome();
    }
}
