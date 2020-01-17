package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.NucleoORM;
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
public class Nucleo implements Serializable {

    private long id;

    private String nome;

    private String cnpj;

    private Date dataInicio;

    private Date dataFim;

    public Nucleo(NucleoORM nucleoORM){
        this.id=nucleoORM.getId();
        this.nome=nucleoORM.getNome();
        this.cnpj=nucleoORM.getCnpj();
        this.dataInicio=nucleoORM.getDataInicio();
        this.dataInicio=nucleoORM.getDataFim();
    }

    public Nucleo(final int id, final String nome) {
        this.id=id;
        this.nome=nome;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
