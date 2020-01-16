package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Nucleo;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NucleoORM  extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;

    private String nome;

    private String cnpj;

    private Date dataInicio;

    private Date dataFim;

    public NucleoORM(Nucleo nucleo){
        this.id=nucleo.getId();
        this.nome=nucleo.getNome();
        this.cnpj=nucleo.getCnpj();
        this.dataInicio=nucleo.getDataInicio();
        this.dataInicio=nucleo.getDataFim();
    }
}
