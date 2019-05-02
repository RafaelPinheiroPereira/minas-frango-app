package com.br.minasfrango.ui.mvp.sales;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.ItemPedido;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.Preco;
import com.br.minasfrango.data.realm.Produto;
import com.br.minasfrango.data.realm.TipoRecebimento;
import com.br.minasfrango.data.realm.Unidade;
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

        Cliente findClienteByID(long codigoCliente);

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

        Pedido getOrderSale();

        void setOrderSale(final Pedido orderSale);


        Pedido loadSaleOrder(long keyPedido);

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

        Unidade getUnitSelected();

        void setUnitSelected(Unidade unitSelected);

        List<Produto> loadAllProducts();

        ArrayList<String> loadAllProductsByName(List<Produto> produtos);

        ArrayList<String> loadAllProductsID(List<Produto> products);

        List<Unidade> loadAllUnitys();

        ArrayList<String> loadAllUnitysToString(List<Unidade> unitys);

        void loadDetailsSale() throws Throwable;

        Preco loadPriceByProduct();

        Preco loadPriceOfUnityByProduct(String unityID);

        Produto loadProductById(long parseLong);

        Produto loadProductByName(String productName);

        TipoRecebimento loadTipoRecebimentoById() throws Throwable;

        List<TipoRecebimento> loadTipoRecebimentosByClient(Cliente client);

        Unidade loadUnityByProduct();

        void refreshSelectedProductViews();

        void saveOrderSale() throws ParseException;

        void setSpinnerProductSelected();

        void setSpinnerUnityPatternOfProductSelected();

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void updateSaleOrder(Pedido orderSale);

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

        ArrayList<String> convertUnitysToString(List<Unidade> unitys);

        void copyOrUpdateSaleOrder(Pedido pedido);

        Cliente findClientById(Long id);

        Produto findProductById(long id);

        Produto findProductByName(String productName);

        Pedido findSalesById(Long id);

        TipoRecebimento findTipoRecebimentoById() throws Throwable;

        List<TipoRecebimento> findTipoRecebimentosByCliente(Cliente client);

        Unidade findUnityByProduct();

        List<Produto> getAllProducts();

        List<Unidade> getAllUnitys();

        int getTipoRecebimentoID();

        ArrayList<String> loadAllProductsByName(List<Produto> produtos);

        ArrayList<String> loadAllProductsID(List<Produto> products);

        Preco loadPriceByProduct();

        Preco loadPriceOfUnityByProduct(String item);

        /**
         * @return saleOrderIdSaved
         */
        long salvarPedido(final Pedido saleOrderToSave);
    }
}
