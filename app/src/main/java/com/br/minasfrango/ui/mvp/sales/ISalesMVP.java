package com.br.minasfrango.ui.mvp.sales;

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

        void atulizarViewPrecoPosFoto();

        Double calculeTotalOrderSale();

        void desabilitarBtnSalvar();

        void error(String msg);

        void esperarPorConexao();

        ArrayList<String> convertTipoRecebimentoInString(List<TipoRecebimento> tiposRecebimentos);

        void exibirBotaoFotografar();

        void dissmis();

        void atualizarPedido(Pedido orderSale);

        AlertDialog getAlertDialog();

        void getParams();

        void fecharConexaoAtiva();

        int getBicos();

        void setAlertDialog(AlertDialog alertDialog);

        Cliente getClient();

        void setClient(final Cliente client);

        Context getContext();

        void setBicos(int bicos);

        ItemPedido getItemPedido();


        void setItemPedido(ItemPedido itemPedido);

        List<ItemPedido> getItens();

        void setItens(List<ItemPedido> itens);

        Pedido buscarVendaPorId(long keyPedido);

        void setOrderSale(final Pedido orderSale);

        void carregarDadosDaVenda() throws Throwable;

        Preco getPrice();

        void setPrice(final Preco price);

        Produto getProductSelected();

        void setProductSelected(final Produto productSelected);

        BigDecimal getQtdProdutos();

        void setQtdProdutos(BigDecimal qtdProdutos);

        String getTipoRecebimento();

        void setTipoRecebimento(final String tipoRecebimento);

        int getTipoRecebimentoID();

        BigDecimal getTotalOrderSale();

        void setTotalOrderSale(BigDecimal totalOrderSale);

        BigDecimal getTotalProductValue();

        void setTotalProductValue(BigDecimal totalProductValue);

        Pedido getOrdemVenda();

        Unidade getUnidadeSelecionada();

        List<Produto> loadAllProducts();

        ArrayList<String> loadAllProductsByName(List<Produto> produtos);

        void setUnidadeSelecionada(Unidade unidadeSelecionada);

        List<Unidade> loadAllUnitys();

        ArrayList<String> loadAllProductsID(List<Produto> produtos);

        ArrayList<String> loadAllUnitysToString(List<Unidade> unidades);

        Preco loadPriceByProduct();

        Preco loadPriceOfUnityByProduct(String unityID);

        Produto loadProductById(long parseLong);

        Produto loadProductByName(String productName);

        List<TipoRecebimento> loadTipoRecebimentosByClient(Cliente cliente);

        Cliente pesquisarClientePorId(long codigoCliente);

        Unidade loadUnityByProduct();

        void refreshSelectedProductViews();

        TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable;

        void setSpinnerProductSelected();

        void setSpinnerUnityPatternOfProductSelected();

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void salvarVenda() throws ParseException;

        void updateTxtAmountOrderSale();

        void updateTxtAmountProducts();

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

        void setSpinnerUnityPatternOfProductSelected();

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void updateTxtAmountOrderSale();

        void updateTxtAmountProducts();

        boolean validarCamposAntesDeAdicionarItem();

    }

    interface IModel {

        long addItemPedido(ItemPedido item);

        ArrayList<String> convertTipoRecebimentoInString(List<TipoRecebimento> tiposRecebimentos);

        Pedido buscarVendaPorId(Long id);

        void copyOrUpdateSaleOrder(Pedido pedido);

        Cliente findClientById(Long id);

        Produto findProductById(long id);

        Produto findProductByName(String productName);

        ArrayList<String> convertUnitysToString(List<Unidade> unidades);

        TipoRecebimento findTipoRecebimentoById() throws Throwable;

        List<TipoRecebimento> findTipoRecebimentosByCliente(Cliente cliente);

        Unidade findUnityByProduct();

        List<Produto> getAllProducts();

        List<Unidade> getAllUnitys();

        int getTipoRecebimentoID();

        ArrayList<String> loadAllProductsByName(List<Produto> produto);

        ArrayList<String> loadAllProductsID(List<Produto> products);

        Preco loadPriceByProduct();

        Preco loadPriceOfUnityByProduct(String item);

        /**
         * @return saleOrderIdSaved
         */
        long salvarPedido(final PedidoORM saleOrderToSave);
    }
}
