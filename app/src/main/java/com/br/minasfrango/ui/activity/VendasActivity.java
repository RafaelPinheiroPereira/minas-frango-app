package com.br.minasfrango.ui.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.ItemPedidoID;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ItemPedidoAdapter;
import com.br.minasfrango.ui.mvp.venda.ISalesMVP;
import com.br.minasfrango.ui.mvp.venda.ISalesMVP.IView;
import com.br.minasfrango.ui.mvp.venda.Presenter;
import com.br.minasfrango.util.AlertDialogUpdateItemSaleOrder;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.FormatacaoMoeda;
import com.br.minasfrango.util.SessionManager;
import com.hussain_chachuliya.customcamera.CustomCamera;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class VendasActivity extends AppCompatActivity implements IView {

    /**
     * Positions initials of spinners present in to activity
     */
    private static final int INITIAL_POSITION = 0;

    @BindView(R.id.actCodigoProduto)
    AutoCompleteTextView actCodigoProduto;

    @BindView(R.id.btnAddItem)
    Button btnAddItem;

    @BindView(R.id.btnImprimir)
    Button btnImprimir;

    ArrayAdapter<String> adaptadorCodigoProduto;

    @BindView(R.id.cetPrecoUnitario)
    CurrencyEditText cetPrecoUnitario;

    @BindView(R.id.edtQTDBicos)
    EditText edtQTDBicos;

    @BindView(R.id.edtQTDProducts)
    EditText edtQTDProducts;

    ISalesMVP.IPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Paint p = new Paint();

    @BindView(R.id.rcvItens)
    RecyclerView rcvItens;

    SearchView searchView;

    @BindView(R.id.spnFormaPagamento)
    Spinner spnFormaPagamento;

    @BindView(R.id.spnProducts)
    Spinner spnProducts;

    @BindView(R.id.spnUnitys)
    Spinner spnUnitys;

    @BindView(R.id.txtAmountProducts)
    TextView txtAmountProducts;

    @BindView(R.id.txtAmountSale)
    TextView txtAmountSale;

    @BindView(R.id.btnSalvarVenda)
    Button btnSalvarVenda;

    ArrayAdapter<String> adaptadorDescricaoProduto;

    ArrayAdapter<String> adapterFormaPagamento;

    ItemPedidoAdapter adapterItemPedidoAdapter;

    ArrayAdapter<String> adapterUnidade;

    @BindView(R.id.btnFotografar)
    Button btnFotografar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);
        mPresenter = new Presenter(this);
        ButterKnife.bind(this);
        iniciarViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getParams();
        // Seta todos os adaptadores necessarios
        setAdapters();
        // Edição de PedidoActivity
        if (mPresenter.getPedido() != null) {
            try {
                mPresenter.carregarDadosDaVenda();

            } catch (Throwable throwable) {

                throwable.printStackTrace();
            }
        } else {
            // Carrega a lista de itens vazio
            initSwipe();
        }

        edtQTDProducts.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {

                        mPresenter.atualizarTxtValorTotalPoduto();
                    }

                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

        cetPrecoUnitario.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mPresenter.atualizarTxtValorTotalPoduto();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CustomCamera.IMAGE_SAVE_REQUEST) {
            if (resultCode == RESULT_OK) {
                mPresenter.atualizarViewPrecoPosFoto();
                AbstractActivity.showToast(
                        mPresenter.getContext(),
                        "Imagem salva em :" + data.getStringExtra(CustomCamera.IMAGE_PATH));

            } else {
                AbstractActivity.showToast(mPresenter.getContext(), "Imagem não foi salva");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.fecharConexaoAtiva();
    }

    @Override
    public void atualizarViewsDoProdutoSelecionado() {
        mPresenter.setSpinnerProductSelected();
        mPresenter.setSpinnerUnidadePadraoDoProdutoSelecionado();
        mPresenter.setQtdProdutos(new BigDecimal("1"));
        mPresenter.setPreco(mPresenter.pesquisarPrecoPorProduto());
        cetPrecoUnitario.setText(String.valueOf(mPresenter.getPreco().getValor()));
        mPresenter.atualizarTxtValorTotalPoduto();
        updateActCodigoProduto();
    }

    @Override
    public void atulizarViewPrecoPosFoto() {
        cetPrecoUnitario.setText("00,00");
    }

    @OnClick(R.id.btnAddItem)
    public void btnAddItemOnClicked(View view) {

        if (mPresenter.validarCamposAntesDeAdicionarItem()) {
            mPresenter.setItemPedido(getItemPedido());
            ItemPedidoID itemPedidoID = null;
            try {
                itemPedidoID = getItemPedidoID();
            } catch (ParseException e) {
                VendasActivity.this.runOnUiThread(
                        () ->
                                AbstractActivity.showToast(
                                        mPresenter.getContext(),
                                        "Erro Formatação Data PedidoORM :" + e.getMessage()));
            }
            mPresenter.getItemPedido().setChavesItemPedido(itemPedidoID);
            if (!mPresenter.getItens().contains(mPresenter.getItemPedido())) {
                mPresenter.getItens().add(mPresenter.getItemPedido());
                mPresenter.updateRecyclerItens();

            } else {
                AbstractActivity.showToast(
                        mPresenter.getContext(), "O produto já existe na lista!");
            }
        }
    }

    @OnClick(R.id.btnSalvarVenda)
    public void btnConfirmSaleOnClicked(View view) {

        if (!mPresenter.localizacaoHabilitada()) {
            AbstractActivity.showToast(mPresenter.getContext(), "Por favor, habilite o seu gps!");
        } else if ((mPresenter.getItens().size() > 0
                && !mPresenter.getTipoRecebimento().equals("Formas de Pagamento"))
                && (!new SessionManager(mPresenter.getContext())
                .getEnderecoBluetooth()
                .isEmpty())) {
            // Realiza Update do PedidoORM
            if (mPresenter.getPedido() != null) {

                List<ItemPedido> itensDTO = mPresenter.getItens();
                mPresenter.getPedido().setTipoRecebimento(mPresenter.getTipoRecebimentoID());
                mPresenter.getPedido().setItens((itensDTO));
                mPresenter.getPedido().setValorTotal(mPresenter.calcularValorTotalVenda());
                mPresenter.atualizarPedido(mPresenter.getPedido());

            } else {
                // Salva o PedidoORM
                try {
                    mPresenter.salvarVenda();

                } catch (ParseException e) {
                    VendasActivity.this.runOnUiThread(
                            () ->
                                    AbstractActivity.showToast(
                                            mPresenter.getContext(),
                                            "Erro Formatacao Data PedidoORM: " + e.getMessage()));
                }
            }
            mPresenter.esperarPorConexao();

        } else if (mPresenter.getTipoRecebimento().equals("Formas de Pagamento")) {
            AbstractActivity.showToast(mPresenter.getContext(), "Forma de Pagamento Inválida!");
        } else if (new SessionManager(mPresenter.getContext()).getEnderecoBluetooth().isEmpty()) {
            AbstractActivity.showToast(
                    mPresenter.getContext(),
                    "Dispositivo não conectado!\nHabilite no Menu : Configurar Impressora.");
        } else {
            AbstractActivity.showToast(
                    mPresenter.getContext(), " No mímimo um item deve ser adicionado!");
        }
    }

    @Override
    public void dissmiss() {
        mPresenter.getAlertDialog().dismiss();
        mPresenter.updateRecyclerItens();
    }

    @Override
    public void carregarDadosDaVenda() throws Throwable {
        TipoRecebimento tipoRecebimento = mPresenter.pesquisarTipoRecebimentoPorId();
        spnFormaPagamento.setSelection(
                adapterFormaPagamento.getPosition(tipoRecebimento.getNome()));
        mPresenter.setTipoRecebimento(tipoRecebimento.getNome());
        mPresenter.setItens(mPresenter.getPedido().getItens());
        mPresenter.updateRecyclerItens();
        initSwipe();
    }

    @Override
    public void desabilitarCliqueBotaoSalvarVenda() {
        btnSalvarVenda.setClickable(false);
    }

    @Override
    public void error(final String text) {
        runOnUiThread(
                ()->Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show());
    }

    @Override
    public void getParams() {

        if (getIntent().getExtras().getLong("keyPedido") == 0) {
            mPresenter.setOrderSale(null);
            mPresenter.setCliente((Cliente) getIntent().getExtras().getSerializable("keyCliente"));
        } else {

            Pedido pedido =
                    mPresenter.buscarVendaPorId(getIntent().getExtras().getLong("keyPedido"));
            mPresenter.setOrderSale(pedido);
            Cliente cliente =
                    mPresenter.pesquisarClientePorId(mPresenter.getPedido().getCodigoCliente());
            mPresenter.setCliente(cliente);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_vendas_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_vendas_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        // filter recycler view when text is changed
                        // adapterItemPedidoAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // filter recycler view when query submitted
                        //  adapterItemPedidoAdapter.getFilter().filter(query);
                        return false;
                    }
                });
        return true;
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
    public void exibirBotaoFotografar() {
        btnFotografar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnFotografar)
    public void setBtnFotografarOnClicked(View view) {
        CustomCamera.init()
                .with((Activity) mPresenter.getContext())
                .setRequiredMegaPixel(1.5f)
                .setPath(
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/Minas Frango/Imagens Vendas")
                .setImageName(
                        mPresenter.getPedido().getId()
                                + DateUtils.formatarDateddMMyyyyParaString(
                                mPresenter.getPedido().getDataPedido())
                                + mPresenter.getCliente().getNome())
                .start();
    }

    @Override
    public void setSpinnerProductSelected() {
        spnProducts.setSelection(
                adaptadorDescricaoProduto.getPosition(mPresenter.getProdutoSelecionado().getNome()));
    }

    @OnItemSelected(R.id.spnFormaPagamento)
    public void setSpnFormaPagamentoOnSelected(int position) {
        mPresenter.setTipoRecebimento(adapterFormaPagamento.getItem(position));
    }

    @Override
    public void setSpinnerUnidadePadraoDoProdutoSelecionado() {
        Unidade unidadePadrao = mPresenter.pesquisarUnidadePorProduto();
        spnUnitys.setSelection(adapterUnidade.getPosition(unidadePadrao.getId().split("-")[0]));
        mPresenter.setUnidadeSelecionada(unidadePadrao);
    }

    @OnItemSelected(R.id.spnProducts)
    public void setSpnProductsOnSelected(int position) {
        // OBTENHO O NOME DO PRODUTO SELECIONADO
        String productName = adaptadorDescricaoProduto.getItem(position);
        mPresenter.setProdutoSelecionado(mPresenter.pesquisarProdutoPorNome(productName));
        mPresenter.atualizarViewsDoProdutoSelecionado();
    }

    @Override
    public void showOnUpdateDialog(final int position) {

        AlertDialogUpdateItemSaleOrder alertDialogUpdateItemSaleOrder =
                new AlertDialogUpdateItemSaleOrder(mPresenter);
        AlertDialog alertDialog = alertDialogUpdateItemSaleOrder.builder(position);
        alertDialog.show();
        mPresenter.setAlertDialog(alertDialog);
    }

    @OnItemSelected(R.id.spnUnitys)
    public void setSpnUnityOnSelected(int position) {
        // setar o preco de acordo com aquela unidade  no presenter
        // Setar o edit text do preco com o preco daquela unidade
        mPresenter.setPreco(mPresenter.pesquisarPrecoDaUnidadePorProduto(adapterUnidade.getItem(position)));
        cetPrecoUnitario.setText(String.valueOf(mPresenter.getPreco().getValor()));
        mPresenter.setQtdProdutos(new BigDecimal(edtQTDProducts.getText().toString()));
        txtAmountProducts.setText(
                FormatacaoMoeda.convertDoubleToString(
                        this.mPresenter.getValorTotalProduto().doubleValue()));
    }

    @Override
    public void updateActCodigoProduto() {
        actCodigoProduto.setText(String.valueOf((int) mPresenter.getProdutoSelecionado().getId()));
    }

    @Override
    public void updateRecyclerItens() {
        adapterItemPedidoAdapter.notifyDataSetChanged();
        mPresenter.setValorTotalPedido(new BigDecimal(mPresenter.calcularValorTotalVenda()));
        mPresenter.atualizarTxtValorTotalVenda();
    }

    @Override
    public void updateTxtAmountOrderSale() {
        txtAmountSale.setText(
                FormatacaoMoeda.convertDoubleToString(
                        mPresenter.calcularValorTotalVenda().doubleValue()));
    }

    @Override
    public void updateTxtAmountProducts() {

        mPresenter.getPreco().setValor(cetPrecoUnitario.getCurrencyDouble());
        mPresenter.setQtdProdutos(
                new BigDecimal(
                        edtQTDProducts.getText().toString().isEmpty()
                                ? "0"
                                : edtQTDProducts.getText().toString()));
        txtAmountProducts.setText(
                FormatacaoMoeda.convertDoubleToString(
                        mPresenter.getValorTotalProduto().doubleValue()));
    }

    @Override
    public void exibirBotaoImprimir() {
        btnImprimir.setVisibility(View.VISIBLE);
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public void onChildDraw(
                            Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

                        Bitmap icon;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                            if (dX > 0) {
                                p.setColor(Color.parseColor("#388E3C"));
                                RectF background =
                                        new RectF(
                                                (float) itemView.getLeft(),
                                                (float) itemView.getTop(),
                                                dX,
                                                (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon =
                                        BitmapFactory.decodeResource(
                                                getResources(), R.mipmap.ic_edit_white_36);
                                RectF icon_dest =
                                        new RectF(
                                                (float) itemView.getLeft() + width,
                                                (float) itemView.getTop() + width,
                                                (float) itemView.getLeft() + 2 * width,
                                                (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            } else {
                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background =
                                        new RectF(
                                                (float) itemView.getRight() + dX,
                                                (float) itemView.getTop(),
                                                (float) itemView.getRight(),
                                                (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon =
                                        BitmapFactory.decodeResource(
                                                getResources(), R.mipmap.ic_delete_white_36dp);
                                RectF icon_dest =
                                        new RectF(
                                                (float) itemView.getRight() - 2 * width,
                                                (float) itemView.getTop() + width,
                                                (float) itemView.getRight() - width,
                                                (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            }
                        }
                        super.onChildDraw(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive);
                    }

                    @Override
                    public boolean onMove(
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.LEFT) {
                            mPresenter.getItens().remove(position);
                            mPresenter.updateRecyclerItens();
                        } else {
                            mPresenter.setItemPedido(mPresenter.getItens().get(position));
                            mPresenter.showOnUpdateDialog(position);
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rcvItens);
    }

    @OnClick(R.id.btnImprimir)
    public void setBtnImprimirOnClicked() {
        this.mPresenter.imprimirComprovante();
        this.mPresenter.desabilitarBtnSalvar();
        this.mPresenter.exibirBotaoFotografar();
    }

    @Override
    public boolean validarCamposAntesDeAdicionarItem() {
        if (TextUtils.isEmpty(edtQTDProducts.getText().toString())) {
            edtQTDProducts.setError("Quantidade Obrigatória!");
            edtQTDProducts.requestFocus();
            return false;
        }
        if (Integer.parseInt(edtQTDProducts.getText().toString()) <= 0) {
            edtQTDProducts.setError("Quantidade mínima de 1 item!");
            edtQTDProducts.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtQTDBicos.getText().toString())) {
            edtQTDBicos.setError("Quantidade de Bicos Obrigatória!");
            edtQTDBicos.requestFocus();
            return false;
        }
        if (Integer.parseInt(edtQTDBicos.getText().toString()) <= 0) {
            edtQTDBicos.setError("Quantidade mínima de 1 bico!");
            edtQTDBicos.requestFocus();
            return false;
        }
        return true;
    }

    @NonNull
    private ItemPedido getItemPedido() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(mPresenter.getQtdProdutos().intValue());
        itemPedido.setValorUnitario(mPresenter.getPreco().getValor());
        itemPedido.setDescricao(mPresenter.getProdutoSelecionado().getNome());
        itemPedido.setBicos(Integer.parseInt(edtQTDBicos.getText().toString()));
        itemPedido.setValorTotal(mPresenter.getValorTotalProduto().doubleValue());
        return itemPedido;
    }

    private void iniciarViews() {
        // Toolbar

        mToolbar.setTitle("Vendas");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtAmountSale.setText("R$ 00,00");
        edtQTDProducts.setText("1");
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(VendasActivity.this);
        // Configurando os recycle views
        rcvItens.setLayoutManager(layoutManager);
    }

    @NonNull
    private ItemPedidoID getItemPedidoID() throws ParseException {
        ItemPedidoID itemPedidoID = new ItemPedidoID();
        itemPedidoID.setIdProduto(mPresenter.getProdutoSelecionado().getId());
        itemPedidoID.setIdUnidade(mPresenter.getUnidadeSelecionada().getId());
        itemPedidoID.setDataVenda(
                DateUtils.formatarDateddMMyyyyhhmm(new Date(System.currentTimeMillis())));
        // Nao sei o significado
        itemPedidoID.setVendaMae("N");
        itemPedidoID.setNucleoCodigo(1);
        itemPedidoID.setTipoVenda("?");
        return itemPedidoID;
    }

    private void setAdapters() {

        List<TipoRecebimento> recebimentos =
                mPresenter.pesquisarTipoRecebimentosPorCliente(mPresenter.getCliente());
        adapterFormaPagamento =
                new ArrayAdapter<>(
                        VendasActivity.this,
                        android.R.layout.simple_list_item_1,
                        mPresenter.converterTipoRecebimentoEmString(recebimentos));

        List<Produto> produtos = mPresenter.obterTodosProdutos();

        adaptadorCodigoProduto =
                new ArrayAdapter<>(
                        mPresenter.getContext(),
                        android.R.layout.simple_list_item_1,
                        mPresenter.obterIDProdutos(produtos));

        adaptadorDescricaoProduto =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        mPresenter.obterNomeDeTodosOsProdutos(produtos));

        adapterItemPedidoAdapter = new ItemPedidoAdapter(mPresenter);

        List<Unidade> unidades = mPresenter.obterTodasUnidades();

        adapterUnidade =
                new ArrayAdapter<>(
                        mPresenter.getContext(),
                        android.R.layout.simple_spinner_item,
                        mPresenter.obterTodasUnidadesEmString(unidades));

        actCodigoProduto.setAdapter(adaptadorCodigoProduto);
        spnProducts.setAdapter(adaptadorDescricaoProduto);
        spnFormaPagamento.setAdapter(adapterFormaPagamento);
        rcvItens.setAdapter(adapterItemPedidoAdapter);
        spnUnitys.setAdapter(adapterUnidade);

        spnProducts.setSelection(INITIAL_POSITION);
        spnFormaPagamento.setSelection(INITIAL_POSITION);
        actCodigoProduto.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    Long idProduct = Long.parseLong((String) adapterView.getItemAtPosition(i));
                    // Seto o produto selecionado no presenter
                    mPresenter.setProdutoSelecionado(mPresenter.pesquisarProdutoPorId(idProduct));
                    mPresenter.atualizarViewsDoProdutoSelecionado();
                });
    }
}
