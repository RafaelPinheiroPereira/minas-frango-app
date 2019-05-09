package com.br.minasfrango.ui.mvp.venda;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
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

public interface ISalesMVP {

    interface IPresenter {

        void atualizarTxtValorTotalPoduto();

        void atualizarTxtValorTotalVenda();

        void desabilitarBtnSalvar();

        void error(String msg);

        void esperarPorConexao();

        void atualizarViewPrecoPosFoto();

        void exibirBotaoFotografar();

        void dissmis();

        void atualizarPedido(Pedido orderSale);

        AlertDialog getAlertDialog();

        void getParams();

        void fecharConexaoAtiva();

        int getBicos();

        void atualizarViewsDoProdutoSelecionado();

        void setAlertDialog(AlertDialog alertDialog);

        Double calcularValorTotalVenda();

        ArrayList<String> converterTipoRecebimentoEmString(List<TipoRecebimento> tiposRecebimentos);

        Context getContext();

        void setBicos(int bicos);

        ItemPedido getItemPedido();

        void setItemPedido(ItemPedido itemPedido);

        List<ItemPedido> getItens();

        void setItens(List<ItemPedido> itens);

        Pedido buscarVendaPorId(long keyPedido);

        void setOrderSale(final Pedido orderSale);

        void carregarDadosDaVenda() throws Throwable;

        Cliente getCliente();

        void setCliente(final Cliente cliente);

        Pedido getPedido();

        Preco getPreco();

        BigDecimal getQtdProdutos();

        void setQtdProdutos(BigDecimal qtdProdutos);

        String getTipoRecebimento();

        void setTipoRecebimento(final String tipoRecebimento);

        int getTipoRecebimentoID();

        void setPreco(final Preco preco);

        Produto getProdutoSelecionado();

        void setProdutoSelecionado(final Produto produtoSelecionado);

        BigDecimal getValorTotalPedido();

        void setValorTotalPedido(BigDecimal valorTotalPedido);

        Unidade getUnidadeSelecionada();

        BigDecimal getValorTotalProduto();

        void setValorTotalProduto(BigDecimal valorTotalProduto);

        void setUnidadeSelecionada(Unidade unidadeSelecionada);

        boolean localizacaoHabilitada();

        ArrayList<String> obterIDProdutos(List<Produto> produtos);

        ArrayList<String> obterNomeDeTodosOsProdutos(List<Produto> produtos);

        List<Unidade> obterTodasUnidades();

        ArrayList<String> obterTodasUnidadesEmString(List<Unidade> unidades);

        List<Produto> obterTodosProdutos();

        Preco pesquisarPrecoDaUnidadePorProduto(String unityID);

        Preco pesquisarPrecoPorProduto();

        Cliente pesquisarClientePorId(long codigoCliente);

        Produto pesquisarProdutoPorId(long parseLong);

        Produto pesquisarProdutoPorNome(String productName);

        TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable;

        void setSpinnerProductSelected();

        List<TipoRecebimento> pesquisarTipoRecebimentosPorCliente(Cliente cliente);

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void salvarVenda() throws ParseException;

        Unidade pesquisarUnidadePorProduto();

        void setSpinnerUnidadePadraoDoProdutoSelecionado();

        void imprimirComprovante();

        boolean validarCamposAntesDeAdicionarItem();
    }

    interface IView {

        void atualizarViewsDoProdutoSelecionado();

        void atulizarViewPrecoPosFoto();

        void dissmiss();

        void error(String msg);

        void carregarDadosDaVenda() throws Throwable;

        void exibirBotaoImprimir();

        void getParams();

        void desabilitarCliqueBotaoSalvarVenda();

        void exibirBotaoFotografar();

        void setSpinnerProductSelected();

        void setSpinnerUnidadePadraoDoProdutoSelecionado();

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void updateTxtAmountOrderSale();

        void updateTxtAmountProducts();

        boolean validarCamposAntesDeAdicionarItem();
    }

    interface IModel {

        long adicionarItemDoPedido(ItemPedido item);

        Preco carregarPrecoDaUnidadePorProduto(String item);

        Pedido buscarVendaPorId(Long id);

        void copyOrUpdateSaleOrder(Pedido pedido);

        Preco carregarPrecoPorProduto();

        ArrayList<String> converterTipoRecebimentoEmString(List<TipoRecebimento> tiposRecebimentos);

        ArrayList<String> converterUnidadesEmString(List<Unidade> unidades);

        ArrayList<String> obterNomeDeTodosOsProdutos(List<Produto> produto);

        Cliente pesquisarClientePorID(Long id);

        List<TipoRecebimento> findTipoRecebimentosByCliente(Cliente cliente);

        ArrayList<String> pesquisarNomeDeTodosProdutos(List<Produto> products);

        List<Produto> getAllProducts();

        List<Unidade> getAllUnitys();

        int getTipoRecebimentoID();

        Produto pesquisarProdutoPorID(long id);

        Produto pesquisarProdutoPorNome(String productName);

        TipoRecebimento pesquisarTipoRecebimentoPorID() throws Throwable;

        Unidade pesquisarUnidadePorProduto();

        /**
         * @return saleOrderIdSaved
         */
        long salvarPedido(final PedidoORM saleOrderToSave);
    }
}
