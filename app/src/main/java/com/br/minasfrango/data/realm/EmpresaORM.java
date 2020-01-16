package com.br.minasfrango.data.realm;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Dispositivo;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Nucleo;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmpresaORM extends RealmObject implements Serializable {

    @PrimaryKey private long id;

    private String cnpj;

    private String nome;

    private Date dataInicio;

    private Date dataFim;

    private String ativo;

    private RealmList<NucleoORM> nucleosORM;
    private RealmList<DispositivoORM> dispositivosORM;

    public EmpresaORM(Empresa empresa) {

        this.id = empresa.getId();
        this.cnpj = empresa.getCnpj();
        this.dataInicio = empresa.getDataInicio();
        this.dataFim = empresa.getDataFim();
        this.nucleosORM = this.converterListNucleoParaListNucleoRealm(empresa.getNucleos());
        this.dispositivosORM=this.converterListDispositivoParaListDispositivoRealm(empresa.getDispositivos());
        this.ativo=empresa.getAtivo();
    }

    private RealmList<NucleoORM> converterListNucleoParaListNucleoRealm(List<Nucleo> nucleos) {
        RealmList<NucleoORM> realmList = new RealmList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            nucleos.forEach(nucleo -> realmList.add(new NucleoORM(nucleo)));
        } else {
            for (Nucleo nucleo : nucleos) {
                realmList.add(new NucleoORM(nucleo));
            }
        }
        return realmList;
    }


    private RealmList<DispositivoORM> converterListDispositivoParaListDispositivoRealm(
            List<Dispositivo> dispositivos) {
        RealmList<DispositivoORM> realmList = new RealmList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            dispositivos.forEach(dispositivo -> realmList.add(new DispositivoORM(dispositivo)));
        } else {
            for (Dispositivo dispositivo : dispositivos) {
                realmList.add(new DispositivoORM(dispositivo));
            }
        }
        return realmList;
    }
}
