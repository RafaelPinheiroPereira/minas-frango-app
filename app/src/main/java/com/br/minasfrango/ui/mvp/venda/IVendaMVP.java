package com.br.minasfrango.ui.mvp.venda;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.ItemPedidoID;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.PedidoORM;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface IVendaMVP {

    interface IPresenter {

        void atulizarViewPrecoPosFoto();

        Double calcularTotalDaVenda();

        void desabilitarBotaoSalvarPedido();

        void error(String msg);

        void esperarPorConexao();

        void setPedido(Pedido Pedido);

        ArrayList<String> converterTipoRecebimentoEmString(List<TipoRecebimento> tiposRecebimentos);

        void exibirBotaoFotografar();

        void dissmis();

        void atualizarPedido(Pedido orderSale);

        AlertDialog getAlertDialog();

        void getParametros();

        void fecharConexaoAtiva();

        int getBicos();

        void setAlertDialog(AlertDialog alertDialog);

        Cliente getCliente();

        void setCliente(final Cliente cliente);

        Context getContext();

        void setBicos(int bicos);

        ItemPedido getItemPedido();


        void setItemPedido(ItemPedido itemPedido);

        List<ItemPedido> getItens();

        void setItens(List<ItemPedido> itens);

        Pedido pesquisarVendaPorId(long keyPedido);



        void carregarDadosDaVenda() throws Throwable;

        Preco getPreco();

        void setPreco(final Preco preco);

        Produto getProdutoSelecionado();

        void setProdutoSelecionado(final Produto produtoSelecionado);

        BigDecimal getQuantidadeProdutos();

        void setQuantidadeProdutos(BigDecimal quantidadeProdutos);

        String getTipoRecebimento();

        void setTipoRecebimento(final String tipoRecebimento);

        int getTipoRecebimentoID();

        BigDecimal getTotalDaVenda();

        void setTotalDaVenda(BigDecimal totalDaVenda);

        BigDecimal getValorTotalProduto();

        void setTotalProductValue(BigDecimal totalProductValue);

        Pedido getPedido();

        Unidade getUnidadeSelecionada();

        List<Produto> carregarProdutos();

        ArrayList<String> carregarProdutosPorNome(List<Produto> produtos);

        void setUnidadeSelecionada(Unidade unidadeSelecionada);

        List<Unidade> carregarUnidades();

        ArrayList<String> carregarIdProdutos(List<Produto> produtos);

        ArrayList<String> carregarUnidadesEmString(List<Unidade> unidades);

        Preco pesquisarPrecoPorProduto();

        Preco pesquisarPrecoDaUnidadePorProduto(String unityID);

        Produto pesquisarProdutoPorId(long parseLong);

        Produto pesquisarProdutoPorNome(String productName);

        List<TipoRecebimento> carregarTipoRecebimentoPorCliente(Cliente cliente);

        Cliente pesquisarClientePorId(long codigoCliente);

        Unidade carregarUnidadesPorProduto();

        void atualizarProdutoSelecionadoView();

        TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable;

        void setSpinnerProdutoSelecionado();

        void setSpinnerUnidadePadraoProdutoSelecionado();

        void exibirDialogAlterarItemPedido(int position);

        void atualizarActCodigoProduto();

        void atualizarRecyclerItens();

        void salvarVenda() throws ParseException;

        void updateTxtAmountOrderSale();

        void updateTxtAmountProducts();

        void imprimirComprovante();

        boolean validarCamposAntesDeAdicionarItem();
    }

    interface IView {

        void atualizarViewsDoProdutoSelecionado();

        void atualizarViewPrecoPosFoto();

        void dissmiss();

        void error(String msg);

        void carregarDadosDaVenda() throws Throwable;

        void exibirBotaoImprimir();

        void getParametros();

        void desabilitarCliqueBotaoSalvarVenda();

        void exibirBotaoFotografar();

        void setSpinnerProductSelected();

        void inicializarSpinnerUnidadesComUnidadePadraoDoProduto();

        void exibirDialogAlterarItemPedido(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void atualizarTextViewTotalVenda();

        void atualizarTextViewValorTotalProduto();

        boolean validarCamposAntesDeAdicionarItem();

    }

    interface IModel {

        long addItemPedido(ItemPedido item);

        void atualizarChaveItemPedido(ItemPedidoID chavesItemPedido);

        void atualizarItemPedido(ItemPedido item);

        ArrayList<String> convertTipoRecebimentoInString(List<TipoRecebimento> tiposRecebimentos);

        void criarChaveItemPedido(ItemPedidoID chavesItemPedido);

        Pedido pesquisarVendaPorId(Long id);

        void copyOrUpdateSaleOrder(Pedido pedido);

        Cliente pesquisarClientePorId(Long id);

        Produto pesquisarProdutoPorId(long id);

        Produto pesquisarProdutoPorNome(String productName);

        ArrayList<String> converterUnidadeParaString(List<Unidade> unidades);

        TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable;

        List<TipoRecebimento> pesquisarTipoRecebimentosPorCliente(Cliente cliente);

        Unidade pesquisarUnidadePorProduto();

        List<Produto> getAllProducts();

        List<Unidade> getAllUnitys();

        int getTipoRecebimentoID();

        ArrayList<String> carregarProdutoPorNome(List<Produto> produto);

        ArrayList<String> carregarProdutoPorId(List<Produto> products);

        Preco carregarPrecoPorProduto();

        Preco carregarPrecoUnidadePorProduto(String item);

        /**
         * @return saleOrderIdSaved
         */
        long salvarPedido(final PedidoORM saleOrderToSave);
    }
}
