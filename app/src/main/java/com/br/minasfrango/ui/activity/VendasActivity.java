package com.br.minasfrango.ui.activity;

import static com.br.minasfrango.util.ConstantsUtil.CAMINHO_IMAGEM_VENDAS;

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
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
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
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ItemPedidoAdapter;
import com.br.minasfrango.ui.mvp.venda.IVendaMVP;
import com.br.minasfrango.ui.mvp.venda.IVendaMVP.IView;
import com.br.minasfrango.ui.mvp.venda.Presenter;
import com.br.minasfrango.util.AlertDialogItemPedido;
import com.br.minasfrango.util.CameraUtil;
import com.br.minasfrango.util.ConstantsUtil;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class VendasActivity extends AppCompatActivity implements IView {

    /** Positions initials of spinners present in to activity */
    private static final int POSICAO_INICIAL = 0;

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

    ArrayAdapter<String> adaptadorDescricoesProduto;

    ItemPedidoAdapter adaptadorItensPedido;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Paint p = new Paint();

    @BindView(R.id.rcvItens)
    RecyclerView rcvItens;

    SearchView searchView;

    ArrayAdapter<String> adaptadorUnidades;

    @BindView(R.id.edtQTDProducts)
    CurrencyEditText edtQuantidadeProduto;

    IVendaMVP.IPresenter mPresenter;

    @BindView(R.id.btnSalvarVenda)
    Button btnSalvarVenda;

    @BindView(R.id.spnProducts)
    Spinner spnProdutos;

    @BindView(R.id.spnLotes)
    Spinner spnLote;

    @BindView(R.id.spnUnitys)
    Spinner spnUnidades;

    @BindView(R.id.txtAmountProducts)
    TextView txtValorTotalProduto;

    @BindView(R.id.txtAmountSale)
    TextView txtValorTotalVenda;

    @BindView(R.id.txtClienteID)
    TextView txtClienteID;

    @BindView(R.id.txtRazaoSocial)
    TextView txtRazaoSocial;

    @BindView(R.id.txtCidade)
    TextView txtCidade;

    @BindView(R.id.txtEndereco)
    TextView txtEndereco;

    @BindView(R.id.btnFotografar)
    Button btnFotografar;

    ArrayAdapter<CharSequence> adaptadorLotes;

    private String nomeFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        ButterKnife.bind(this);
        iniciarViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter = new Presenter(this);
        mPresenter.getParametros();
        mPresenter.setFuncionario(mPresenter.getFuncionarioDaSessao());

        if(mPresenter.getFuncionario().getAlteraPreco().equals(ConstantsUtil.TEM_PERMISSAO_PARA_ALTERAR_PRECO)){
            cetPrecoUnitario.setEnabled(true);
        }else{
            cetPrecoUnitario.setEnabled(false);
        }

        setAdaptadores();
        try {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                if (Optional.ofNullable(mPresenter.getPedido()).isPresent()) {
                    mPresenter.carregarVenda();
                } else {
                    inicializarSwipe();
                }
            } else {
                if (mPresenter.getPedido() != null) {
                    mPresenter.carregarVenda();
                } else {
                    inicializarSwipe();
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        edtQuantidadeProduto.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {

                        mPresenter.updateTxtAmountProducts();
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
                        mPresenter.updateTxtAmountProducts();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtil.RESULTADO_INTENCAO_FOTO) {
            if (resultCode == RESULT_OK) {

              mPresenter.getPedido().setNomeFoto(nomeFoto+".jpg");
              mPresenter.atualizarPedido(mPresenter.getPedido());


                AbstractActivity.showToast(
                        mPresenter.getContext(),
                        "Imagem salva: " + CameraUtil.LOCAL_ONDE_A_IMAGEM_FOI_SALVA);
                this.finish();

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

    @OnClick(R.id.btnAddItem)
    public void adicionarItem(View view) {

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

            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                if (mPresenter.getItens().stream()
                                .filter(
                                        itemPedido ->
                                                itemPedido.getChavesItemPedido().getIdProduto()
                                                        == mPresenter
                                                                .getItemPedido()
                                                                .getChavesItemPedido()
                                                                .getIdProduto())
                                .count()
                        == 0) {
                    mPresenter.getItens().add(mPresenter.getItemPedido());
                    mPresenter.atualizarRecyclerItens();

                } else {
                    AbstractActivity.showToast(
                            mPresenter.getContext(), "O produto já existe na lista!");
                }
            } else {
                boolean temItemNaLista = false;

                for (ItemPedido itemPedido : mPresenter.getItens()) {
                    if (itemPedido.getChavesItemPedido().getIdProduto()
                            == mPresenter.getItemPedido().getChavesItemPedido().getIdProduto()) {
                        temItemNaLista = true;
                    }
                }
                if (!temItemNaLista) {
                    mPresenter.getItens().add(mPresenter.getItemPedido());
                    mPresenter.atualizarRecyclerItens();
                } else {
                    AbstractActivity.showToast(
                            mPresenter.getContext(), "O produto já existe na lista!");
                }
            }
        }
    }

    @Override
    public void atualizarTextViewTotalVenda() {
        txtValorTotalVenda.setText(
                FormatacaoMoeda.converterParaDolar(
                        mPresenter.calcularTotalDaVenda().doubleValue()));
    }

    @Override
    public void atualizarTextViewValorTotalProduto() {

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            if (Optional.ofNullable(mPresenter.getPreco()).isPresent()) {
                mPresenter.getPreco().setValor(cetPrecoUnitario.getCurrencyDouble());
            } else {
                mPresenter.setPreco(new Preco());
            }
        } else {
            if (mPresenter.getPreco() != null) {
                mPresenter.getPreco().setValor(cetPrecoUnitario.getCurrencyDouble());
            } else {
                mPresenter.setPreco(new Preco());
            }
        }
        mPresenter.setQuantidadeProdutos(
                new BigDecimal(
                                edtQuantidadeProduto.getText().toString().isEmpty()
                                        ? 0.0
                                        : edtQuantidadeProduto.getCurrencyDouble())
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN));
        txtValorTotalProduto.setText(
                FormatacaoMoeda.converterParaReal(mPresenter.getValorTotalProduto().doubleValue()));
    }

    @Override
    public void atualizarViewPrecoPosFoto() {}

    @Override
    public void atualizarSpinnerLotes() {

        String[] lotes = getResources().getStringArray(R.array.lotes);
        for (int i = 0; i < lotes.length; i++) {
            if (lotes[i].equals(mPresenter.getItens().get(0).getLote())) {
                spnLote.setSelection(i);
            }
        }
    }

    @Override
    public void atualizarViewsDoProdutoSelecionado() {
        mPresenter.setSpinnerProdutoSelecionado();
        mPresenter.setSpinnerUnidadePadraoProdutoSelecionado();
        mPresenter.setQuantidadeProdutos(new BigDecimal("1"));
        mPresenter.setPreco(mPresenter.pesquisarPrecoPorProduto());

        cetPrecoUnitario.setText(
                FormatacaoMoeda.converterParaDolar(mPresenter.getPreco().getValor()));

        mPresenter.updateTxtAmountProducts();
        updateActCodigoProduto();
    }

    @OnClick(R.id.btnSalvarVenda)
    public void btnConfirmSaleOnClicked(View view) {

        if ((mPresenter.getItens().size() > 0)
                && (!new ControleSessao(mPresenter.getContext())
                        .getEnderecoBluetooth()
                        .isEmpty())) {
            // Realiza Update do PedidoORM
            if (mPresenter.getPedido() != null) {

                List<ItemPedido> itensDTO = mPresenter.getItens();

                mPresenter.getPedido().setItens((itensDTO));
                mPresenter.getPedido().setValorTotal(mPresenter.calcularTotalDaVenda());
                mPresenter.atualizarPedido(mPresenter.getPedido());

            } else {
                // Salva o PedidoORM
                try {
                    long sequencePedido =
                            mPresenter.configurarSequenceDoPedido(
                                    new ControleSessao(mPresenter.getContext()));

                    if (sequencePedido > 0) {
                        mPresenter.salvarVenda(sequencePedido);
                    } else {
                        AbstractActivity.showToast(
                                mPresenter.getContext(),
                                "Código de vendas não está atualizado.\nPor favor, contate o suporte do sistema. ");
                    }

                } catch (ParseException e) {
                    VendasActivity.this.runOnUiThread(
                            () ->
                                    AbstractActivity.showToast(
                                            mPresenter.getContext(),
                                            "Erro Formatacao Data Pedido: " + e.getMessage()));
                }
            }

            mPresenter.esperarPorConexao();

        } else if (new ControleSessao(mPresenter.getContext()).getEnderecoBluetooth().isEmpty()) {
            AbstractActivity.showToast(
                    mPresenter.getContext(),
                    "DispositivoImpressora não conectado!\nHabilite no Menu : Configurar Impressora.");
        } else {
            AbstractActivity.showToast(
                    mPresenter.getContext(), " No mímimo um item deve ser adicionado!");
        }
    }

    @Override
    public void carregarVenda() throws Throwable {

        mPresenter.setItens(mPresenter.getPedido().getItens());
        mPresenter.atualizarRecyclerItens();
        mPresenter.atualizarSpinnerLotes();
        inicializarSwipe();
    }

    @Override
    public void error(final String text) {
        runOnUiThread(
                () -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show());
    }

    @Override
    public void desabilitarCliqueBotaoSalvarVenda() {
        btnSalvarVenda.setClickable(false);
        btnSalvarVenda.setEnabled(false);
    }

    @Override
    public void dissmiss() {
        mPresenter.getAlertDialog().dismiss();
        mPresenter.atualizarRecyclerItens();
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

    @Override
    public void exibirDialogAlterarItemPedido(final int position) {

        AlertDialogItemPedido alertDialogItemPedido = new AlertDialogItemPedido(mPresenter);
        AlertDialog alertDialog = alertDialogItemPedido.builder(position);
        alertDialog.show();
        mPresenter.setAlertDialog(alertDialog);
    }

    @OnClick(R.id.btnFotografar)
    public void fotografarComprovante(View view) {
        nomeFoto =
                String.format("%02d", mPresenter.getPedido().getIdNucleo())
                        + String.format("%03d", mPresenter.getPedido().getCodigoFuncionario())
                        + String.format("%08d", mPresenter.getPedido().getIdVenda());

        CameraUtil cameraUtil = new CameraUtil((Activity) mPresenter.getContext());
        try {
            cameraUtil.tirarFoto(CAMINHO_IMAGEM_VENDAS, nomeFoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getParametros() {

        if (getIntent().getExtras().getLong("keyPedido") == 0) {
            mPresenter.setPedido(null);
            mPresenter.setCliente((Cliente) getIntent().getExtras().getSerializable("keyCliente"));
        } else {

            Pedido pedido =
                    mPresenter.pesquisarVendaPorId(getIntent().getExtras().getLong("keyPedido"));
            mPresenter.setPedido(pedido);
            Cliente cliente =
                    mPresenter.pesquisarClientePorId(mPresenter.getPedido().getCodigoCliente());
            mPresenter.setCliente(cliente);
        }
    }

    @OnClick(R.id.btnImprimir)
    public void imprimirComprovante() {

        for (int i = 0; i < 2; i++) {
            this.mPresenter.imprimirComprovante();
        }
        this.mPresenter.desabilitarBotaoSalvarPedido();
        this.mPresenter.exibirBotaoFotografar();
    }

    @Override
    public void inicializarSpinnerUnidadesComUnidadePadraoDoProduto() {
        Unidade unidadePadrao = mPresenter.carregarUnidadesPorProduto();
        spnUnidades.setSelection(
                adaptadorUnidades.getPosition(unidadePadrao.getId().split("-")[0]));
        mPresenter.setUnidadeSelecionada(unidadePadrao);
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
                        // adaptadorItensPedido.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // filter recycler view when query submitted
                        //  adaptadorItensPedido.getFilter().filter(query);
                        return false;
                    }
                });
        return true;
    }

    @Override
    public void updateActCodigoProduto() {
        actCodigoProduto.setText(String.valueOf((int) mPresenter.getProdutoSelecionado().getId()));
    }

    @Override
    public void setSpinnerProductSelected() {
        spnProdutos.setSelection(
                adaptadorDescricoesProduto.getPosition(
                        mPresenter.getProdutoSelecionado().getNome()));
    }

    @OnItemSelected(R.id.spnProducts)
    public void setSpnProductsOnSelected(int position) {
        // OBTENHO O NOME DO PRODUTO SELECIONADO
        String productName = adaptadorDescricoesProduto.getItem(position);
        mPresenter.setProdutoSelecionado(mPresenter.pesquisarProdutoPorNome(productName));
        mPresenter.atualizarProdutoSelecionadoView();
    }

    @OnItemSelected(R.id.spnLotes)
    public void setLoteSelecionado(int position) {

        String lote = adaptadorLotes.getItem(position).toString();
        mPresenter.setLoteSelecionado(lote);
    }

    @OnItemSelected(R.id.spnUnitys)
    public void setSpnUnityOnSelected(int position) {
        // setar o preco de acordo com aquela unidade  no presenter
        // Setar o edit text do preco com o preco daquela unidade

        if (mPresenter
                .getFuncionario()
                .getAlteraPreco()
                .equals(ConstantsUtil.TEM_PERMISSAO_PARA_ALTERAR_PRECO)) {

            mPresenter.setPreco(
                    mPresenter.pesquisarPrecoDaUnidadePorProduto(
                            adaptadorUnidades.getItem(position)));
            mPresenter.getPreco().setValor(cetPrecoUnitario.getCurrencyDouble());
        } else {

            Preco precoZerado =
                    mPresenter.pesquisarPrecoDaUnidadePorProduto(
                            adaptadorUnidades.getItem(position));
            precoZerado.setValor(0.0);
            mPresenter.setPreco(precoZerado);
        }

        cetPrecoUnitario.setText(
                FormatacaoMoeda.converterParaDolar(mPresenter.getPreco().getValor()));
        mPresenter.setQuantidadeProdutos(
                new BigDecimal(edtQuantidadeProduto.getText().toString().replace(",", "."))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN));
        txtValorTotalProduto.setText(
                FormatacaoMoeda.converterParaReal(
                        this.mPresenter.getValorTotalProduto().doubleValue()));
    }

    @Override
    public void exibirBotaoImprimir() {
        btnImprimir.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateRecyclerItens() {
        adaptadorItensPedido.notifyDataSetChanged();
        mPresenter.setTotalDaVenda(new BigDecimal(mPresenter.calcularTotalDaVenda()));
        mPresenter.updateTxtAmountOrderSale();
    }

    @Override
    public boolean validarCamposAntesDeAdicionarItem() {
        if (TextUtils.isEmpty(edtQuantidadeProduto.getText().toString())) {
            edtQuantidadeProduto.setError("Quantidade Obrigatória!");
            edtQuantidadeProduto.requestFocus();
            return false;
        }
        if (edtQuantidadeProduto.getCurrencyDouble() <= 0) {
            edtQuantidadeProduto.setError("Quantidade mínima de 1 item!");
            edtQuantidadeProduto.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtQTDBicos.getText().toString())) {
            edtQTDBicos.setError("Quantidade de Bicos Obrigatória!");
            edtQTDBicos.requestFocus();
            return false;
        }
        if (Long.parseLong(edtQTDBicos.getText().toString()) <= 0) {
            edtQTDBicos.setError("Quantidade mínima de 1 bico!");
            edtQTDBicos.requestFocus();
            return false;
        }
        return true;
    }

    @NonNull
    private ItemPedido getItemPedido() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(mPresenter.getQuantidadeProdutos().doubleValue());
        itemPedido.setValorUnitario(mPresenter.getPreco().getValor());
        itemPedido.setDescricao(mPresenter.getProdutoSelecionado().getNome());
        itemPedido.setBicos(Integer.parseInt(edtQTDBicos.getText().toString()));
        itemPedido.setValorTotal(mPresenter.getValorTotalProduto().doubleValue());
        itemPedido.setLote(mPresenter.getLoteSelecionado());
        return itemPedido;
    }

    @NonNull
    private ItemPedidoID getItemPedidoID() throws ParseException {
        ItemPedidoID itemPedidoID = new ItemPedidoID();
        itemPedidoID.setIdProduto(mPresenter.getProdutoSelecionado().getId());
        itemPedidoID.setIdUnidade(mPresenter.getUnidadeSelecionada().getId().split("-")[0]);
        itemPedidoID.setDataVenda(
                DateUtils.formatarDateParaddMMyyyyhhmm(new Date(System.currentTimeMillis())));
        // Nao sei o significado
        itemPedidoID.setVendaMae("N");
        itemPedidoID.setNucleoCodigo(new ControleSessao(mPresenter.getContext()).getIdNucleo());
        itemPedidoID.setTipoVenda("?");
        return itemPedidoID;
    }

    private void inicializarSwipe() {
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
                            mPresenter.atualizarRecyclerItens();
                        } else {
                            mPresenter.setItemPedido(mPresenter.getItens().get(position));
                            mPresenter.exibirDialogAlterarItemPedido(position);
                            mPresenter.atualizarRecyclerItens();
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rcvItens);
    }

    private void iniciarViews() {
        // Toolbar

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtQuantidadeProduto.setText("0.00");
        txtValorTotalVenda.setText("R$ 00,00");

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(VendasActivity.this);
        // Configurando os recycle views
        rcvItens.setLayoutManager(layoutManager);
    }

    private void setAdaptadores() {

        List<Produto> produtos = mPresenter.carregarProdutos();

        adaptadorCodigoProduto =
                new ArrayAdapter<>(
                        mPresenter.getContext(),
                        android.R.layout.simple_list_item_1,
                        mPresenter.carregarIdProdutos(produtos));

        adaptadorDescricoesProduto =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        mPresenter.carregarProdutosPorNome(produtos));

        adaptadorItensPedido = new ItemPedidoAdapter(mPresenter);

        List<Unidade> unidades = mPresenter.carregarUnidades();

        adaptadorUnidades =
                new ArrayAdapter<>(
                        mPresenter.getContext(),
                        android.R.layout.simple_spinner_item,
                        mPresenter.carregarUnidadesEmString(unidades));

        actCodigoProduto.setAdapter(adaptadorCodigoProduto);
        spnProdutos.setAdapter(adaptadorDescricoesProduto);

        rcvItens.setAdapter(adaptadorItensPedido);
        spnUnidades.setAdapter(adaptadorUnidades);

        spnProdutos.setSelection(POSICAO_INICIAL);

        actCodigoProduto.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    Long idProduct = Long.parseLong((String) adapterView.getItemAtPosition(i));
                    // Seto o produto selecionado no presenter
                    mPresenter.setProdutoSelecionado(mPresenter.pesquisarProdutoPorId(idProduct));
                    mPresenter.atualizarProdutoSelecionadoView();
                });

        adaptadorLotes =
                ArrayAdapter.createFromResource(
                        mPresenter.getContext(),
                        R.array.lotes,
                        android.R.layout.simple_spinner_item);
        spnLote.setAdapter(adaptadorLotes);
        spnLote.setSelection(POSICAO_INICIAL);

        txtClienteID.setText(String.format("%05d", mPresenter.getCliente().getId()));
        txtRazaoSocial.setText(mPresenter.getCliente().getRazaoSocial());
        txtCidade.setText(mPresenter.getCliente().getCidade());
        txtEndereco.setText(mPresenter.getCliente().getEndereco());
    }
}
