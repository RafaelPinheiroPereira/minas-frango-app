package com.br.minasfrango.ui.mvp.venda;

import android.app.Activity;
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
import com.br.minasfrango.ui.mvp.venda.IVendaMVP.IView;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.ImpressoraUtil;
import com.br.minasfrango.util.ControleSessao;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements IVendaMVP.IPresenter {

    Cliente cliente;

    ItemPedido itemPedido;

    List<ItemPedido> itens = new ArrayList<>();

    IVendaMVP.IModel mModel;

    IVendaMVP.IView mView;

    Pedido pedido;

    Preco preco;

    Produto produtoSelecionado;

    BigDecimal quantidadeProdutos;

    String tipoRecebimento;

    BigDecimal totalDaVenda;

    BigDecimal totalProductValue;

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
    public void atulizarViewPrecoPosFoto() {
        this.mView.atualizarViewPrecoPosFoto();
    }

    @Override
    public void  desabilitarBotaoSalvarPedido() {
        this.mView.desabilitarCliqueBotaoSalvarVenda();
    }

    @Override
    public void exibirBotaoFotografar() {
        this.mView.exibirBotaoFotografar();
    }

    @Override
    public Double calcularTotalDaVenda() {
        return getItens().stream().mapToDouble(ItemPedido::getValorTotal).sum();
    }

    @Override
    public int getBicos() {
        return bicos;
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
    public ArrayList<String> converterTipoRecebimentoEmString(
            final List<TipoRecebimento> tiposRecebimentos) {
        return this.mModel.convertTipoRecebimentoInString(tiposRecebimentos);
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
    public Pedido pesquisarVendaPorId(final long keyPedido) {
        return this.mModel.pesquisarVendaPorId(keyPedido);
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
    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public void setCliente(final Cliente cliente) {
        this.cliente = cliente;
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
        return this.itens;
    }

    @Override
    public void setItens(final List<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public Pedido getPedido() {
        return this.pedido;
    }

    @Override
    public void getParametros() {
        this.mView.getParametros();
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
    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    @Override
    public void setProdutoSelecionado(final Produto produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }

    @Override
    public BigDecimal getQuantidadeProdutos() {
        return quantidadeProdutos;
    }

    @Override
    public void setQuantidadeProdutos(final BigDecimal quantidadeProdutos) {
        this.quantidadeProdutos = quantidadeProdutos;
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
    public BigDecimal getTotalDaVenda() {
        return totalDaVenda;
    }

    @Override
    public void setTotalDaVenda(final BigDecimal totalDaVenda) {
        this.totalDaVenda = totalDaVenda;
    }

    @Override
    public BigDecimal getValorTotalProduto() {
        return getQuantidadeProdutos().multiply(new BigDecimal(getPreco().getValor()));
    }

    @Override
    public void setTotalProductValue(final BigDecimal totalProductValue) {
        this.totalProductValue = totalProductValue;
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
    public List<Produto> carregarProdutos() {
        return this.mModel.getAllProducts();
    }

    @Override
    public void imprimirComprovante() {

        this.mImpressoraUtil.imprimirComprovantePedido(getPedido(), getCliente());
    }

    @Override
    public ArrayList<String> carregarIdProdutos(final List<Produto> products) {
        return this.mModel.carregarProdutoPorId(products);
    }

    @Override
    public List<Unidade> carregarUnidades() {
        return this.mModel.getAllUnitys();
    }

    @Override
    public ArrayList<String> carregarUnidadesEmString(final List<Unidade> unitys) {
        return this.mModel.converterUnidadeParaString(unitys);
    }

    @Override
    public ArrayList<String> carregarProdutosPorNome(final List<Produto> produtoS) {
        return this.mModel.carregarProdutoPorNome(produtoS);
    }

    @Override
    public Preco pesquisarPrecoPorProduto() {
        return this.mModel.carregarPrecoPorProduto();
    }

    @Override
    public Preco pesquisarPrecoDaUnidadePorProduto(final String unityID) {

        return this.mModel.carregarPrecoUnidadePorProduto(unityID);
    }

    @Override
    public Produto pesquisarProdutoPorId(final long id) {
        return this.mModel.pesquisarProdutoPorId(id);
    }

    @Override
    public Produto pesquisarProdutoPorNome(final String productName) {
        return this.mModel.pesquisarProdutoPorNome(productName);
    }

    @Override
    public Cliente pesquisarClientePorId(final long codigoCliente) {
        return this.mModel.pesquisarClientePorId(codigoCliente);
    }

    @Override
    public List<TipoRecebimento> carregarTipoRecebimentoPorCliente(final Cliente client) {
        return this.mModel.pesquisarTipoRecebimentosPorCliente(client);
    }

    @Override
    public Unidade carregarUnidadesPorProduto() {
        return this.mModel.pesquisarUnidadePorProduto();
    }

    @Override
    public TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable {
        return this.mModel.pesquisarTipoRecebimentoPorId();
    }

    @Override
    public void atualizarProdutoSelecionadoView() {
        this.mView.atualizarViewsDoProdutoSelecionado();
    }

    @Override
    public void setSpinnerProdutoSelecionado() {
        this.mView.setSpinnerProductSelected();
    }

    @Override
    public void setSpinnerUnidadePadraoProdutoSelecionado() {
        this.mView.inicializarSpinnerUnidadesComUnidadePadraoDoProduto();
    }

    @Override
    public void exibirDialogAlterarItemPedido(final int position) {
        this.mView.exibirDialogAlterarItemPedido(position);
    }

    @Override
    public void atualizarActCodigoProduto() {
        this.mView.updateActCodigoProduto();
    }

    @Override
    public void atualizarRecyclerItens() {
        this.mView.updateRecyclerItens();
    }

    @Override
    public void salvarVenda() throws ParseException {

        Pedido pedido = new Pedido();
        pedido.setDataPedido(
                DateUtils.formatarDateParaddMMyyyyhhmm(new java.util.Date(System.currentTimeMillis())));
        // Agora setar o id definitivo do item do pedido

        getItens().forEach(item->{
            this.mModel.criarChaveItemPedido(item.getChavesItemPedido());
            this.mModel.addItemPedido(item);
        });
        ControleSessao controleSessao = new ControleSessao(getContext());
        pedido.setCodigoFuncionario(controleSessao.getIdUsuario());
        pedido.setCodigoCliente(getCliente().getId());
        pedido.setValorTotal(calcularTotalDaVenda());
        pedido.setTipoRecebimento(getTipoRecebimentoID());

        PedidoORM pedidoORM = new PedidoORM(pedido);

        // Salva o pedidoORM e retorna o id salvo
        long idSaleOrder = this.mModel.salvarPedido(pedidoORM);

        // Seta a chave composta do item pedidoORM com o id da venda
        getItens().forEach(item->{item.getChavesItemPedido().setIdVenda(idSaleOrder);
                                  this.mModel.atualizarChaveItemPedido(item.getChavesItemPedido());


                                  ;
        });
        pedido.setItens(getItens());
        pedido.setId(pedidoORM.getId());

        this.mModel.copyOrUpdateSaleOrder(pedido);
    }



    @Override
    public void updateTxtAmountOrderSale() {
        this.mView.atualizarTextViewTotalVenda();
    }

    @Override
    public void updateTxtAmountProducts() {
        this.mView.atualizarTextViewValorTotalProduto();
    }

    @Override
    public boolean validarCamposAntesDeAdicionarItem() {
        return this.mView.validarCamposAntesDeAdicionarItem();
    }
}
