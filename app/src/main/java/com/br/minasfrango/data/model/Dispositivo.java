package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.DispositivoORM;
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
 public class Dispositivo implements Serializable {
    @PrimaryKey
    private String mac;

    private String nome;

    private String descricao;

    private long idEmpresa;

    private Date dataInicio;

    private Date dataFim;

    private String ativo;

    public  Dispositivo(DispositivoORM dispositivoORM){
        this.mac=dispositivoORM.getMac();
        this.nome=dispositivoORM.getNome();
        this.descricao=dispositivoORM.getDescricao();
        this.idEmpresa=dispositivoORM.getIdEmpresa();
        this.dataInicio=dispositivoORM.getDataInicio();
        this.dataFim=dispositivoORM.getDataFim();
        this.ativo=dispositivoORM.getAtivo();
    }
}
