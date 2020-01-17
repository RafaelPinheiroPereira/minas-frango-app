package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ClienteGrupoORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClienteGrupo implements Serializable {


    private long id;
    private String nome;
    private long idEmpresa;

    public ClienteGrupo(ClienteGrupoORM clienteGrupoORM){
        this.id=clienteGrupoORM.getId();
        this.nome=clienteGrupoORM.getNome();
        this.idEmpresa=clienteGrupoORM.getIdEmpresa();
    }
    @Override
    public String toString() {
        return this.getNome();
    }
}
