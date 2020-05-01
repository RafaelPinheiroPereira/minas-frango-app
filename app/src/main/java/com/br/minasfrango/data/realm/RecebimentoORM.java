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


    @PrimaryKey
    private long id;

    private boolean check = false;

    private Date dataRecebimento;

    private Date dataVencimento;

    private Date dataVenda;

    private long idCliente;

    private long idEmpresa;

    private long idFuncionario;

    private long idVenda;

    @Ignore
    private int orderSelected = 0;

    private long tipoRecebimento;

    private double valorAmortizado;

    private double valorVenda;

    private String idConta;

    private long idPedidoBloco;

    private long idNucleo;

    private long idRecibo;






    public RecebimentoORM(Recebimento recebimento) {
        this.id =  recebimento.getIdVenda();
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
        this.idEmpresa=recebimento.getIdEmpresa();
        this.idPedidoBloco=recebimento.getIdPedidoBloco();
        this.idNucleo=recebimento.getIdNucleo();
        this.idRecibo=recebimento.getIdRecibo();



    }
}
