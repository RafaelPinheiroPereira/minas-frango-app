package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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
public class Recebimento extends RealmObject implements Serializable {

    private boolean check = false;

    private Date dataRecebimento;

    private Date dataVencimento;

    private Date dataVenda;

    @PrimaryKey
    private long id;

    private long idCliente;

    private long idFuncionario;

    private long idVenda;

    @Ignore
    private int orderSelected = 0;

    private long tipoRecebimento;

    private double valorAmortizado;

    private double valorVenda;

    public Recebimento(
            long idFuncionario,
            long idCliente,
            long idVenda,
            Date dataVenda,
            double valorVenda,
            Date dataVencimento) {
        this.idFuncionario = idFuncionario;
        this.idCliente = idCliente;
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.valorVenda = valorVenda;
        this.dataVencimento = dataVencimento;
    }
}
