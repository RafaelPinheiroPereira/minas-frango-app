package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.ClienteGrupo;
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
public class ClienteGrupoORM extends RealmObject implements Serializable {
    @PrimaryKey
    private long id;
    private String nome;
    private long idEmpresa;


    public ClienteGrupoORM(ClienteGrupo clienteGrupo){
        this.id=clienteGrupo.getId();
        this.nome=clienteGrupo.getNome();
        this.idEmpresa=clienteGrupo.getIdEmpresa();
    }

    public ClienteGrupoORM(final int id, final String nome) {
        this.id=id;
        this.nome=nome;
    }
}
