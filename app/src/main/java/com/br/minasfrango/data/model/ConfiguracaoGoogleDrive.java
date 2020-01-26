package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ConfiguracaoGoogleDriveORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfiguracaoGoogleDrive implements Serializable {

    private long id;
    private long idFuncionario;
    private String idPastaFuncionario;
    private String idPastaVenda;
    private String idPastaRecibo;
    private long  idEmpresa;

    public ConfiguracaoGoogleDrive(ConfiguracaoGoogleDriveORM configuracaoGoogleDriveORM) {
        this.id= configuracaoGoogleDriveORM.getId();
        this.idFuncionario=configuracaoGoogleDriveORM.getIdFuncionario();
        this.idPastaFuncionario =configuracaoGoogleDriveORM.getIdPastaFuncionario();
        this.idPastaVenda=configuracaoGoogleDriveORM.getIdPastaVenda();
        this.idPastaRecibo =configuracaoGoogleDriveORM.getIdPastaRecibo();
        this.idEmpresa= configuracaoGoogleDriveORM.getIdEmpresa();

    }
}
