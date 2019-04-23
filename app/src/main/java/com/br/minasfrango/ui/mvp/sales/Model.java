package com.br.minasfrango.ui.mvp.sales;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.PrecoIDDAO;
import com.br.minasfrango.data.dao.PriceDAO;
import com.br.minasfrango.data.dao.ProductDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.PrecoID;
import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.data.pojo.TipoRecebimento;
import com.br.minasfrango.data.pojo.Unidade;
import java.util.ArrayList;
import java.util.List;

public class Model implements ISalesMVP.IModel {


    ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedido.class);

    ClientDAO mClientDAO = ClientDAO.getInstace(Cliente.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(Pedido.class);

    PrecoIDDAO mPrecoIDDAO = PrecoIDDAO.getInstance(PrecoID.class);

    PriceDAO mPriceDAO = PriceDAO.getInstace(Preco.class);

    ProductDAO mProductDAO = ProductDAO.getInstace(Produto.class);

    ProductDAO produtoDAO = ProductDAO.getInstace(Produto.class);

    PedidoDAO saleDAO = PedidoDAO.getInstace(Pedido.class);

    TipoRecebimentoDAO tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

    UnidadeDAO unidadeDAO = UnidadeDAO.getInstace(Unidade.class);

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
    public ArrayList<String> convertUnitysToString(final List<Unidade> unitys) {
        ArrayList<String> unityNames = new ArrayList<>();
        unitys.forEach(item->{
            String[] unitID = item.getId().split("-");
            unityNames.add(unitID[0]);
        });
        return unityNames;
    }


    @Override
    public void copyOrUpdateSaleOrder(final Pedido pedido) {
        mPedidoDAO.copyOrUpdate(pedido);
    }

    @Override
    public Cliente findClientById(final Long id) {
        return this.mClientDAO.findById(id);
    }

    @Override
    public Produto findProductById(final long id) {
        return mProductDAO.findById(id);
    }

    @Override
    public Produto findProductByName(final String productName) {
        return mProductDAO.findByName(productName);
    }

    @Override
    public Pedido findSalesById(final Long id) {
        return saleDAO.findById(id);
    }

    @Override
    public TipoRecebimento findTipoRecebimentoById() throws Throwable {
        return this.tipoRecebimentoDAO.findById(mPresenter.getOrderSale().getTipoRecebimento());
    }

    @Override
    public List<TipoRecebimento> findTipoRecebimentosByCliente(final Cliente client) {
        return tipoRecebimentoDAO.findTipoRecebimentoByCliente(client);
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
    public ArrayList<String> loadAllProductsID(final List<Produto> products) {

        ArrayList<String> productIds = new ArrayList<>();
        products.forEach(item->productIds.add(String.valueOf(item.getId())));
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
    public long saveOrderSale(final Pedido saleOrderToSave) {
        return this.mPedidoDAO.addPedido(saleOrderToSave);
    }
}
