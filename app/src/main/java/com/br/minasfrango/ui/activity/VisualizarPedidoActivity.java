package com.br.minasfrango.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.br.minasfrango.R;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ItensPedidoVisualizarAdapter;
import com.br.minasfrango.ui.mvp.visualizar.IViewOrderMVP;
import com.br.minasfrango.ui.mvp.visualizar.IViewOrderMVP.IView;
import com.br.minasfrango.ui.mvp.visualizar.Presenter;
import com.br.minasfrango.util.CameraUtil;
import com.br.minasfrango.util.DateUtils;

import java.text.DateFormat;

public class VisualizarPedidoActivity extends AppCompatActivity implements IView {

    @BindView(R.id.btnFotografar)
    Button btnFotografar;

    @BindView(R.id.btnImprimir)
    Button btnImprimir;

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
        iniciarViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter = new Presenter(this);
        mPresenter.setPedido(mPresenter.getParametrosDaVenda(getIntent().getExtras()));
        mPresenter.setCliente(mPresenter.pesquisarClientePorID(mPresenter.getPedido().getCodigoCliente()));
        try {
            mPresenter.setTipoRecebimento(
                    mPresenter.pesquisarTipoRecebimentoPorId(
                            mPresenter.getPedido().getTipoRecebimento()));
        } catch (Throwable throwable) {
            VisualizarPedidoActivity.this.runOnUiThread(
                    () -> {
                        AbstractActivity.showToast(this, throwable.getMessage());
                    });
        }

        mPresenter.setDataView();

        mPresenter.esperarPorConexao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.fecharConexaoAtiva();
    }

    @Override
    public void exibirBotaoGerarRecibo() {
        this.btnImprimir.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.btnImprimir)
    public void setBtnImprimirOnClicked(View view) {

        this.mPresenter.imprimirComprovante();
        this.mPresenter.exibirBotaoFotografar();
    }


    @OnClick(R.id.btnFotografar)
    public void fotografarComprovante(View view) {

        String nomeFoto = mPresenter.getPedido().getId()
                + DateUtils.formatarDateddMMyyyyParaString(
                mPresenter.getPedido().getDataPedido()).replace("/", "-")
                + mPresenter.getCliente().getNome();

        CameraUtil cameraUtil = new CameraUtil((Activity) mPresenter.getContext());
        cameraUtil.tirarFoto(CameraUtil.CAMINHO_IMAGEM_VENDAS, nomeFoto);


    }




    @Override
    public void setDataView() {
        mAdapter = new ItensPedidoVisualizarAdapter(this, mPresenter.getPedido().getItens());
        rcvItensViewOrder.setAdapter(mAdapter);
        rcvItensViewOrder.setLayoutManager(new LinearLayoutManager(this));
        txtTipoRecebimento.setText(mPresenter.getTipoRecebimento().getNome());
        txtSaleOrderDate.setText(
                DateFormat.getDateInstance()
                        .format(mPresenter.getPedido().getDataPedido())
                        .toUpperCase());
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

    @Override
    public void exibirBotaoFotografar() {
        btnFotografar.setVisibility(View.VISIBLE);

    }

    private void iniciarViews() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
