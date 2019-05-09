package com.br.minasfrango.ui.mvp.venda;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.ui.mvp.venda.ISalesMVP.IView;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.ImpressoraUtil;
import com.br.minasfrango.util.SessionManager;
import com.location.aravind.getlocation.GeoLocator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements ISalesMVP.IPresenter {

    Cliente cliente;

    ItemPedido itemPedido;

    List<ItemPedido> itens = new ArrayList<>();

    ISalesMVP.IModel mModel;

    ISalesMVP.IView mView;

    Pedido pedido;

    Preco preco;

    Produto produtoSelecionado;

    BigDecimal qtdProdutos;

    String tipoRecebimento;

    BigDecimal valorTotalPedido;

    BigDecimal valorTotalProduto;

    Unidade unidadeSelecionada;

    ImpressoraUtil mImpressoraUtil;

    int bicos;

    private AlertDialog mAlertDialog;

    public Presenter(final IView view) {
        mView = view;
        mModel = new Model(this);
        mImpressoraUtil = new ImpressoraUtil((Activity) getContext());
    }

    @Override
    public void atualizarTxtValorTotalPoduto() {
        this.mView.updateTxtAmountProducts();
    }

    @Override
    public void desabilitarBtnSalvar() {
        this.mView.desabilitarCliqueBotaoSalvarVenda();
    }

    @Override
    public void exibirBotaoFotografar() {
        this.mView.exibirBotaoFotografar();
    }

    @Override
    public void atualizarTxtValorTotalVenda() {
        this.mView.updateTxtAmountOrderSale();
    }

    @Override
    public int getBicos() {
        return bicos;
    }

    @Override
    public void atualizarViewPrecoPosFoto() {
        this.mView.atulizarViewPrecoPosFoto();
    }

    @Override
    public void error(final String msg) {
        this.mView.error(msg);
    }

    @Override
    public void esperarPorConexao() {
        if (this.mImpressoraUtil.esperarPorConexao()) {
            this.mView.exibirBotaoImprimir();
        }
    }

    @Override
    public void atualizarViewsDoProdutoSelecionado() {
        this.mView.atualizarViewsDoProdutoSelecionado();
    }

    @Override
    public void setBicos(final int bicos) {
        this.bicos = bicos;
    }

    @Override
    public void dissmis() {
        this.mView.dissmiss();
    }

    @Override
    public void atualizarPedido(final Pedido orderSale) {
        this.mModel.copyOrUpdateSaleOrder(orderSale);
    }

    @Override
    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    @Override
    public Pedido buscarVendaPorId(final long keyPedido) {
        return this.mModel.buscarVendaPorId(keyPedido);
    }

    @Override
    public void carregarDadosDaVenda() throws Throwable {
        this.mView.carregarDadosDaVenda();
    }

    @Override
    public void setAlertDialog(final AlertDialog alertDialog) {
        mAlertDialog = alertDialog;
    }

    @Override
    public Double calcularValorTotalVenda() {
        return getItens().stream().mapToDouble(ItemPedido::getValorTotal).sum();
    }

    @Override
    public ArrayList<String> converterTipoRecebimentoEmString(
            final List<TipoRecebimento> tiposRecebimentos) {
        return this.mModel.converterTipoRecebimentoEmString(tiposRecebimentos);
    }

    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

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
    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public void getParams() {
        this.mView.getParams();
    }

    @Override
    public void fecharConexaoAtiva() {
        this.mImpressoraUtil.fecharConexaoAtiva();
    }

    @Override
    public ItemPedido getItemPedido() {
        return itemPedido;
    }

    @Override
    public void setCliente(final Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public Preco getPreco() {
        return preco;
    }

    @Override
    public void setPreco(final Preco preco) {
        this.preco = preco;
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
    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    @Override
    public void setProdutoSelecionado(final Produto produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }

    @Override
    public BigDecimal getValorTotalPedido() {
        return valorTotalPedido;
    }

    @Override
    public void setValorTotalPedido(final BigDecimal valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    @Override
    public Unidade getUnidadeSelecionada() {
        return unidadeSelecionada;
    }

    @Override
    public void setUnidadeSelecionada(final Unidade unidadeSelecionada) {
        this.unidadeSelecionada = unidadeSelecionada;
    }

    @Override
    public BigDecimal getValorTotalProduto() {
        return getQtdProdutos().multiply(new BigDecimal(getPreco().getValor()));
    }

    @Override
    public void setValorTotalProduto(final BigDecimal valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }

    @Override
    public void imprimirComprovante() {

        this.mImpressoraUtil.imprimirComprovantePedido(getPedido(), getCliente());
    }

    @Override
    public boolean localizacaoHabilitada() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm =
                    (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            int mode =
                    Settings.Secure.getInt(
                            getContext().getContentResolver(),
                            Settings.Secure.LOCATION_MODE,
                            Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    @Override
    public ArrayList<String> obterIDProdutos(final List<Produto> products) {
        return this.mModel.pesquisarNomeDeTodosProdutos(products);
    }

    @Override
    public ArrayList<String> obterNomeDeTodosOsProdutos(final List<Produto> produtoS) {
        return this.mModel.obterNomeDeTodosOsProdutos(produtoS);
    }

    @Override
    public List<Unidade> obterTodasUnidades() {
        return this.mModel.getAllUnitys();
    }

    @Override
    public ArrayList<String> obterTodasUnidadesEmString(final List<Unidade> unitys) {
        return this.mModel.converterUnidadesEmString(unitys);
    }

    @Override
    public List<Produto> obterTodosProdutos() {
        return this.mModel.getAllProducts();
    }

    @Override
    public Cliente pesquisarClientePorId(final long codigoCliente) {
        return this.mModel.pesquisarClientePorID(codigoCliente);
    }

    @Override
    public Preco pesquisarPrecoDaUnidadePorProduto(final String unityID) {

        return this.mModel.carregarPrecoDaUnidadePorProduto(unityID);
    }

    @Override
    public Preco pesquisarPrecoPorProduto() {
        return this.mModel.carregarPrecoPorProduto();
    }

    @Override
    public Produto pesquisarProdutoPorId(final long id) {
        return this.mModel.pesquisarProdutoPorID(id);
    }

    @Override
    public Produto pesquisarProdutoPorNome(final String productName) {
        return this.mModel.pesquisarProdutoPorNome(productName);
    }

    @Override
    public TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable {
        return this.mModel.pesquisarTipoRecebimentoPorID();
    }

    @Override
    public void setSpinnerProductSelected() {
        this.mView.setSpinnerProductSelected();
    }

    @Override
    public List<TipoRecebimento> pesquisarTipoRecebimentosPorCliente(final Cliente client) {
        return this.mModel.findTipoRecebimentosByCliente(client);
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
    public Unidade pesquisarUnidadePorProduto() {
        return this.mModel.pesquisarUnidadePorProduto();
    }

    @Override
    public void salvarVenda() throws ParseException {

        // Pega a geolocalizacao
        GeoLocator geoLocator = new GeoLocator(getContext(), (Activity) getContext());

        Pedido pedido = new Pedido();
        pedido.setDataPedido(
                DateUtils.formatarDateddMMyyyyhhmm(new java.util.Date(System.currentTimeMillis())));
        // Agora setar o id definitivo do item do pedido

        getItens().forEach(item->this.mModel.adicionarItemDoPedido(item));
        SessionManager session = new SessionManager(getContext());
        pedido.setCodigoFuncionario(session.getUserID());
        pedido.setCodigoCliente(getCliente().getId());
        pedido.setValorTotal(calcularValorTotalVenda());
        pedido.setTipoRecebimento(getTipoRecebimentoID());

        PedidoORM pedidoORM = new PedidoORM(pedido);

        // Salva o pedidoORM e retorna o id salvo
        long idSaleOrder = this.mModel.salvarPedido(pedidoORM);

        // Seta a chave composta do item pedidoORM com o id da venda
        getItens().forEach(item->item.getChavesItemPedido().setIdVenda(idSaleOrder));
        pedido.setItens(getItens());
        pedido.setId(pedidoORM.getId());

        this.mModel.copyOrUpdateSaleOrder(pedido);
    }

    @Override
    public void setOrderSale(final Pedido orderSale) {
        this.pedido = orderSale;
    }

    @Override
    public void setSpinnerUnidadePadraoDoProdutoSelecionado() {
        this.mView.setSpinnerUnidadePadraoDoProdutoSelecionado();
    }

    @Override
    public boolean validarCamposAntesDeAdicionarItem() {
        return this.mView.validarCamposAntesDeAdicionarItem();
    }
}
