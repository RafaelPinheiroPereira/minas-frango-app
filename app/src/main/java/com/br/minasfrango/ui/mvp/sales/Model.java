package com.br.minasfrango.ui.mvp.sales;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.PrecoIDDAO;
import com.br.minasfrango.data.dao.PriceDAO;
import com.br.minasfrango.data.dao.ProductDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ItemPedidoORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.PrecoIDORM;
import com.br.minasfrango.data.realm.PrecoORM;
import com.br.minasfrango.data.realm.ProdutoORM;
import com.br.minasfrango.data.realm.UnidadeORM;
import java.util.ArrayList;
import java.util.List;

public class Model implements ISalesMVP.IModel {

    ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedidoORM.class);

    ClientDAO mClientDAO = ClientDAO.getInstace(ClienteORM.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(PedidoORM.class);

    PrecoIDDAO mPrecoIDDAO = PrecoIDDAO.getInstance(PrecoIDORM.class);

    PriceDAO mPriceDAO = PriceDAO.getInstace(PrecoORM.class);

    ProductDAO mProductDAO = ProductDAO.getInstace(ProdutoORM.class);

    ProductDAO produtoDAO = ProductDAO.getInstace(ProdutoORM.class);

    PedidoDAO saleDAO = PedidoDAO.getInstace(PedidoORM.class);

    TipoRecebimentoDAO tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

    UnidadeDAO unidadeDAO = UnidadeDAO.getInstace(UnidadeORM.class);

    private com.br.minasfrango.ui.mvp.sales.Presenter mPresenter;

    public Model(com.br.minasfrango.ui.mvp.sales.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public long addItemPedido(final ItemPedido item) {
        return this.itemPedidoDAO.addItemPedido(item);
    }

    @Override
    public ArrayList<String> convertTipoRecebimentoInString(final List<TipoRecebimento> tiposRecebimentos) {
        ArrayList<String> strTiposRecebimentos = new ArrayList<>();
        strTiposRecebimentos.add("Formas de Pagamento");
        tiposRecebimentos.forEach(item->strTiposRecebimentos.add(item.getNome()));
        return strTiposRecebimentos;
    }

    @Override
    public Pedido buscarVendaPorId(final Long id) {
        PedidoORM pedidoORM = saleDAO.findById(id);
        Pedido pedido = new Pedido(pedidoORM);
        pedido.setItens(Pedido.converterListItemPedidoRealmParaModel(pedidoORM));
        return pedido;
    }

    @Override
    public ArrayList<String> convertUnitysToString(final List<Unidade> unidades) {
        ArrayList<String> unityNames = new ArrayList<>();
        unidades.forEach(item->{
            String[] unitID = item.getId().split("-");
            unityNames.add(unitID[0]);
        });
        return unityNames;
    }

    @Override
    public void copyOrUpdateSaleOrder(final Pedido pedido) {
        PedidoORM pedidoORM = new PedidoORM(pedido);
        pedidoORM.setItens(PedidoORM.converterListModelParaListRealm(pedido.getItens()));
        mPedidoDAO.copyOrUpdate(pedidoORM);
        mPresenter.setOrdemVenda(pedido);
    }

    @Override
    public Cliente findClientById(final Long id) {
        return new Cliente(this.mClientDAO.findById(id));
    }

    @Override
    public Produto findProductByName(final String productName) {
        return mProductDAO.findByName(productName);
    }

    @Override
    public Produto findProductById(final long id) {
        return new Produto(mProductDAO.findById(id));
    }

    @Override
    public TipoRecebimento findTipoRecebimentoById() throws Throwable {
        return this.tipoRecebimentoDAO.findById(mPresenter.getOrdemVenda().getTipoRecebimento());
    }

    @Override
    public List<TipoRecebimento> findTipoRecebimentosByCliente(final Cliente cliente) {
        return tipoRecebimentoDAO.findTipoRecebimentoByCliente(cliente);
    }

    @Override
    public Unidade findUnityByProduct() {
        return this.unidadeDAO.findUnityPattenByProduct(mPresenter.getProductSelected());
    }

    @Override
    public List<Produto> getAllProducts() {
        return this.produtoDAO.getAll();
    }

    @Override
    public List<Unidade> getAllUnitys() {
        return this.unidadeDAO.getAll();
    }

    @Override
    public int getTipoRecebimentoID() {
        return this.tipoRecebimentoDAO.codigoFormaPagamento(mPresenter.getTipoRecebimento());
    }

    @Override
    public ArrayList<String> loadAllProductsByName(final List<Produto> produtos) {
        ArrayList<String> productNames = new ArrayList<String>();
        produtos.forEach(item->productNames.add(item.getNome()));
        return productNames;
    }

    @Override
    public ArrayList<String> loadAllProductsID(final List<Produto> produtos) {

        ArrayList<String> productIds = new ArrayList<>();
        produtos.forEach(item->productIds.add(String.valueOf(item.getId())));
        return productIds;

    }

    @Override
    public Preco loadPriceByProduct() {
        return this.mPriceDAO.carregaPrecoProduto(mPresenter.getProductSelected());
    }

    @Override
    public Preco loadPriceOfUnityByProduct(final String unityID) {
        long id = mPrecoIDDAO.findPrecoIDByUnidadeAndProdutoAndCliente(mPresenter.getProductSelected(), unityID,
                mPresenter.getClient());
        return this.mPriceDAO.findPriceByPriceID(id);
    }

    @Override
    public long salvarPedido(final PedidoORM saleOrderToSave) {
        return this.mPedidoDAO.addPedido(saleOrderToSave);
    }
}
