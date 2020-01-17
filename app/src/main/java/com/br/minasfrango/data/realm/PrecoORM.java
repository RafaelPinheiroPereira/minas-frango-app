package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Preco;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrecoORM extends RealmObject implements Serializable {

    PrecoIDORM chavesPrecoORM;

    @PrimaryKey
    private String id;

    private double valor;
    private long idEmpresa;
    private long idNucleo;

    public PrecoORM(Preco preco) {
        this.id = preco.getId();
        this.chavesPrecoORM = new PrecoIDORM(preco.getChavesPreco());
        this.valor = preco.getValor();
        this.idEmpresa=preco.getIdEmpresa();
        this.idNucleo=preco.getIdNucleo();
    }

    @Override
    public String toString() {
        return "PrecoORM{"
                + "id='"
                + id
                + '\''
                + ", chavesPrecoORM="
                + chavesPrecoORM
                + ", valor="
                + valor
                + '}';
    }
}
