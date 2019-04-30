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

        Double calculeTotalOrderSale();

        void desabilitarBtnSalvar();

        void error(String msg);

        void esperarPorConexao();

        ArrayList<String> convertTipoRecebimentoInString(List<TipoRecebimento> tiposRecebimentos);

        void dissmis();

        Cliente findClienteByID(long codigoCliente);

        AlertDialog getAlertDialog();

        void getParams();

        void fecharConexaoAtiva();


        void setAlertDialog(AlertDialog alertDialog);

        Cliente getClient();

        void setClient(final Cliente client);

        Context getContext();

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

        boolean validateFieldsBeforeAddItem();

        void imprimirPedido();
    }

    interface IView {

        void desabilitarBtnSalvar();

        void dissmiss();

        void error(String msg);

        void exibirBotaoImprimir();

        void getParams();

        void loadDetailsSale() throws Throwable;

        void refreshSelectedProductViews();

        void setSpinnerProductSelected();

        void setSpinnerUnityPatternOfProductSelected();

        void showOnUpdateDialog(int position);

        void updateActCodigoProduto();

        void updateRecyclerItens();

        void updateTxtAmountOrderSale();

        void updateTxtAmountProducts();

        boolean validateFieldsBeforeAddItem();

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
        long saveOrderSale(final Pedido saleOrderToSave);
    }
}
