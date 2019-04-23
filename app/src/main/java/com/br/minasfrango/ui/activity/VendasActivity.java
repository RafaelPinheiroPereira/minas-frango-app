package com.br.minasfrango.ui.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.ItemPedido;
import com.br.minasfrango.data.realm.ItemPedidoID;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.Produto;
import com.br.minasfrango.data.realm.TipoRecebimento;
import com.br.minasfrango.data.realm.Unidade;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ItemPedidoAdapter;
import com.br.minasfrango.ui.mvp.sales.ISalesMVP;
import com.br.minasfrango.ui.mvp.sales.ISalesMVP.IView;
import com.br.minasfrango.ui.mvp.sales.Presenter;
import com.br.minasfrango.util.AlertDialogUpdateItemSaleOrder;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.FormatacaoMoeda;
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

  ArrayAdapter<String> adaptadorCodigoProduto;

  ArrayAdapter<String> adaptadorDescricaoProduto;

  ArrayAdapter<String> adapterFormaPagamento;

  ItemPedidoAdapter adapterItemPedidoAdapter;

  ArrayAdapter<String> adapterUnidade;

  @BindView(R.id.btnAddItem)
  Button btnAddItem;

  @BindView(R.id.btnConfirmSale)
  Button btnConfirmSale;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vendas);
    mPresenter = new Presenter(this);
    ButterKnife.bind(this);
    initViews();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresenter.getParams();
    // Seta todos os adaptadores necessarios
    setAdapters();
    // Edição de PedidoActivity
    if (mPresenter.getOrderSale() != null) {
      try {
        mPresenter.loadDetailsSale();

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

                mPresenter.updateTxtAmountProducts();
              }

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
              }
            });

    cetPrecoUnitario.addTextChangedListener(
            new TextWatcher() {
              @Override
              public void afterTextChanged(Editable s) {
              }

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.updateTxtAmountProducts();
          }
            });
  }

  @OnClick(R.id.btnAddItem)
  public void btnAddItemOnClicked(View view) {

    if (mPresenter.validateFieldsBeforeAddItem()) {
      mPresenter.setItemPedido(getItemPedido());
      ItemPedidoID itemPedidoID = null;
      try {
        itemPedidoID = getItemPedidoID();
      } catch (ParseException e) {
        VendasActivity.this.runOnUiThread(
                ()->
                        AbstractActivity.showToast(
                                mPresenter.getContext(), "Erro Formatação Data Pedido :" + e.getMessage()));
      }
      mPresenter.getItemPedido().setChavesItemPedido(itemPedidoID);
      if (!mPresenter.getItens().contains(mPresenter.getItemPedido())) {
        mPresenter.getItens().add(mPresenter.getItemPedido());
        mPresenter.updateRecyclerItens();

      } else {
        AbstractActivity.showToast(mPresenter.getContext(), "O produto já existe na lista!");
      }
    }
  }

  @OnClick(R.id.btnConfirmSale)
  public void btnConfirmSaleOnClicked(View view) {

    if (mPresenter.getItens().size() > 0
            && !mPresenter.getTipoRecebimento().equals("Formas de Pagamento")) {
      // Realiza Update do Pedido
      if (mPresenter.getOrderSale() != null) {

        List<ItemPedido> itensDTO = mPresenter.getItens();
        mPresenter.getOrderSale().setTipoRecebimento(mPresenter.getTipoRecebimentoID());
        mPresenter.getOrderSale().setItens(Pedido.dtoToRealList(itensDTO));
        mPresenter.getOrderSale().setValorTotal(mPresenter.calculeTotalOrderSale());
        mPresenter.updateSaleOrder(mPresenter.getOrderSale());
        AbstractActivity.showToast(mPresenter.getContext(), "Pedido Alterado com Sucesso!");
        NavUtils.navigateUpFromSameTask(this);
      } else {
        // Salva o Pedido
        try {
          mPresenter.saveOrderSale();

          AbstractActivity.showToast(mPresenter.getContext(), "Pedido Salvo!");
          NavUtils.navigateUpFromSameTask(this);

        } catch (ParseException e) {
          VendasActivity.this.runOnUiThread(
                  ()->
                          AbstractActivity.showToast(
                                  mPresenter.getContext(), "Erro Formatacao Data Pedido: " + e.getMessage()));
        }
      }

    } else if (mPresenter.getTipoRecebimento().equals("Formas de Pagamento")) {
      AbstractActivity.showToast(mPresenter.getContext(), "Forma de Pagamento Inválida!");
    } else {
      AbstractActivity.showToast(
              mPresenter.getContext(), " No mímimo um item deve ser adicionado!");
    }
  }

  @Override
  public void dissmiss() {
    mPresenter.getAlertDialog().dismiss();
  }

  @Override
  public void getParams() {

    if (getIntent().getExtras().getLong("keyPedido") == 0) {
      mPresenter.setOrderSale(null);
      mPresenter.setClient((Cliente) getIntent().getExtras().getSerializable("keyCliente"));
    } else {

      Pedido pedido = mPresenter.loadSaleOrder(getIntent().getExtras().getLong("keyPedido"));
      mPresenter.setOrderSale(Pedido.convertRealmToDTO(pedido));
      Cliente cliente = mPresenter.findClienteByID(mPresenter.getOrderSale().getCodigoCliente());
      mPresenter.setClient(Cliente.convertRealmToDTO(cliente));
    }
  }

  @Override
  public void loadDetailsSale() throws Throwable {
    TipoRecebimento tipoRecebimento = mPresenter.loadTipoRecebimentoById();
    spnFormaPagamento.setSelection(adapterFormaPagamento.getPosition(tipoRecebimento.getNome()));
    mPresenter.setTipoRecebimento(tipoRecebimento.getNome());
    mPresenter.setItens(mPresenter.getOrderSale().realmListToDTO());
    mPresenter.updateRecyclerItens();
    initSwipe();
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
  public void refreshSelectedProductViews() {
    mPresenter.setSpinnerProductSelected();
    mPresenter.setSpinnerUnityPatternOfProductSelected();
    mPresenter.setQtdProdutos(new BigDecimal("1"));
    mPresenter.setPrice(mPresenter.loadPriceByProduct());
    cetPrecoUnitario.setText(String.valueOf(mPresenter.getPrice().getValor()));
    mPresenter.updateTxtAmountProducts();
    updateActCodigoProduto();
  }

  @Override
  public void setSpinnerProductSelected() {
    spnProducts.setSelection(
            adaptadorDescricaoProduto.getPosition(mPresenter.getProductSelected().getNome()));
  }

  @Override
  public void setSpinnerUnityPatternOfProductSelected() {
    Unidade unityPattern = mPresenter.loadUnityByProduct();

    spnUnitys.setSelection(adapterUnidade.getPosition(unityPattern.getId().split("-")[0]));
    mPresenter.setUnitSelected(unityPattern);
  }

  @OnItemSelected(R.id.spnFormaPagamento)
  public void setSpnFormaPagamentoOnSelected(int position) {

    mPresenter.setTipoRecebimento(adapterFormaPagamento.getItem(position));
  }

  @OnItemSelected(R.id.spnProducts)
  public void setSpnProductsOnSelected(int position) {
    // OBTENHO O NOME DO PRODUTO SELECIONADO
    String productName = adaptadorDescricaoProduto.getItem(position);
    mPresenter.setProductSelected(mPresenter.loadProductByName(productName));
    mPresenter.refreshSelectedProductViews();
  }

  @OnItemSelected(R.id.spnUnitys)
  public void setSpnUnityOnSelected(int position) {
    // setar o preco de acordo com aquela unidade  no presenter
    // Setar o edit text do preco com o preco daquela unidade
    mPresenter.setPrice(mPresenter.loadPriceOfUnityByProduct(adapterUnidade.getItem(position)));
    cetPrecoUnitario.setText(String.valueOf(mPresenter.getPrice().getValor()));
    mPresenter.setQtdProdutos(new BigDecimal(edtQTDProducts.getText().toString()));
    txtAmountProducts.setText(
            FormatacaoMoeda.convertDoubleToString(
                    this.mPresenter.getTotalProductValue().doubleValue()));
  }

  @Override
  public void showOnUpdateDialog(final int position) {

    AlertDialogUpdateItemSaleOrder alertDialogUpdateItemSaleOrder =
            new AlertDialogUpdateItemSaleOrder(mPresenter);
    AlertDialog alertDialog = alertDialogUpdateItemSaleOrder.builder(position);
    alertDialog.show();
    mPresenter.setAlertDialog(alertDialog);
  }

  @Override
  public void updateActCodigoProduto() {
    actCodigoProduto.setText(String.valueOf((int) mPresenter.getProductSelected().getId()));
  }

  @Override
  public void updateRecyclerItens() {
    adapterItemPedidoAdapter.notifyDataSetChanged();
    mPresenter.setTotalOrderSale(new BigDecimal(mPresenter.calculeTotalOrderSale()));
    mPresenter.updateTxtAmountOrderSale();
  }

  @Override
  public void updateTxtAmountOrderSale() {
    txtAmountSale.setText(
            FormatacaoMoeda.convertDoubleToString(mPresenter.calculeTotalOrderSale().doubleValue()));
  }

  @Override
  public void updateTxtAmountProducts() {

    mPresenter.getPrice().setValor(cetPrecoUnitario.getCurrencyDouble());
    mPresenter.setQtdProdutos(
            new BigDecimal(
                    edtQTDProducts.getText().toString().isEmpty()
                            ? "0"
                            : edtQTDProducts.getText().toString()));
    txtAmountProducts.setText(
            FormatacaoMoeda.convertDoubleToString(mPresenter.getTotalProductValue().doubleValue()));
  }

  @Override
  public boolean validateFieldsBeforeAddItem() {
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
    itemPedido.setValorUnitario(mPresenter.getPrice().getValor());
    itemPedido.setDescricao(mPresenter.getProductSelected().getNome());
    itemPedido.setBicos(Integer.parseInt(edtQTDBicos.getText().toString()));
    itemPedido.setValorTotal(mPresenter.getTotalProductValue().doubleValue());
    return itemPedido;
  }

  @NonNull
  private ItemPedidoID getItemPedidoID() throws ParseException {
    ItemPedidoID itemPedidoID = new ItemPedidoID();
    itemPedidoID.setIdProduto(mPresenter.getProductSelected().getId());
    itemPedidoID.setIdUnidade(mPresenter.getUnitSelected().getId());
    itemPedidoID.setDataVenda(DateUtils.formatDateDDMMYYYY(new Date(System.currentTimeMillis())));
    // Nao sei o significado
    itemPedidoID.setVendaMae("N");
    itemPedidoID.setNucleoCodigo(1);
    itemPedidoID.setTipoVenda("?");
    return itemPedidoID;
  }

  private void initSwipe() {
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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
                    icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_edit_white_36);
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
                    icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_white_36dp);
                    RectF icon_dest =
                            new RectF(
                                    (float) itemView.getRight() - 2 * width,
                                    (float) itemView.getTop() + width,
                                    (float) itemView.getRight() - width,
                                    (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                  }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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

  private void initViews() {
    // Toolbar

    mToolbar.setTitle("Vendas");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    txtAmountSale.setText("Valor Total do Produto:R$ 00,00");
    edtQTDProducts.setText("1");
    LinearLayoutManager layoutManager;
    layoutManager = new LinearLayoutManager(VendasActivity.this);
    // Configurando os recycle views
    rcvItens.setLayoutManager(layoutManager);
  }

  private void setAdapters() {

    List<TipoRecebimento> tipoRecebimentos =
            mPresenter.loadTipoRecebimentosByClient(mPresenter.getClient());
    adapterFormaPagamento =
            new ArrayAdapter<>(
                    VendasActivity.this,
                    android.R.layout.simple_list_item_1,
                    mPresenter.convertTipoRecebimentoInString(tipoRecebimentos));

    List<Produto> produtos = mPresenter.loadAllProducts();

    adaptadorCodigoProduto =
            new ArrayAdapter<>(
                    mPresenter.getContext(),
                    android.R.layout.simple_list_item_1,
                    mPresenter.loadAllProductsID(produtos));

    adaptadorDescricaoProduto =
            new ArrayAdapter(
                    this, android.R.layout.simple_list_item_1, mPresenter.loadAllProductsByName(produtos));

    adapterItemPedidoAdapter = new ItemPedidoAdapter(mPresenter);

    List<Unidade> unitys = mPresenter.loadAllUnitys();

    adapterUnidade =
            new ArrayAdapter<>(
                    mPresenter.getContext(),
                    android.R.layout.simple_spinner_item,
                    mPresenter.loadAllUnitysToString(unitys));

    actCodigoProduto.setAdapter(adaptadorCodigoProduto);
    spnProducts.setAdapter(adaptadorDescricaoProduto);
    spnFormaPagamento.setAdapter(adapterFormaPagamento);
    rcvItens.setAdapter(adapterItemPedidoAdapter);
    spnUnitys.setAdapter(adapterUnidade);

    spnProducts.setSelection(INITIAL_POSITION);
    spnFormaPagamento.setSelection(INITIAL_POSITION);
    actCodigoProduto.setOnItemClickListener(
            (adapterView, view, i, l)->{
              Long idProduct = Long.parseLong((String) adapterView.getItemAtPosition(i));
              // Seto o produto selecionado no presenter
              mPresenter.setProductSelected(mPresenter.loadProductById(idProduct));
              mPresenter.refreshSelectedProductViews();
            });
  }
}
