package com.br.minasfrango.ui.mvp.recebimento;

import android.content.Context;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentsMVP {

    interface IPresenter {

        void atualizarViewSaldoDevedor();

        void calcularAmortizacaoAutomatica();

        /**
         * @param position do item selecionado na lista de notas abertas
         */
        void calcularArmotizacaoManual(final int position);

        void exibirBotaoGerarRecibo();

        BigDecimal getValorTotalAmortizado();

        void carregarDadosClienteView();

        ArrayList<String> carregarDescricaoTipoRecebimentosAVista() throws Throwable;

        Cliente getCliente();

        /** Metodos relacionados a impressao */
        void esperarPorConexao();

        void exibirMensagemDeSaldoInsuficiente(String s);

        BigDecimal getValorCredito();

        void removerAmortizacao(int position);

        void salvarAmortizacao();

        void setCliente(Cliente cliente);

        Context getContext();

        void setValorCredito(BigDecimal valorCredito);

        BigDecimal getValorTotalDevido();

        int getIdTipoRecebimento();

        void setIdTipoRecebimento(int idTipoRecebimento);

        void getParams();

        int getPositionOpenNotaSelect();

        List<Recebimento> getRecebimentos();

        boolean isAmortizacaoAutomatica();

        void setAmortizacaoAutomatica(boolean amortizacaoAutomatica);

        int pesquisarIdTipoRecebimento(String item);

        void fecharConexaoAtiva();

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(int i, Recebimento recebimento);

        boolean saldoDevidoEhMaiorQueZero();

        void setAutomaticNoteSelectionOrder();

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(int i, Recebimento recebimento);

        boolean valorCreditoEhMaiorQueZero();

        boolean valorTotalDebitoEhMenorOuIgualAoCredito();

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);

        void imprimirComprovante();

        void inabilitarBotaoSalvar();

        List<Recebimento> pesquisarRecebimentoPorCliente();
    }

    interface IModel {

        void calcularAmortizacaoAutomatica();

        void calcularArmotizacaoManual(int position);

        ArrayList<String> obterTipoRecebimentosAVista() throws Throwable;

        int pesquisarIdTipoRecebimento(String item);

        List<Recebimento> pesquisarRecebimentoPorCliente();

        boolean valorCreditoEhMaiorQueZero();

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                final int posicao, Recebimento recebimentoToUpdate);

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int posicao, Recebimento recebimentoToUpdate);

        void removerAmortizacao(int position);

        boolean saldoDevidoEhMaiorQueZero();

        void salvarAmortizacao();

        void setAutomaticNoteSelectionOrder();

        boolean totalValueOfDebtISLessTranCreditOrEquals();
    }

    interface IView {

        void atualizarViewSaldoDevedor();

        void exibirBotaoGerarRecibo();

        void getParams();

        void inabilitarBotaoSalvarAmortizacao();

        void setClientViews();

        void showCreditoInsuficiente(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);
    }
}
