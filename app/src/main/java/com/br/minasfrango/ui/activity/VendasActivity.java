package com.br.minasfrango.ui.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.adapter.ItemPedidoAdapter;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.PriceDAO;
import com.br.minasfrango.data.dao.ProductoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.ItemPedidoID;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.data.pojo.TipoRecebimento;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.FormatacaoMoeda;
import com.br.minasfrango.util.SessionManager;
import io.realm.RealmList;
import io.realm.exceptions.RealmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VendasActivity extends AppCompatActivity
        implements RecyclerViewOnClickListenerHack, AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener, View.OnClickListener {

    Toolbar mToolbar;

    Cliente cliente;

    SearchView searchView;

    RecyclerView rcItemPedido;

    EditText edtQuantidade, edtBicos;

    CurrencyEditText cetPrecoUnitario;

    Spinner spnUnidade;

    Paint p = new Paint();

    Button btnAddItem;

    Spinner spnFormaPagamento, spnProduto;

    AutoCompleteTextView actCodigoProduto;

    Button btnConfirmar;

    TextView txtValorTotalProduto, txtValorTotal;

    LinearLayoutManager layoutManager;

    ProductoDAO produtoDAO;

    ClientDAO mClientDAO;

    PriceDAO precoDAO;

    UnidadeDAO unidadeDAO;

    TipoRecebimentoDAO tipoRecebimentoDAO;

    PedidoDAO pedidoDAO;

    ItemPedidoDAO itemPedidoDAO;

    ArrayList<Produto> produtos;

    ArrayAdapter<String> adapterFormaPagamento;

    ArrayList<ItemPedido> itens = new ArrayList<>();

    List<ItemPedido> itensTemp = new ArrayList<>();


    ItemPedidoAdapter adapterItemPedidoAdapter;


    ArrayAdapter<String> adaptadorCodigoProduto;

    ArrayAdapter<String> adaptadorDescricaoProduto;

    ArrayList<String> descricoesProdutos;

    ArrayAdapter<String> adapterUnidade;


    Produto produtoSelecionado;

    Preco preco;


    String formaPagamento;

    Pedido pedidoRealizado;

    double quantidadeAtual = 0;

    double valorTotal = 0;

    private String codigoUnidade;

    private TipoRecebimentoDAO mTipoRecebimentoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        getParams();
        initViews();


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Instanciando os DAOS necessarios
        produtoDAO = ProductoDAO.getInstace(Produto.class);
        pedidoDAO = PedidoDAO.getInstace(Pedido.class);
        mClientDAO = ClientDAO.getInstace(Cliente.class);
        tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();
        unidadeDAO = UnidadeDAO.getInstace();
        precoDAO = PriceDAO.getInstace(Preco.class);
        itemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedido.class);
        mTipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

        //Seta todos os adaptadores necessarios
        setAdapters();

        //Todos os listeners da Activity
        listeners();

        //Edição de Pedido
        if (pedidoRealizado != null) {
            try {
                loadDetailsSale();
            } catch (Throwable throwable) {

                throwable.printStackTrace();
            }
        } else {
            //Carrega a lista de itens vazio
            initSwipe();
        }




    }

    private void listeners() {
        actCodigoProduto.setOnItemClickListener(this);
        spnProduto.setOnItemSelectedListener(this);
        actCodigoProduto.setOnClickListener(this);
        btnConfirmar.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
        spnFormaPagamento.setOnItemSelectedListener(this);

        spnFormaPagamento.setSelection(0);
        formaPagamento = adapterFormaPagamento.getItem(0);

        spnUnidade.setSelection(0);

        spnFormaPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formaPagamento = (String) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codigoUnidade = (String) parent.getAdapter().getItem(position);
                preco = precoDAO.carregaPrecoUnidadeProduto(produtoSelecionado, codigoUnidade);
                cetPrecoUnitario.setText(String.valueOf(preco.getValor()));
                quantidadeAtual = Integer.parseInt(edtQuantidade.getText().toString());
                if (quantidadeAtual >= 0) {
                    Double preco = cetPrecoUnitario.getCurrencyDouble();
                    valorTotal = Integer.parseInt(edtQuantidade.getText().toString()) * preco;
                    txtValorTotal.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
                }
                txtValorTotal.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void setAdapters() {

        adapterFormaPagamento = new ArrayAdapter<String>(VendasActivity.this, android.R.layout.simple_list_item_1,
                loadTipoRecebimentos());

        produtos = produtoDAO.getAll();

        ArrayList<String> codigosProdutos = loadProductById(produtos);
        adaptadorCodigoProduto = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                codigosProdutos);

        loadProductByDescription(produtos);

        adaptadorDescricaoProduto = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, descricoesProdutos);
        spnProduto.setAdapter(adaptadorDescricaoProduto);

        spnFormaPagamento.setAdapter(adapterFormaPagamento);
        adapterItemPedidoAdapter = new ItemPedidoAdapter(this, itensTemp, txtValorTotalProduto);

        actCodigoProduto.setAdapter(adaptadorCodigoProduto);

        rcItemPedido.setAdapter(adapterItemPedidoAdapter);


    }

    private void loadDetailsSale() throws Throwable {

        TipoRecebimento tipoRecebimento = mTipoRecebimentoDAO.findById(pedidoRealizado.getTipoRecebimento());
        int position = adapterFormaPagamento.getPosition(tipoRecebimento.getNome());
        spnFormaPagamento.setSelection(position);

        for (ItemPedido aux : pedidoRealizado.realmListToList()) {
            ItemPedido itensAux = new ItemPedido();
            itensAux.setId(aux.getId());
            itensAux.setQuantidade(aux.getQuantidade());
            itensAux.setValorUnitario(aux.getValorUnitario());
            itensAux.setChavesItemPedido(aux.getChavesItemPedido());
            itensAux.setDescricao(aux.getDescricao());
            itensAux.setValorTotal(aux.getValorTotal());
            itens.add(itensAux);


        }
        itensTemp.addAll(itens);

        adapterItemPedidoAdapter.notifyDataSetChanged();

        initSwipe();
        txtValorTotalProduto.setText(
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(pedidoRealizado.getValorTotal()));

    }

    private ArrayList<String> loadTipoRecebimentos() {
        ArrayList<String> strTiposRecebimentos = new ArrayList<String>();
        List<TipoRecebimento> tiposRecebimento = tipoRecebimentoDAO.findTipoRecebimentoByCliente(cliente);
        strTiposRecebimentos.add("FORMAS DE PAGAMENTO");
        for (TipoRecebimento tipoRecebimento : tiposRecebimento) {
            strTiposRecebimentos.add(tipoRecebimento.getNome());
        }
        return strTiposRecebimentos;

    }

    private void addItem(ItemPedido itemPedido) {

        adapterItemPedidoAdapter.addListItem(itemPedido, (itensTemp.size() + 1));
        txtValorTotalProduto
                .setText("TOTAL: " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                        .format(calculaValorTotalPedido(itensTemp)));

    }

    private void loadProductByDescription(ArrayList<Produto> produtos) {
        descricoesProdutos = new ArrayList<String>();
        for (Produto produto : produtos) {
            descricoesProdutos.add(produto.getNome());
        }

    }

    private ArrayList<String> loadProductById(ArrayList<Produto> produtos) {

        ArrayList<String> codigosProdutos = new ArrayList<>();

        for (Produto produto : produtos) {
            codigosProdutos.add(String.valueOf(produto.getId()));
        }
        return codigosProdutos;

    }

    private void initViews() {
        //Toolbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("VENDAS");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actCodigoProduto = findViewById(R.id.auto_txt_codigo);
        spnProduto = findViewById(R.id.spn_produtos);
        rcItemPedido = findViewById(R.id.card_recycler_itens);
        btnConfirmar = findViewById(R.id.btn_confirmar_pedido);
        txtValorTotal = findViewById(R.id.txt_valor_total);
        edtBicos = findViewById(R.id.edtBicos);
        edtQuantidade = findViewById(R.id.edtQTD);
        cetPrecoUnitario = findViewById(R.id.txt_preco_unitario);
        spnFormaPagamento = findViewById(R.id.spinner_forma_pagamento);
        spnUnidade = findViewById(R.id.spinner_unidade);
        btnAddItem = findViewById(R.id.button_add);

        txtValorTotalProduto = findViewById(R.id.txt_valor_total_pedido);
        txtValorTotalProduto.setText("VALOR TOTAL: 00,00");

        layoutManager = new LinearLayoutManager(VendasActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        //Configurando os recycle views
        rcItemPedido.setLayoutManager(layoutManager);


    }

    private void confirmaPedido() {

        try {
            Double valorTotalPedido;

            Pedido pedido = new Pedido();

            RealmList<ItemPedido> itensPedidos = new RealmList<ItemPedido>();

            int codigoFormaPagamento = tipoRecebimentoDAO.codigoFormaPagamento(formaPagamento);

            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            String strData = formatador.format(new Date(System.currentTimeMillis()));
            try {
                pedido.setDataPedido(formatador.parse(strData));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < itens.size(); i++) {
                ItemPedido aux = new ItemPedido();
                ItemPedidoID itemPedidoID = itens.get(i).getChavesItemPedido();
                itemPedidoID.setDataVenda(pedido.getDataPedido());
                aux.setDescricao(itens.get(i).getDescricao());
                aux.setValorTotal(itens.get(i).getValorTotal());
                aux.setId(itens.get(i).getId());
                aux.setChavesItemPedido(itemPedidoID);
                aux.setQuantidade(itens.get(i).getQuantidade());
                aux.setValorUnitario(itens.get(i).getValorUnitario());
                itensPedidos.add(aux);
            }
            SessionManager session = new SessionManager(getApplicationContext());
            if (session.checkLogin()) {
                finish();
            }
            pedido.setCodigoFuncionario(session.getUserID());
            pedido.setItens(itensPedidos);
            pedido.setCodigoCliente(cliente.getId());
            pedido.setValorTotal(calculaValorTotalPedido(itens));

            pedido.setTipoRecebimento(codigoFormaPagamento);
            pedidoDAO = PedidoDAO.getInstace(Pedido.class);

            long idVenda = pedidoDAO.addPedido(pedido);

            for (ItemPedido itemPedido : itensPedidos) {
                ItemPedidoID itemPedidoID = itemPedido.getChavesItemPedido();
                itemPedidoID.setIdVenda(idVenda);
                itemPedido.setChavesItemPedido(itemPedidoID);

            }

            pedido.setItens(itensPedidos);

            pedidoDAO.updatePedido(pedido);

            //alert de confimacao

            Toast.makeText(VendasActivity.this, "PEDIDO REALIZADO COM SUCESSO!", Toast.LENGTH_LONG).show();
            NavUtils.navigateUpFromSameTask(this);
        } catch (RealmException ex) {

            System.out.println("Erro ao inserir:" + ex.getMessage());
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, int position) {
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String descricaoProduto = (String) parent.getAdapter().getItem(position);
        for (Produto aux : produtos) {
            if (aux.getNome().equals(descricaoProduto)) {
                actCodigoProduto.setText(String.valueOf(aux.getId()));
                for (int i = 0; i < descricoesProdutos.size(); i++) {
                    if (descricoesProdutos.get(i).equals(aux.getNome())) {
                        produtoSelecionado = aux;
                        showADDProduto();
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_confirmar_pedido:

                if (itensTemp.size() > 0 && !formaPagamento.equals("FORMAS DE PAGAMENTO")) {
                    if (pedidoRealizado != null) {
                        updatePedido();
                    } else {
                        confirmaPedido();
                    }

                } else if (formaPagamento.equals("FORMAS DE PAGAMENTO")) {
                    Toast.makeText(VendasActivity.this, "FORMA DE PAGAMENTO INVÁLIDA!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VendasActivity.this, " NO MÍNIMO UM ITEM DEVE SER ADICIONADO!",
                            Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.auto_txt_codigo:

                break;

            case R.id.button_add:

                if (validateFieldsForAddItem(edtQuantidade,edtBicos)) {

                    ItemPedido itemPedido = getItemPedido();
                    ItemPedidoID itemPedidoID = getItemPedidoID();

                    itemPedido.setChavesItemPedido(itemPedidoID);

                    if (!itensTemp.contains(itemPedido)) {
                        addItem(itemPedido);
                    } else {
                        Toast.makeText(VendasActivity.this,
                                "O PRODUTO JÁ EXISTE NA LISTA", Toast.LENGTH_LONG)
                                .show();
                    }

                    //vai somente na hora de salvar
       /* itemPedido.setChavesItemPedido(itemPedidoID);
        long id = itemPedidoDAO.addItemPedido(itemPedido);
        itemPedido.setId(id);*/
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_vendas_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_vendas_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterItemPedidoAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterItemPedidoAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Quando chama o auto complete deve setar no spinner de descricao do produto
        String codigoProduto = (String) parent.getAdapter().getItem(position);
        spnProduto.setSelection(adaptadorDescricaoProduto.getPosition(codigoProduto));

        //setaSpinner(codigoProduto);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @NonNull
    private ItemPedidoID getItemPedidoID() {
        ItemPedidoID itemPedidoID = new ItemPedidoID();

        itemPedidoID.setIdProduto(produtoSelecionado.getId());
        itemPedidoID.setIdUnidade(codigoUnidade);

        SimpleDateFormat formatador = new SimpleDateFormat("yyyy/MM/dd");
        String strData = formatador.format(new Date(System.currentTimeMillis()));
        try {
            itemPedidoID.setDataVenda(formatador.parse(strData));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Nao sei o significado
        itemPedidoID.setVendaMae("N");
        itemPedidoID.setNucleoCodigo(1);
        itemPedidoID.setTipoVenda("?");
        return itemPedidoID;
    }

    @NonNull
    private ItemPedido getItemPedido() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString()));
        itemPedido.setValorUnitario(cetPrecoUnitario.getCurrencyDouble());
        itemPedido.setDescricao(produtoSelecionado.getNome());
        itemPedido.setBicos(Integer.parseInt(edtBicos.getText().toString()));
        try {
            itemPedido.setValorTotal(
                    NumberFormat.getCurrencyInstance().parse(txtValorTotal.getText().toString())
                            .doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return itemPedido;
    }

    private boolean validateFieldsForAddItem(final EditText edtQTD,final EditText edtBicos) {

        if (TextUtils.isEmpty(edtQTD.getText().toString())) {
            edtQTD.setError("Quantidade Obrigatória!");
            edtQTD.requestFocus();
            return false;
        }
        if (Integer.parseInt(edtQTD.getText().toString()) <= 0) {
            edtQTD.setError("Quantidade mínima de 1 item!");
            edtQTD.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtBicos.getText().toString())) {
            edtBicos.setError("Quantidade de Bicos Obrigatória!");
            edtBicos.requestFocus();
            return false;
        }
        if (Integer.parseInt(edtBicos.getText().toString()) <= 0) {
            edtBicos.setError("Quantidade mínima de 1 bico!");
            edtBicos.requestFocus();
            return false;
        }
        return true;
    }

    private void updatePedido() {
        RealmList<ItemPedido> itensPedidos = new RealmList<ItemPedido>();
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido aux = new ItemPedido();
            aux.setDescricao(itens.get(i).getDescricao());
            aux.setValorTotal(itens.get(i).getValorTotal());
            aux.setId(itens.get(i).getId());
            aux.setChavesItemPedido(itens.get(i).getChavesItemPedido());
            aux.setQuantidade(itens.get(i).getQuantidade());
            aux.setValorUnitario(itens.get(i).getValorUnitario());
            itensPedidos.add(aux);
        }
        for (ItemPedido itemPedido : itensPedidos) {
            ItemPedidoID itemPedidoID = itemPedido.getChavesItemPedido();
            itemPedidoID.setIdVenda(pedidoRealizado.getId());
            itemPedido.setChavesItemPedido(itemPedidoID);

        }
        int codigoFormaPagamento = tipoRecebimentoDAO.codigoFormaPagamento(formaPagamento);
        pedidoRealizado.setValorTotal(calculaValorTotalPedido(itens));
        pedidoRealizado.setItens(itensPedidos);
        pedidoRealizado.setTipoRecebimento(codigoFormaPagamento);
        pedidoDAO.updatePedido(pedidoRealizado);
        //alert de confimacao

        Toast.makeText(VendasActivity.this, "Pedido Alterado com Sucesso!", Toast.LENGTH_LONG).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void getParams() {
        Bundle args = getIntent().getExtras();
        long idPedido = args.getLong("keyPedido");
        if (idPedido == 0) {
            pedidoRealizado = null;
            cliente = (Cliente) args.getSerializable("keyCliente");
        } else {

            pedidoRealizado = pedidoDAO.findById(idPedido);
            cliente = mClientDAO.findById("id",pedidoRealizado.getCodigoCliente());


        }

    }

    @SuppressLint("NewApi")
    private double calculaValorTotalPedido(List<ItemPedido> itens) {
        Double valorTotalPedido = 0.0;
        for (int i = 0; i < itens.size(); i++) {

            valorTotalPedido += itens.get(i).getValorTotal();


        }
        //return itens.stream().mapToDouble(ItemPedido::getValorTotal).sum();
        return valorTotalPedido;
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    adapterItemPedidoAdapter.removeListItem(position);
                    txtValorTotalProduto.setText(
                            "TOTAL: " + FormatacaoMoeda.convertDoubleToString(calculaValorTotalPedido(itensTemp)));

                } else {

                    updateDialog(itensTemp.get(position), position, txtValorTotal, txtValorTotalProduto);
//
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                    float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_edit_white_36);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width,
                                (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width,
                                (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_white_36dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width,
                                (float) itemView.getTop() + width, (float) itemView.getRight() - width,
                                (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rcItemPedido);
    }

    private void setaSpinner(String codigoProduto) {

        for (Produto aux : produtos) {
            if (aux.getId() == Integer.parseInt(codigoProduto)) {

                for (int i = 0; i < descricoesProdutos.size(); i++) {
                    if (descricoesProdutos.get(i).equals(aux.getNome())) {
                        spnProduto.setSelection(i);
                        produtoSelecionado = aux;
                        showADDProduto();
                        break;
                    }
                }
            }
        }


    }

    private void showADDProduto() {

        edtQuantidade.setText("1");
        quantidadeAtual = 1;

        preco = precoDAO.carregaPrecoProduto(produtoSelecionado);
        ArrayList<String> unidades = new ArrayList<String>();
        ArrayList<String> todasUnidades = new ArrayList<String>();
        String unidadePadrao = "";
        unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produtoSelecionado);
        unidades.add(unidadePadrao);
        todasUnidades = unidadeDAO.carregaUnidadesProduto(produtoSelecionado);
        unidades.addAll(todasUnidades);
        adapterUnidade = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unidades);
        //mClientAdapter da unidade
        spnUnidade.setAdapter(adapterUnidade);
        codigoUnidade = adapterUnidade.getItem(0);
        //unidade clicada seta o preco

        txtValorTotal.setText(
                FormatacaoMoeda.convertDoubleToString(quantidadeAtual * preco.getValor()));

        // valorTotal = quantidadeAtual * preco.getValor();
        cetPrecoUnitario.setText(String.valueOf(preco.getValor()));
        edtQuantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                txtValorTotal
                        .setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorTotal));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                quantidadeAtual = Double.parseDouble(s.toString().equals("") ? "0" : s.toString());
                if (quantidadeAtual > 0) {
                    valorTotal = quantidadeAtual *
                            cetPrecoUnitario.getCurrencyDouble();

                    txtValorTotal
                            .setText(FormatacaoMoeda.convertDoubleToString(valorTotal));

                }
            }
        });

        cetPrecoUnitario.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (quantidadeAtual > 0) {
                    Double preco = cetPrecoUnitario.getCurrencyDouble();
                    valorTotal = Integer.parseInt(edtQuantidade.getText().toString()) * preco;
                    txtValorTotal
                            .setText(FormatacaoMoeda.convertDoubleToString(valorTotal));
                }
                txtValorTotal.setText(FormatacaoMoeda.convertDoubleToString(valorTotal));
            }
        });

    }

    private void updateDialog(final ItemPedido itemPedido, final int position, final TextView txtValorTotal,
            final TextView valorTotalProduto) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VendasActivity.this);
        LayoutInflater inflater = (LayoutInflater) VendasActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_edit_item_pedido, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("DADOS DO ITEM");

        TextView idProdutoTextView, descricaoProdutoTextView;
        final EditText edtQuantidade;
        final CurrencyEditText cetPreco;
        Spinner spnUnidade ;
        Button btnCancelar, btnSave;

        idProdutoTextView = dialogView.findViewById(R.id.txt_codigo_produto);
        descricaoProdutoTextView = dialogView.findViewById(R.id.txt_descricao_produto);
        edtQuantidade = dialogView.findViewById(R.id.edt_quantidade_produto);
        cetPreco = dialogView.findViewById(R.id.edt_preco);
        spnUnidade = dialogView.findViewById(R.id.spinner_unidade_produto);
        btnCancelar = dialogView.findViewById(R.id.button_cancelar_edicao);
        btnSave = dialogView.findViewById(R.id.button_confirmar_edicao);


        ArrayList<String> unidades = new ArrayList<String>();
        ArrayList<String> todasUnidades = new ArrayList<String>();
        String unidadePadrao = "";


        idProdutoTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
        descricaoProdutoTextView.setText(itemPedido.getDescricao());

        cetPreco.setText(NumberFormat.getCurrencyInstance().format(itemPedido.getValorUnitario()));
        edtQuantidade.setText(String.valueOf(itemPedido.getQuantidade()));

        precoDAO = PriceDAO.getInstace(Preco.class);
        final Produto produto = new Produto();
        produto.setId((long) itemPedido.getChavesItemPedido().getIdProduto());
        produto.setNome(itemPedido.getDescricao());
        unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produto);
        unidades.add(unidadePadrao);
        todasUnidades = unidadeDAO.carregaUnidadesProduto(produto);
        unidades.addAll(todasUnidades);
        adapterUnidade = new ArrayAdapter<String>(VendasActivity.this, android.R.layout.simple_spinner_item,
                unidades);
        //mClientAdapter da unidade
        spnUnidade.setAdapter(adapterUnidade);

        spnUnidade.setSelection(adapterUnidade.getPosition(itemPedido.getChavesItemPedido().getIdUnidade()));


        spnUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codigoUnidade = (String) parent.getAdapter().getItem(position);
                preco = precoDAO.carregaPrecoUnidadeProduto(produto, codigoUnidade);
                //Preciso descomentar assim que conseguir pegar o preco pela de acordo com a unidade
              //  cetPreco.setText(String.valueOf(preco.getValor()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                adapterItemPedidoAdapter.notifyDataSetChanged();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o edit no item pedido dao e chama edit na lista de itens

                itemPedido.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString()));
                itemPedido.setValorUnitario(cetPreco.getCurrencyDouble());

                valorTotal = 0.0;
                int quantidadeAtual = Integer.parseInt(edtQuantidade.getText().toString());
                if (quantidadeAtual >= 0) {
                    Double preco = cetPreco.getCurrencyDouble();
                    valorTotal = (quantidadeAtual * preco);
                    txtValorTotal
                            .setText(FormatacaoMoeda.convertDoubleToString(valorTotal));

                }

                itemPedido.setValorTotal(valorTotal);
                itemPedido.setDescricao(produto.getNome());
                itemPedido.getChavesItemPedido().setIdUnidade(codigoUnidade);

                adapterItemPedidoAdapter.updateListItem(itemPedido, position);
                valorTotalProduto.setText(
                        "TOTAL: " + FormatacaoMoeda.convertDoubleToString(calculaValorTotalPedido(itensTemp)));

                b.dismiss();
                Toast.makeText(VendasActivity.this, "Item alterado com sucesso", Toast.LENGTH_LONG).show();
            }
        });
        b.show();

    }


}

