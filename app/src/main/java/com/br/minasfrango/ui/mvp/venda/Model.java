package com.br.minasfrango.ui.mvp.venda;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.ItemPedidoIDDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.PrecoDAO;
import com.br.minasfrango.data.dao.PrecoIDDAO;
import com.br.minasfrango.data.dao.ProdutoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.ItemPedidoID;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
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

    ItemPedidoIDDAO mItemPedidoIDDAO=ItemPedidoIDDAO.getInstace(ItemPedidoIDORM.class);

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(PedidoORM.class);

    PrecoIDDAO mPrecoIDDAO = PrecoIDDAO.getInstance(PrecoIDORM.class);

    PrecoDAO mPrecoDAO = PrecoDAO.getInstace(PrecoORM.class);

    ProdutoDAO mProdutoDAO = ProdutoDAO.getInstace(ProdutoORM.class);

    ProdutoDAO produtoDAO = ProdutoDAO.getInstace(ProdutoORM.class);

    PedidoDAO saleDAO = PedidoDAO.getInstace(PedidoORM.class);

    EmpresaDAO mEmpresaDAO= EmpresaDAO.getInstace(EmpresaORM.class);



    UnidadeDAO unidadeDAO = UnidadeDAO.getInstace(UnidadeORM.class);

    FuncionarioDAO mFuncionarioDAO=FuncionarioDAO.getInstace(FuncionarioORM.class);


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
        this.mItemPedidoIDDAO.alterar(new ItemPedidoIDORM(chavesItemPedido));

    }

    @Override
    public void atualizarItemPedido(final ItemPedido item) {
        this.itemPedidoDAO.updateItem(new ItemPedidoORM(item));
    }

    @Override
    public Preco carregarPrecoPorProduto() {
        return this.mPrecoDAO.carregaPrecoProduto(mPresenter.getProdutoSelecionado());
    }

    @Override
    public Preco carregarPrecoUnidadePorProduto(final String unityID) {
        String id = mPrecoIDDAO.findPrecoIDByUnidadeAndProdutoAndCliente(mPresenter.getProdutoSelecionado(), unityID,
                mPresenter.getCliente());
        if (id.equals("")){
            String padrao = mPrecoIDDAO.findPrecoIDByUnidadeAndProdutoPadrao(mPresenter.getProdutoSelecionado(), unityID);
            return this.mPrecoDAO.findPriceByPriceID(padrao);
        }
        else{



        return this.mPrecoDAO.findPriceByPriceID(id);}
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
        mPedidoDAO.alterar(pedidoORM);
        mPresenter.setPedido(pedido);
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
        return new Cliente(this.mClienteDAO.findById(id));
    }

    @Override
    public long pesquisarCodigoMaximoDeVendaDoFuncionario(final int idUsuario) {

        FuncionarioORM funcionarioORM=mFuncionarioDAO.findById(Long.valueOf(idUsuario));
        Funcionario funcionarioPequisado= new Funcionario(funcionarioORM);
        return funcionarioPequisado.getMaxIdVenda();
    }

    @Override
    public Empresa pesquisarEmpresaRegistrada() {
        return mEmpresaDAO.pesquisarEmpresaRegistradaNoDispositivo();
    }

    @Override
    public Produto pesquisarProdutoPorId(final long id) {
        return new Produto(mProdutoDAO.findById(id));
    }

    @Override
    public Produto pesquisarProdutoPorNome(final String productName) {
        return mProdutoDAO.findByName(productName);
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
    public Unidade pesquisarUnidadePorProduto() {
        return this.unidadeDAO.findUnityPattenByProduct(mPresenter.getProdutoSelecionado());
    }

    @Override
    public Pedido pesquisarVendaPorId(final Long id) {
        PedidoORM pedidoORM = saleDAO.findById(id);
        Pedido pedido = new Pedido(pedidoORM);
        pedido.setItens(PedidoORM.converterListRealmParaModel(pedidoORM.getItens()));
        return pedido;
    }

    @Override
    public long salvarPedido(final PedidoORM saleOrderToSave) {
        return this.mPedidoDAO.addPedido(saleOrderToSave);
    }
    @Override
    public void atualizarIdMaximoDeVenda(final long idFuncionario, final long idVendaMaxima) {
        Funcionario funcionarioPesquisado= new Funcionario(this.mFuncionarioDAO.findById(idFuncionario));
        funcionarioPesquisado.setMaxIdVenda(idVendaMaxima);
        this.mFuncionarioDAO.alterar(new FuncionarioORM(funcionarioPesquisado));
    }
}