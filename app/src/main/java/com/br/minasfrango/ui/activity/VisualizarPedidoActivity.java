package com.br.minasfrango.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ItensPedidoVisualizarAdapter;
import com.br.minasfrango.ui.mvp.vieworder.IViewOrderMVP;
import com.br.minasfrango.ui.mvp.vieworder.IViewOrderMVP.IView;
import com.br.minasfrango.ui.mvp.vieworder.Presenter;
import java.text.DateFormat;

public class VisualizarPedidoActivity extends AppCompatActivity implements IView {

  @BindView(R.id.lnlMotivoCancelamento)
  LinearLayout lnlMotivoCancelamento;

  ItensPedidoVisualizarAdapter mAdapter;

  IViewOrderMVP.IPresenter mPresenter;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.rcvItensViewOrder)
  RecyclerView rcvItensViewOrder;

  @BindView(R.id.txtAdress)
  TextView txtAdress;

  @BindView(R.id.txtClientName)
  TextView txtClientName;

  @BindView(R.id.txtMotivoCancelamento)
  TextView txtMotivoCancelamento;

  @BindView(R.id.txtSaleOrderDate)
  TextView txtSaleOrderDate;

  @BindView(R.id.txtSaleOrderID)
  TextView txtSaleOrderID;

  @BindView(R.id.txtStatus)
  TextView txtStatus;

  @BindView(R.id.txtTipoRecebimento)
  TextView txtTipoRecebimento;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_visualizar_pedido);
    ButterKnife.bind(this);
    initView();
  }

  @Override
  protected void onStart() {
    super.onStart();

    mPresenter = new Presenter(this);
    mPresenter.setPedido(mPresenter.getSaleOrderParams(getIntent().getExtras()));
    mPresenter.setCliente(mPresenter.findClientByID(mPresenter.getPedido().getCodigoCliente()));
    try {
      mPresenter.setTipoRecebimento(
              mPresenter.findTipoRecebimentoByID(mPresenter.getPedido().getTipoRecebimento()));
    } catch (Throwable throwable) {
      VisualizarPedidoActivity.this.runOnUiThread(
              ()->{
                AbstractActivity.showToast(this, throwable.getMessage());
              });
    }

    mPresenter.setDataView();
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
  public void setDataView() {
    mAdapter = new ItensPedidoVisualizarAdapter(this, mPresenter.getPedido().realmListToDTO());
    rcvItensViewOrder.setAdapter(mAdapter);
    rcvItensViewOrder.setLayoutManager(new LinearLayoutManager(this));

    txtTipoRecebimento.setText(mPresenter.getTipoRecebimento().getNome());
    txtSaleOrderDate.setText(
            DateFormat.getDateInstance().format(mPresenter.getPedido().getDataPedido()).toUpperCase());
    txtSaleOrderID.setText(String.valueOf(mPresenter.getPedido().getId()));
    txtClientName.setText(mPresenter.getCliente().getNome());
    txtAdress.setText(mPresenter.getCliente().getEndereco());
    txtStatus.setText(mPresenter.getPedido().isCancelado() ? "Cancelado" : "Ativo");
    if (mPresenter.getPedido().isCancelado()) {
      txtMotivoCancelamento.setText(mPresenter.getPedido().getMotivoCancelamento());
    } else {
      lnlMotivoCancelamento.setVisibility(View.INVISIBLE);
    }
  }

  private void initView() {
    mToolbar.setTitle("Visualizar Pedidos");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
