package com.br.minasfrango.data.model;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.realm.DispositivoORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.NucleoORM;
import io.realm.RealmList;
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
 public class Empresa implements Serializable {

    private long id;

    private String cnpj;

    private String nome;

    private Date dataInicio;

    private Date dataFim;

    private String ativo;

    List<Nucleo> nucleos;
    List<Dispositivo> dispositivos;

     public  Empresa (EmpresaORM empresaORM){

          this.id=empresaORM.getId();
          this.cnpj=empresaORM.getCnpj();
          this.dataInicio=empresaORM.getDataInicio();
          this.dataFim=empresaORM.getDataFim();
          this.nucleos=this.converterListNucleoRealmParaNucleo(empresaORM.getNucleosORM());
          this.dispositivos=this.converterListDispositivoRealmParaDispositivo(empresaORM.getDispositivosORM());
          this.ativo=empresaORM.getAtivo();

     }

     private   List<Nucleo> converterListNucleoRealmParaNucleo(
             List<NucleoORM> nucleosORM) {
          List<Nucleo> nucleos = new RealmList<>();
          if (VERSION.SDK_INT >= VERSION_CODES.N) {
               nucleosORM.forEach(nucleoORM->nucleos.add(new Nucleo(nucleoORM)));
          } else {
               for (NucleoORM nucleoORM : nucleosORM) {
                    nucleos.add(new Nucleo(nucleoORM));
               }
          }
          return nucleos;
     }

     private   List<Dispositivo> converterListDispositivoRealmParaDispositivo(
             List<DispositivoORM> dispositivoORMS) {
          List<Dispositivo> dispositivos = new RealmList<>();
          if (VERSION.SDK_INT >= VERSION_CODES.N) {
               dispositivoORMS.forEach(dispositivoORM->dispositivos.add(new Dispositivo(dispositivoORM)));
          } else {
               for (DispositivoORM dispositivoORM : dispositivoORMS) {
                    dispositivos.add(new Dispositivo(dispositivoORM));
               }
          }
          return dispositivos;
     }
}
