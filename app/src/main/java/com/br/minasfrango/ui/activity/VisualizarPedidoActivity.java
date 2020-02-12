package com.br.minasfrango.ui.activity;

import static com.br.minasfrango.util.ConstantsUtil.CAMINHO_IMAGEM_VENDAS;

import android.app.Activity;
import android.content.Intent;
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
import com.br.minasfrango.util.DriveServiceHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collections;

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

    String nomeFoto;

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
        mPresenter.setCliente(
                mPresenter.pesquisarClientePorID(mPresenter.getPedido().getCodigoCliente()));
        mPresenter.verificarCredenciaisGoogleDrive();
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
        for (int i = 0; i < 2; i++) {
            this.mPresenter.imprimirComprovante();
        }
        this.mPresenter.exibirBotaoFotografar();
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
    public void setDataView() {
        mAdapter = new ItensPedidoVisualizarAdapter(this, mPresenter.getPedido().getItens());
        rcvItensViewOrder.setAdapter(mAdapter);
        rcvItensViewOrder.setLayoutManager(new LinearLayoutManager(this));

        txtSaleOrderDate.setText(
                DateFormat.getDateInstance()
                        .format(mPresenter.getPedido().getDataPedido())
                        .toUpperCase());
        txtSaleOrderID.setText(String.valueOf(mPresenter.getPedido().getIdVenda()));
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

    @Override
    public void verificarCredenciaisGoogleDrive() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        this, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount());
        Drive googleDriveService =
                new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                        .setApplicationName("Minas Frangos")
                        .build();

        mPresenter.setDriveServiceHelper(new DriveServiceHelper(googleDriveService));
    }

    private void iniciarViews() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            } else {
                AbstractActivity.showToast(mPresenter.getContext(), "Imagem não foi salva");
            }
        }
    }
}
