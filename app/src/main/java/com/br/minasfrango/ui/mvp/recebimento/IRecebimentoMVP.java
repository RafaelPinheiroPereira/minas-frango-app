package com.br.minasfrango.ui.mvp.recebimento;

import android.content.Context;
import com.br.minasfrango.data.model.BlocoRecibo;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.util.DriveServiceHelper;
import java.math.BigDecimal;
import java.util.List;

public interface IRecebimentoMVP {

    interface IPresenter {

        void alterarBlocoRecibo(BlocoRecibo blocoRecibo);

        List<Recebimento> obterRecebimentoPorCliente();

        void atualizarViewSaldoDevedor();

        void calcularAmortizacaoAutomatica();

        /**
         * @param position do item selecionado na lista de notas abertas
         */
        void calcularArmotizacaoManual(final int position);

        void atualizarRecycleView();

        void exibirBotaoGerarRecibo();

        BigDecimal getValorTotalAmortizado();

        void configurarViewComDadosDoCliente();

        void desabilitarBotaoSalvar();

        Cliente getCliente();

        boolean ehAmortizacaoAutomatica();

        /**
         * Metodos relacionados a impressao
         */
        void esperarPorConexao();

        void exibirBotaoFotografar();



        void removerAmortizacao(int position);

        void salvarAmortizacao(final long idBlocoRecibo);

        void setCliente(Cliente cliente);

        Context getContext();

        long configurarSequenceDoRecebimento();



        BigDecimal getCredito();

        Conta getConta();

        void setConta(Conta conta);

        void setCredito(BigDecimal credit);

        int getPositionOpenNotaSelect();

        void getParametros();

        List<Recebimento> getRecebimentos();

        DriveServiceHelper getDriveServiceHelper();

        void setDriveServiceHelper(DriveServiceHelper driveServiceHelper);

        BigDecimal getValorTotalDevido();



        void fecharConexaoAtiva();

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                int i, Recebimento recebimento);

        boolean saldoDevidoEhMaiorQueZero();

        void setAutomaticNoteSelectionOrder();

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int i, Recebimento recebimento);

        void setPositionOpenNotaSelect(int positionOpenNotaSelect);

        void setTypeOfAmortizationIsAutomatic(boolean typeOfAmortizationIsAutomatic);

        void showInsuficentCredit(String s);

        void updateRecycleViewAlteredItem(int position);

        void imprimirComprovante();

        boolean valorDoCreditoEhMaiorDoQueZero();

        boolean valorTotalDevidoEhMenorOuIgualAoCredito();

        List<Conta> pesquisarContaPorId();

        void verificarCredenciaisGoogleDrive();

        BlocoRecibo getBlocoRecibo();

        void setBlocoRecibo(BlocoRecibo blocoRecibo);
    }

    interface IModel {

        void alterarBlocoRecibo(BlocoRecibo blocoRecibo);

        void calcularAmortizacaoAutomatica();
        void calcularArmotizacaoManual(int position);

        long configurarSequenceDoRecebimento();

        boolean crediValueIsGranThenZero();
        List<Conta> pesquisarContaPorId();



        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                final int posicao, Recebimento recebimentoToUpdate);

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int posicao, Recebimento recebimentoToUpdate);

        void removerAmortizacao(int position);

        boolean saldoDevidoEhMaiorQueZero();

        void salvarAmortizacao(final long idBlocoRecibo);

        void setOrdenarSelecaoAutomaticaDasNotas();

        boolean ehMenorOuIgualAoCreditoOValorDoDebito();

        List<Recebimento> pesquisarRecebimentoPorCliente() ;

    }

    interface IView {

        void atualizarViewSaldoDevedor();

        void configurarViewComDadosDoCliente();

        void exibirBotaoComprovante();

        void exibirBotaoFotografar();

        void desabilitarBotaoSalvarAmortizacao();

        void getParametros();

        void showInsuficentCredit(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);

        void verificarCredenciaisGoogleDrive();
    }
}