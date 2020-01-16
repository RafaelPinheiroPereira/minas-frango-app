package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Dispositivo;
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

public class DispositivoORM extends RealmObject implements Serializable {

    @PrimaryKey
    private String mac;

    private String nome;

    private String descricao;

    private long idEmpresa;

    private Date dataInicio;

    private Date dataFim;

    private String ativo;

    public  DispositivoORM(Dispositivo dispositivo){
        this.mac=dispositivo.getMac();
        this.nome=dispositivo.getNome();
        this.descricao=dispositivo.getDescricao();
        this.idEmpresa=dispositivo.getIdEmpresa();
        this.dataInicio=dispositivo.getDataInicio();
        this.dataFim=dispositivo.getDataFim();
        this.ativo=dispositivo.getAtivo();
    }
}
