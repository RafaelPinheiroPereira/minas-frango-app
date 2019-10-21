package com.br.minasfrango.ui.mvp.venda;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.ItemPedidoIDDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.PrecoIDDAO;
import com.br.minasfrango.data.dao.PriceDAO;
import com.br.minasfrango.data.dao.ProductDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.ItemPedidoID;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ItemPedidoIDORM;
import com.br.minasfrango.data.realm.ItemPedidoORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.PrecoIDORM;
import com.br.minasfrango.data.realm.PrecoORM;
import com.br.minasfrango.data.realm.ProdutoORM;
import com.br.minasfrango.data.realm.UnidadeORM;
import java.util.ArrayList;
import java.util.List;

public class Model implements IVendaMVP.IModel {

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

    ItemPedidoIDDAO mItemPedidoIDDAO = ItemPedidoIDDAO.getInstace(ItemPedidoIDORM.class);

    private com.br.minasfrango.ui.mvp.venda.Presenter mPresenter;

    public Model(com.br.minasfrango.ui.mvp.venda.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public long addItemPedido(final ItemPedido item) {
        return this.itemPedidoDAO.addItemPedido(item);
    }

    @Override
    public void atualizarChaveItemPedido(final ItemPedidoID chavesItemPedido) {
        this.mItemPedidoIDDAO.copyOrUpdate(new ItemPedidoIDORM(chavesItemPedido));

    }

    @Override
    public void atualizarItemPedido(final ItemPedido item) {
        this.itemPedidoDAO.updateItem(new ItemPedidoORM(item));
    }

    @Override
    public Preco carregarPrecoPorProduto() {
        return this.mPriceDAO.carregaPrecoProduto(mPresenter.getProdutoSelecionado());
    }

    @Override
    public Preco carregarPrecoUnidadePorProduto(final String unityID) {
        String id = mPrecoIDDAO.findPrecoIDByUnidadeAndProdutoAndCliente(mPresenter.getProdutoSelecionado(), unityID,
                mPresenter.getCliente());
        return this.mPriceDAO.findPriceByPriceID(id);
    }

    @Override
    public ArrayList<String> carregarProdutoPorId(final List<Produto> produtos) {

        ArrayList<String> productIds = new ArrayList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            produtos.forEach(item->productIds.add(String.valueOf(item.getId())));
        } else {
            for (Produto produto : produtos) {
                productIds.add(String.valueOf(produto.getId()));
            }
        }
        return productIds;

    }

    @Override
    public ArrayList<String> carregarProdutoPorNome(final List<Produto> produtos) {
        ArrayList<String> productNames = new ArrayList<String>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            produtos.forEach(item->productNames.add(item.getNome()));
        } else {
            for (Produto produto : produtos) {
                productNames.add(produto.getNome());
            }
        }
        return productNames;
    }

    @Override
    public void copyOrUpdateSaleOrder(final Pedido pedido) {
        PedidoORM pedidoORM = new PedidoORM(pedido);
        pedidoORM.setItens(PedidoORM.converterListModelParaListRealm(pedido.getItens()));
        mPedidoDAO.copyOrUpdate(pedidoORM);
        mPresenter.setPedido(pedido);
    }

    @Override
    public ArrayList<String> convertTipoRecebimentoInString(final List<TipoRecebimento> tiposRecebimentos) {
        ArrayList<String> strTiposRecebimentos = new ArrayList<>();
        strTiposRecebimentos.add("Formas de Pagamento");
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            tiposRecebimentos.forEach(item->strTiposRecebimentos.add(item.getNome()));
        } else {
            for (TipoRecebimento tipoRecebimento : tiposRecebimentos) {
                strTiposRecebimentos.add(tipoRecebimento.getNome());
            }
        }
        return strTiposRecebimentos;
    }

    @Override
    public ArrayList<String> converterUnidadeParaString(final List<Unidade> unidades) {
        ArrayList<String> unityNames = new ArrayList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            unidades.forEach(item->{
                String[] unitID = item.getId().split("-");
                unityNames.add(unitID[0]);
            });
        } else {
            for (Unidade unidade : unidades) {
                String[] unitID = unidade.getId().split("-");
                unityNames.add(unitID[0]);

            }
        }
        return unityNames;
    }

    @Override
    public void criarChaveItemPedido(final ItemPedidoID chavesItemPedido) {
        this.mItemPedidoIDDAO.inserir(chavesItemPedido);

    }

    @Override
    public Cliente pesquisarClientePorId(final Long id) {
        return new Cliente(this.mClientDAO.findById(id));
    }

    @Override
    public Produto pesquisarProdutoPorId(final long id) {
        return new Produto(mProductDAO.findById(id));
    }

    @Override
    public Produto pesquisarProdutoPorNome(final String productName) {
        return mProductDAO.findByName(productName);
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
    public TipoRecebimento pesquisarTipoRecebimentoPorId() throws Throwable {
        return this.tipoRecebimentoDAO.findById(mPresenter.getPedido().getTipoRecebimento());
    }

    @Override
    public List<TipoRecebimento> pesquisarTipoRecebimentosPorCliente(final Cliente cliente) {
        return tipoRecebimentoDAO.findTipoRecebimentoByCliente(cliente);
    }

    @Override
    public Unidade pesquisarUnidadePorProduto() {
        return this.unidadeDAO.findUnityPattenByProduct(mPresenter.getProdutoSelecionado());
    }

    @Override
    public Pedido pesquisarVendaPorId(final Long id) {
        PedidoORM pedidoORM = saleDAO.findById(id);

        Pedido pedido = new Pedido(pedidoORM);
        pedido.setItens(itemPedidoDAO.allItensByPedido(pedidoORM));
        return pedido;
    }

    @Override
    public long salvarPedido(final PedidoORM saleOrderToSave) {
        return this.mPedidoDAO.addPedido(saleOrderToSave);
    }
}