package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.BlocoReciboORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlocoRecibo implements Serializable {

    private long id;

    private String idFormatado;

    private String nomeFoto;

    private boolean fotoMigrada = false;

    public BlocoRecibo(BlocoReciboORM blocoReciboORM) {
        this.id = blocoReciboORM.getId();
        this.idFormatado=blocoReciboORM.getIdFormatado();
        this.nomeFoto=blocoReciboORM.getNomeFoto();
        this.fotoMigrada =blocoReciboORM.isFotoMigrada();

    }
}
