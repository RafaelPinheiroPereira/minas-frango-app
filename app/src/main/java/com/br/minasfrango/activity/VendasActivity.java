package com.br.minasfrango.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
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
import com.br.minasfrango.R;
import com.br.minasfrango.adapter.ItemPedidoAdapter;
import com.br.minasfrango.dao.ClienteDAO;
import com.br.minasfrango.dao.ItemPedidoDAO;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.dao.PrecoDAO;
import com.br.minasfrango.dao.ProdutoDAO;
import com.br.minasfrango.dao.TipoRecebimentoDAO;
import com.br.minasfrango.dao.UnidadeDAO;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.ItemPedidoID;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;
import com.br.minasfrango.model.TipoRecebimento;
import com.br.minasfrango.util.CurrencyEditText;
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

    RecyclerView itemPedidoRecyclerView;

    EditText qtdEditText, qtdBicos;

    CurrencyEditText precoEditText;

    Spinner uniSpinner;

    Paint p = new Paint();

    Button addButton;

    Spinner formaPagamentoSpinner;

    AutoCompleteTextView codigoProdutoAutoCompleteTextView;

    Spinner spnProduto;

    Button confirmarPedidoButton;

    TextView valorTotalProduto, valorTotalTextView;

    LinearLayoutManager layoutManager;

    ProdutoDAO produtoDAO;

    ClienteDAO mClienteDAO;

    PrecoDAO precoDAO;

    UnidadeDAO unidadeDAO;

    TipoRecebimentoDAO tipoRecebimentoDAO;

    PedidoDAO pedidoDAO;

    ItemPedidoDAO itemPedidoDAO;

    ArrayList<Produto> produtos;

    ArrayAdapter<String> adapterFormaPagamento;


    ArrayList<ItemPedido> itens;

    ItemPedidoAdapter adapterItemPedidoAdapter;

    ArrayList<String> codigosProdutos;

    ArrayAdapter<String> adaptadorCodigoProduto;

    ArrayAdapter<String> adaptadorDescricaoProduto;

    ArrayList<String> descricoesProdutos;

    ArrayAdapter<String> adapterUnidade;

    List<TipoRecebimento> tiposRecebimento;

    ArrayList<String> strTiposRecebimentos = new ArrayList<String>();

    Produto produtoSelecionado;

    Preco preco;


    String formaPagamento;

    Pedido pedidoRealizado;

    double quantidadeAtual = 0;

    double valorTotal = 0;

    private String codigoUnidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        produtoDAO = ProdutoDAO.getInstace();
        pedidoDAO = PedidoDAO.getInstace();
        mClienteDAO = ClienteDAO.getInstace();
        tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();
        unidadeDAO = UnidadeDAO.getInstace();
        precoDAO = PrecoDAO.getInstace();
        itemPedidoDAO = ItemPedidoDAO.getInstace();

        getParams();
        initView();

        //Formas de Pagamento

        tiposRecebimento = tipoRecebimentoDAO.findTipoRecebimentoByCliente(cliente);
        populateSpinnerTipoRecebimento();

        adapterFormaPagamento = new ArrayAdapter<String>(VendasActivity.this, android.R.layout.simple_list_item_1,
                strTiposRecebimentos);
        formaPagamentoSpinner.setAdapter(adapterFormaPagamento);

        //Edição de Pedido
        if (pedidoRealizado != null) {
            itens = new ArrayList<ItemPedido>();

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

            adapterItemPedidoAdapter = new ItemPedidoAdapter(this, itens, valorTotalProduto);
            itemPedidoRecyclerView.setLayoutManager(layoutManager);
            itemPedidoRecyclerView.setAdapter(adapterItemPedidoAdapter);
            initSwipe();
            valorTotalProduto.setText(
                    NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(pedidoRealizado.getValorTotal()));
            for (int i = 0; i < strTiposRecebimentos.size(); i++) {
                for (int j = 0; j < tiposRecebimento.size(); j++) {
                    if (strTiposRecebimentos.get(i).equals(tiposRecebimento.get(j).getNome())) {
                        formaPagamentoSpinner.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            //Carrega a lista de itens vazio

            itens = new ArrayList<ItemPedido>();
            adapterItemPedidoAdapter = new ItemPedidoAdapter(this, itens, valorTotalProduto);
            itemPedidoRecyclerView.setLayoutManager(layoutManager);
            itemPedidoRecyclerView.setAdapter(adapterItemPedidoAdapter);
            initSwipe();
            formaPagamentoSpinner.setSelection(0);
            formaPagamento = adapterFormaPagamento.getItem(0);
        }

        produtos = produtoDAO.carregaProdutos();
        carregaCodigoProdutos(produtos);
        carregaDescricoesProdutos(produtos);

        adaptadorCodigoProduto = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                codigosProdutos);
        codigoProdutoAutoCompleteTextView.setAdapter(adaptadorCodigoProduto);
        codigoProdutoAutoCompleteTextView.setOnItemClickListener(this);

        adaptadorDescricaoProduto = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, descricoesProdutos);
        spnProduto.setAdapter(adaptadorDescricaoProduto);

        spnProduto.setOnItemSelectedListener(this);

        layoutManager = new LinearLayoutManager(VendasActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        codigoProdutoAutoCompleteTextView.setOnClickListener(this);
        confirmarPedidoButton.setOnClickListener(this);

        uniSpinner.setSelection(0);
        uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codigoUnidade = (String) parent.getAdapter().getItem(position);
                preco = precoDAO.carregaPrecoUnidadeProduto(produtoSelecionado, codigoUnidade);
                precoEditText.setText(String.valueOf(preco.getValor()));
                quantidadeAtual = Integer.parseInt(qtdEditText.getText().toString());
                if (quantidadeAtual >= 0) {
                    Double preco = precoEditText.getCurrencyDouble();
                    valorTotal = Integer.parseInt(qtdEditText.getText().toString()) * preco;
                    valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
                }
                valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!qtdEditText.getText().toString().isEmpty()
                        && Integer.parseInt(qtdEditText.getText().toString()) > 0) {
                    String qtd = qtdEditText.getText().toString();
                    if (valorTotal >= 0 && Integer.parseInt(qtd) >= 1) {
                        int i = 0;
                        while (i < itens.size()) {

                            if (itens.get(i).getDescricao().equals(produtoSelecionado.getNome())) {
                                break;
                            } else {
                                i++;
                            }

                        }
                        if (i == itens.size()) {
                            //
                            addItemPedido(produtoSelecionado, Integer.parseInt(qtdEditText.getText().toString()));
                        } else {
                            Toast.makeText(VendasActivity.this,
                                    "O PRODUTO JÁ EXISTE NA LISTA", Toast.LENGTH_LONG)
                                    .show();
                        }


                    } else {
                        qtdEditText.setError("QUANTIDADE INVÁLIDA");
                        Toast.makeText(VendasActivity.this, "A QUANTIDADE MÍNIMA É DE 1 ITEM",
                                Toast.LENGTH_LONG).show();

                    }
                } else {
                    qtdEditText.setError("QUANTIDADE INVÁLIDA");
                }
            }
        });

        formaPagamentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formaPagamento = (String) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinnerTipoRecebimento() {
        strTiposRecebimentos.add("FORMAS DE PAGAMENTO");
        for (TipoRecebimento tipoRecebimento : tiposRecebimento) {
            strTiposRecebimentos.add(tipoRecebimento.getNome());
        }
    }


    private void addItemPedido(Produto produto, int quantidade) {

        ItemPedido itemPedido = new ItemPedido();

        ItemPedidoID itemPedidoID = new ItemPedidoID();

        itemPedidoID.setIdProduto(produto.getId());
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

        itemPedido.setQuantidade(quantidade);

        Double number = precoEditText.getCurrencyDouble();
        itemPedido.setValorUnitario(number.doubleValue());
        itemPedido.setDescricao(produto.getNome());

        try {
            itemPedido.setValorTotal(
                    NumberFormat.getCurrencyInstance().parse(valorTotalTextView.getText().toString()).doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        itemPedido.setChavesItemPedido(itemPedidoID);
        long id = itemPedidoDAO.addItemPedido(itemPedido);
        itemPedido.setId(id);

        adapterItemPedidoAdapter.addListItem(itemPedido, (itens.size() + 1));

        valorTotalProduto
                .setText("TOTAL: " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                        .format(calculaValorTotalPedido(itens)));
    }

    private void carregaDescricoesProdutos(ArrayList<Produto> produtos) {
        descricoesProdutos = new ArrayList<String>();
        for (Produto produto : produtos) {
            descricoesProdutos.add(produto.getNome());
        }
    }

    private void carregaCodigoProdutos(ArrayList<Produto> produtos) {

        codigosProdutos = new ArrayList<String>();

        for (Produto produto : produtos) {
            codigosProdutos.add(String.valueOf(produto.getId()));
        }


    }

    private void initView() {

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("VENDAS");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        codigoProdutoAutoCompleteTextView = findViewById(R.id.auto_txt_codigo);
        spnProduto = findViewById(R.id.spn_produtos);

        itemPedidoRecyclerView = findViewById(R.id.card_recycler_itens);
        confirmarPedidoButton = findViewById(R.id.btn_confirmar_pedido);

        valorTotalTextView = findViewById(R.id.txt_valor_total);

        qtdBicos = findViewById(R.id.edtBicos);

        qtdEditText = findViewById(R.id.edtQTD);
        precoEditText = findViewById(R.id.txt_preco_unitario);

        formaPagamentoSpinner = findViewById(R.id.spinner_forma_pagamento);

        uniSpinner = findViewById(R.id.spinner_unidade);
        addButton = findViewById(R.id.button_add);

        valorTotalProduto = findViewById(R.id.txt_valor_total_pedido);
        valorTotalProduto.setText("VALOR TOTAL: 00,00");

        layoutManager = new LinearLayoutManager(VendasActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


    }

    private void getParams() {
        Bundle args = getIntent().getExtras();
        long idPedido = args.getLong("keyPedido");
        if (idPedido == 0) {
            pedidoRealizado = null;
            cliente = (Cliente) args.getSerializable("keyCliente");
        } else {

            pedidoRealizado = pedidoDAO.findById(idPedido);
            cliente = mClienteDAO.findById(pedidoRealizado.getCodigoCliente());


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

                codigoProdutoAutoCompleteTextView.setText(String.valueOf(aux.getId()));

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

    private void setaSpinner(String codigoProduto) {
        qtdEditText.requestFocus();
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String codigoProduto = (String) parent.getAdapter().getItem(position);
        setaSpinner(codigoProduto);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_confirmar_pedido:

                if (itens.size() > 0 && !formaPagamento.equals("FORMAS DE PAGAMENTO")) {
                    if (pedidoRealizado != null) {
                        updatePedido();
                    } else {
                        confirmaPedido();
                    }

                } else if (formaPagamento.equals("FORMAS DE PAGAMENTO")) {
                    Toast.makeText(VendasActivity.this, "Forma de Pagamento Inválida!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VendasActivity.this, "Por favor, adicione no minimo um item na lista!",
                            Toast.LENGTH_LONG).show();
                }

                break;


            case R.id.auto_txt_codigo:

                break;


        }
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
            pedido.setCodigoFuncionario(session.getMatricula());
            pedido.setItens(itensPedidos);
            pedido.setCodigoCliente(cliente.getId());
            pedido.setValorTotal(calculaValorTotalPedido(itens));

            pedido.setTipoRecebimento(codigoFormaPagamento);
            pedidoDAO = PedidoDAO.getInstace();

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

    private double calculaValorTotalPedido(ArrayList<ItemPedido> itens) {
        Double valorTotalPedido = 0.0;
        for (int i = 0; i < itens.size(); i++) {

            valorTotalPedido += itens.get(i).getValorTotal();


        }
        return valorTotalPedido;
    }

    private void showADDProduto() {

        qtdEditText.setText("1");
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
        //adapter da unidade
        uniSpinner.setAdapter(adapterUnidade);
        codigoUnidade = adapterUnidade.getItem(0);
        //unidade clicada seta o preco

        valorTotalTextView.setText(
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(quantidadeAtual * preco.getValor()));

        // valorTotal = quantidadeAtual * preco.getValor();
        precoEditText.setText(String.valueOf(preco.getValor()));
        qtdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                quantidadeAtual = Double.parseDouble(s.toString().equals("") ? "0" : s.toString());
                if (quantidadeAtual > 0) {
                    valorTotal = quantidadeAtual * Double
                            .parseDouble(precoEditText.getText().toString().replace(",", "."));

                    valorTotalTextView
                            .setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorTotal));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                valorTotalTextView
                        .setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorTotal));
            }
        });

        precoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (quantidadeAtual > 0) {
                    Double preco = precoEditText.getCurrencyDouble();
                    valorTotal = Integer.parseInt(qtdEditText.getText().toString()) * preco;
                    valorTotalTextView
                            .setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorTotal));
                }
                valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void alertaConfirmacao(Context ctx, String strMsg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Cancelamento de Pedido");
        builder.setMessage(strMsg);

        String positiveText = "SIM";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        finish();
                        dialog.dismiss();
                        //edtConfirmaEmail.setText("");

                        return;
                    }
                });

        String negativeText = "NÃO";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
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
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterItemPedidoAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterItemPedidoAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
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
                    valorTotalProduto.setText(
                            "TOTALIZADOR" + NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                                    .format(calculaValorTotalPedido(itens)));

                } else {

                    updateDialog(itens.get(position), position, valorTotalTextView, valorTotalProduto);
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
        itemTouchHelper.attachToRecyclerView(itemPedidoRecyclerView);
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
        final EditText quantidadeEditText;
        final CurrencyEditText precoEditText;
        Spinner unidadeSpinner = null;
        Button cancelarEdicaoButton, salvaEdicaoButton;
        ArrayList<String> unidades = new ArrayList<String>();
        ArrayList<String> todasUnidades = new ArrayList<String>();
        String unidadePadrao = "";

        idProdutoTextView = dialogView.findViewById(R.id.txt_codigo_produto);
        descricaoProdutoTextView = dialogView.findViewById(R.id.txt_descricao_produto);
        quantidadeEditText = dialogView.findViewById(R.id.edt_quantidade_produto);
        precoEditText = dialogView.findViewById(R.id.edt_preco);
        unidadeSpinner = dialogView.findViewById(R.id.spinner_unidade_produto);
        cancelarEdicaoButton = dialogView.findViewById(R.id.button_cancelar_edicao);
        salvaEdicaoButton = dialogView.findViewById(R.id.button_confirmar_edicao);

        idProdutoTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
        descricaoProdutoTextView.setText(itemPedido.getDescricao());
        precoEditText.setText(String.valueOf(itemPedido.getValorUnitario()));
        quantidadeEditText.setText(String.valueOf(itemPedido.getQuantidade()));

        precoDAO = PrecoDAO.getInstace();
        final Produto produto = new Produto();
        produto.setId((long) itemPedido.getChavesItemPedido().getIdProduto());
        produto.setNome(itemPedido.getDescricao());
        unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produto);
        unidades.add(unidadePadrao);
        todasUnidades = unidadeDAO.carregaUnidadesProduto(produto);
        unidades.addAll(todasUnidades);
        adapterUnidade = new ArrayAdapter<String>(VendasActivity.this, android.R.layout.simple_spinner_item,
                unidades);
        //adapter da unidade
        unidadeSpinner.setAdapter(adapterUnidade);
        codigoUnidade = adapterUnidade.getItem(0);

        unidadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codigoUnidade = (String) parent.getAdapter().getItem(position);
                preco = precoDAO.carregaPrecoUnidadeProduto(produto, codigoUnidade);
                precoEditText.setText(String.valueOf(preco.getValor()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        cancelarEdicaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                adapterItemPedidoAdapter.notifyDataSetChanged();
            }
        });
        salvaEdicaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o edit no item pedido dao e chama edit na lista de itens

                itemPedido.setQuantidade(Integer.parseInt(quantidadeEditText.getText().toString()));
                itemPedido.setValorUnitario(precoEditText.getCurrencyDouble());

                valorTotal = 0.0;
                int quantidadeAtual = Integer.parseInt(quantidadeEditText.getText().toString());
                if (quantidadeAtual >= 0) {
                    Double preco = precoEditText.getCurrencyDouble();
                    valorTotal = (quantidadeAtual * preco);
                    txtValorTotal
                            .setText(NumberFormat.getCurrencyInstance().format(valorTotal));

                }

                itemPedido.setValorTotal(valorTotal);
                itemPedido.setDescricao(produto.getNome());
                itemPedido.getChavesItemPedido().setIdUnidade(codigoUnidade);
                itemPedidoDAO.updateItem(itemPedido);

                adapterItemPedidoAdapter.updateListItem(itemPedido, position);
                valorTotalProduto.setText(
                        "Totalizador" + NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                                .format(calculaValorTotalPedido(itens)));

                b.dismiss();
                Toast.makeText(VendasActivity.this, "Item alterado com sucesso", Toast.LENGTH_LONG).show();
            }
        });
        b.show();

    }


}

