package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Recebimento;
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
public class RecebimentoORM extends RealmObject implements Serializable {

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
    private String idConta;

    public RecebimentoORM(
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

    public RecebimentoORM(Recebimento recebimento) {
        this.id = recebimento.getId();
        this.check = recebimento.isCheck();
        this.dataRecebimento = recebimento.getDataRecebimento();
        this.dataVencimento = recebimento.getDataVencimento();
        this.dataVenda = recebimento.getDataVenda();
        this.idFuncionario = recebimento.getIdFuncionario();
        this.idCliente = recebimento.getIdCliente();
        this.tipoRecebimento = recebimento.getTipoRecebimento();
        this.valorAmortizado = recebimento.getValorAmortizado();
        this.valorVenda = recebimento.getValorVenda();
        this.orderSelected = recebimento.getOrderSelected();
        this.idVenda = recebimento.getIdVenda();
        this.idConta=recebimento.getIdConta();
    }
}
