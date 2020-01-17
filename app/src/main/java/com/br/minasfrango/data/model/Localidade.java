package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.LocalidadeORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Localidade implements Serializable {

    private long id;

    private String nome;

    private Rota rota;

    private long idEmpresa;

    public Localidade(LocalidadeORM localidadeORM) {
        this.id = localidadeORM.getId();
        this.rota = new Rota(localidadeORM.getMRotaORM());
        this.nome = localidadeORM.getNome();
        this.idEmpresa=localidadeORM.getIdEmpresa();
    }
}
