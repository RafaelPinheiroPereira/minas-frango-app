package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.BlocoRecibo;
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
public class BlocoReciboORM extends RealmObject implements Serializable {

    @PrimaryKey private long id;

    private String idFormatado;

    private String nomeFoto;

    private boolean fotoMigrada = false;

    public BlocoReciboORM(BlocoRecibo blocoRecibo) {
        this.id = blocoRecibo.getId();
        this.idFormatado=blocoRecibo.getIdFormatado();
        this.nomeFoto=blocoRecibo.getNomeFoto();
        this.fotoMigrada =blocoRecibo.isFotoMigrada();
    }
}
