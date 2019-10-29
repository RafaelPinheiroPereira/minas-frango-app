package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.RecebimentoORM;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class Recebimento implements Serializable, Comparable<Recebimento> {

    private boolean check = false;

    private Date dataRecebimento;

    private Date dataVencimento;

    private Date dataVenda;

    private long id;

    private long idCliente;

    private long idFuncionario;

    private long idVenda;

    private int orderSelected = 0;

    private long tipoRecebimento;

    private double valorAmortizado;

    private double valorVenda;
    private String idConta;

    public Recebimento(RecebimentoORM recebimentoORM) {
        this.id = recebimentoORM.getId();
        this.check = recebimentoORM.isCheck();
        this.dataRecebimento = recebimentoORM.getDataRecebimento();
        this.dataVencimento = recebimentoORM.getDataVencimento();
        this.dataVenda = recebimentoORM.getDataVenda();
        this.idFuncionario = recebimentoORM.getIdFuncionario();
        this.idCliente = recebimentoORM.getIdCliente();
        this.tipoRecebimento = recebimentoORM.getTipoRecebimento();
        this.valorAmortizado = recebimentoORM.getValorAmortizado();
        this.valorVenda = recebimentoORM.getValorVenda();
        this.orderSelected = recebimentoORM.getOrderSelected();
        this.idVenda = recebimentoORM.getIdVenda();
        this.idConta=recebimentoORM.getIdConta();
    }




    @Override
    public int compareTo(Recebimento outroRecebimento) {
        if (this.getOrderSelected() > outroRecebimento.getOrderSelected()) {
            return 1;
        } else if (this.getOrderSelected() < outroRecebimento.getOrderSelected()) {
            return -1;
        } else {
            return 0;
        }
    }
}
