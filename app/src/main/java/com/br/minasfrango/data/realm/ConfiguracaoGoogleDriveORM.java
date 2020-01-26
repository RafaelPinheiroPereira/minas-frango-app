package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.ConfiguracaoGoogleDrive;
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
public class ConfiguracaoGoogleDriveORM extends RealmObject implements Serializable {
    @PrimaryKey
    private long id;

    private long idFuncionario;
    private String idPastaFuncionario;
    private String idPastaVenda;
    private String idPastaRecibo;
    private long  idEmpresa;

    public ConfiguracaoGoogleDriveORM(ConfiguracaoGoogleDrive configuracaoGoogleDrive) {

        this.id= configuracaoGoogleDrive.getId();
        this.idFuncionario=configuracaoGoogleDrive.getIdFuncionario();
        this.idPastaFuncionario =configuracaoGoogleDrive.getIdPastaFuncionario();
        this.idPastaVenda=configuracaoGoogleDrive.getIdPastaVenda();
        this.idPastaRecibo =configuracaoGoogleDrive.getIdPastaRecibo();
        this.idEmpresa= configuracaoGoogleDrive.getIdEmpresa();
    }
}
