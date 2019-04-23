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
import com.br.minasfrango.ui.mvp.sales.ISalesMVP.IView;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.SessionManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements ISalesMVP.IPresenter {

    Cliente client;

    ItemPedido itemPedido;

    List<ItemPedido> itens = new ArrayList<>();

    ISalesMVP.IModel mModel;

    ISalesMVP.IView mView;

    Pedido orderSale;

    Preco price;

    Produto productSelected;

    BigDecimal qtdProdutos;

    String tipoRecebimento;

    BigDecimal totalOrderSale;

    BigDecimal totalProductValue;

    Unidade unitSelected;

    private AlertDialog mAlertDialog;

    public Presenter(final IView view) {
        mView = view;
        mModel = new Model(this);
    }

    @Override
    public Double calculeTotalOrderSale() {
        return getItens().stream().mapToDouble(ItemPedido::getValorTotal).sum();
    }

    @Override
    public ArrayList<String> convertTipoRecebimentoInString(
            final List<TipoRecebimento> tiposRecebimentos) {
        return this.mModel.convertTipoRecebimentoInString(tiposRecebimentos);
    }

    @Override
    public void dissmis() {
        this.mView.dissmiss();
    }

    @Override
    public Cliente findClienteByID(final long codigoCliente) {
        return this.mModel.findClientById(codigoCliente);
    }

    @Override
    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    @Override
    public Pedido getOrderSale() {
        return this.orderSale;
    }

    @Override
    public void setAlertDialog(final AlertDialog alertDialog) {
        mAlertDialog = alertDialog;
    }

    @Override
    public Cliente getClient() {
        return client;
    }

    @Override
    public void setClient(final Cliente client) {
        this.client = client;
    }

    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

    @Override
    public ItemPedido getItemPedido() {
        return itemPedido;
    }

    @Override
    public void setItemPedido(final ItemPedido itemPedido) {
        this.itemPedido = itemPedido;
    }

    @Override
    public List<ItemPedido> getItens() {
        return itens;
    }

    @Override
    public void setItens(final List<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public void setOrderSale(Pedido orderSale) {
        this.orderSale = orderSale;
    }

    @Override
    public void getParams() {
        this.mView.getParams();

    }

    @Override
    public Pedido loadSaleOrder(final long keyPedido) {
        return this.mModel.findSalesById(keyPedido);
    }

    @Override
    public Preco getPrice() {
        return price;
    }

    @Override
    public void setPrice(final Preco price) {
        this.price = price;
    }

    @Override
    public Produto getProductSelected() {
        return productSelected;
    }

    @Override
    public void setProductSelected(final Produto productSelected) {
        this.productSelected = productSelected;
    }

    @Override
    public BigDecimal getQtdProdutos() {
        return qtdProdutos;
    }

    @Override
    public void setQtdProdutos(final BigDecimal qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }

    @Override
    public String getTipoRecebimento() {
        return tipoRecebimento;
    }

    @Override
    public void setTipoRecebimento(final String tipoRecebimento) {
        this.tipoRecebimento = tipoRecebimento;
    }

    @Override
    public int getTipoRecebimentoID() {
        return this.mModel.getTipoRecebimentoID();
    }

    @Override
    public BigDecimal getTotalOrderSale() {
        return totalOrderSale;
    }

    @Override
    public void setTotalOrderSale(final BigDecimal totalOrderSale) {
        this.totalOrderSale = totalOrderSale;
    }

    @Override
    public BigDecimal getTotalProductValue() {
        return getQtdProdutos().multiply(new BigDecimal(getPrice().getValor()));
    }

    @Override
    public void setTotalProductValue(final BigDecimal totalProductValue) {
        this.totalProductValue = totalProductValue;
    }

    @Override
    public Unidade getUnitSelected() {
        return unitSelected;
    }

    @Override
    public void setUnitSelected(final Unidade unitSelected) {
        this.unitSelected = unitSelected;
    }

    @Override
    public List<Produto> loadAllProducts() {
        return this.mModel.getAllProducts();
    }

    @Override
    public ArrayList<String> loadAllProductsByName(final List<Produto> produtos) {
        return this.mModel.loadAllProductsByName(produtos);
    }

    @Override
    public ArrayList<String> loadAllProductsID(final List<Produto> products) {
        return this.mModel.loadAllProductsID(products);
    }

    @Override
    public List<Unidade> loadAllUnitys() {
        return this.mModel.getAllUnitys();
    }

    @Override
    public ArrayList<String> loadAllUnitysToString(final List<Unidade> unitys) {
        return this.mModel.convertUnitysToString(unitys);
    }

    @Override
    public void loadDetailsSale() throws Throwable {
        this.mView.loadDetailsSale();
    }

    @Override
    public Preco loadPriceByProduct() {
        return this.mModel.loadPriceByProduct();
    }

    @Override
    public Preco loadPriceOfUnityByProduct(final String unityID) {

        return this.mModel.loadPriceOfUnityByProduct(unityID);
    }

    @Override
    public Produto loadProductById(final long id) {
        return this.mModel.findProductById(id);
    }

    @Override
    public Produto loadProductByName(final String productName) {
        return this.mModel.findProductByName(productName);
    }

    @Override
    public TipoRecebimento loadTipoRecebimentoById() throws Throwable {
        return this.mModel.findTipoRecebimentoById();
    }

    @Override
    public List<TipoRecebimento> loadTipoRecebimentosByClient(final Cliente client) {
        return this.mModel.findTipoRecebimentosByCliente(client);
    }

    @Override
    public Unidade loadUnityByProduct() {
        return this.mModel.findUnityByProduct();
    }

    @Override
    public void refreshSelectedProductViews() {
        this.mView.refreshSelectedProductViews();
    }

    @Override
    public void saveOrderSale() throws ParseException {

        Pedido pedido = new Pedido();
        pedido.setDataPedido(DateUtils.formatDateDDMMYYYY(new java.util.Date(System.currentTimeMillis())));
        //Agora setar o id definitivo do item do pedido

        getItens().forEach(item->this.mModel.addItemPedido(item));
        SessionManager session = new SessionManager(getContext());
        pedido.setCodigoFuncionario(session.getUserID());
        pedido.setCodigoCliente(getClient().getId());
        pedido.setValorTotal(calculeTotalOrderSale());
        pedido.setTipoRecebimento(getTipoRecebimentoID());

        //Salva o pedido e retorna o id salvo
        long idSaleOrder = this.mModel.saveOrderSale(pedido);

        //Seta a chave composta do item pedido com o id da venda
        getItens().forEach(item->
                item.getChavesItemPedido().setIdVenda(idSaleOrder)
        );
        pedido.setItens(Pedido.dtoToRealList(getItens()));
        this.mModel.copyOrUpdateSaleOrder(pedido);
    }

    @Override
    public void setSpinnerProductSelected() {
        this.mView.setSpinnerProductSelected();
    }

    @Override
    public void setSpinnerUnityPatternOfProductSelected() {
        this.mView.setSpinnerUnityPatternOfProductSelected();
    }

    @Override
    public void showOnUpdateDialog(final int position) {
        this.mView.showOnUpdateDialog(position);
    }

    @Override
    public void updateActCodigoProduto() {
        this.mView.updateActCodigoProduto();
    }

    @Override
    public void updateRecyclerItens() {
        this.mView.updateRecyclerItens();
    }

    @Override
    public void updateSaleOrder(final Pedido orderSale) {
        this.mModel.copyOrUpdateSaleOrder(orderSale);

    }

    @Override
    public void updateTxtAmountOrderSale() {
        this.mView.updateTxtAmountOrderSale();
    }

    @Override
    public void updateTxtAmountProducts() {
        this.mView.updateTxtAmountProducts();
    }

    @Override
    public boolean validateFieldsBeforeAddItem() {
        return this.mView.validateFieldsBeforeAddItem();
    }
}
