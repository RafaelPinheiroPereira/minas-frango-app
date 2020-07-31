package com.br.minasfrango.ui.activity;

import static com.br.minasfrango.util.ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ContaAdapter;
import com.br.minasfrango.ui.adapter.RecebimentoAdapter;
import com.br.minasfrango.ui.mvp.recebimento.IRecebimentoMVP;
import com.br.minasfrango.ui.mvp.recebimento.Presenter;
import com.br.minasfrango.util.CameraUtil;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.DriveServiceHelper;
import com.br.minasfrango.util.FormatacaoMoeda;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

public class RecebimentoActivity extends AppCompatActivity implements IRecebimentoMVP.IView {

    private static final int POSICAO_INICIAL = 0;


    ContaAdapter adaptadorConta;

    @BindView(R.id.btnImprimirRecebimento)
    Button btnImprimirRecebimento;

    @BindView(R.id.btnSalvarRecebimento)
    Button btnSalvarRecebimento;

    @BindView(R.id.btnFotografar)
    Button btnFotografar;

    @BindView(R.id.edtValueAmortize)
    CurrencyEditText cetValorAmortizar;

    @BindView(R.id.rcvRecebimento)
    RecyclerView rcvRecebimento;

    @BindView(R.id.spnTipoRecebimento)
    Spinner spnConta;

    IRecebimentoMVP.IPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swtcAmortize)
    Switch swtcAmortiza;

    @BindView(R.id.txtEndereco)
    TextView txtEndereco;

    @BindView(R.id.txtClientID)
    TextView txtIdCliente;

    @BindView(R.id.txtFanstasyName)
    TextView txtNomeFantasia;

    RecebimentoAdapter adapter;

    @BindView(R.id.txtQTDNotasAbertas)
    TextView txtQuantidadeDeNotasAbertas;

    @BindView(R.id.txtTotalDevido)
    TextView txtValorTotalDevido;

    private String nomeFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recebimento);
        ButterKnife.bind(this);

        inicializarViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter = new Presenter(this);

        mPresenter.getParametros();
        mPresenter.configurarViewComDadosDoCliente();

        mPresenter.verificarCredenciaisGoogleDrive();

        swtcAmortiza.setChecked(false);

        mPresenter.getRecebimentos().addAll(mPresenter.obterRecebimentoPorCliente());

        txtQuantidadeDeNotasAbertas.setText("Notas Abertas: " + mPresenter.getRecebimentos().size());

        txtValorTotalDevido.setText(
                FormatacaoMoeda.converterParaDolar(
                        mPresenter.getValorTotalDevido().doubleValue()));
        adapter = new RecebimentoAdapter(mPresenter);
        rcvRecebimento.setAdapter(adapter);

        try {
            adaptadorConta =
                    new ContaAdapter(
                            RecebimentoActivity.this,
                            mPresenter.pesquisarContaPorId());
        } catch (Throwable throwable) {
            RecebimentoActivity.this.runOnUiThread(
                    ()->{
                        AbstractActivity.showToast(
                                RecebimentoActivity.this, throwable.getMessage());
                    });
        }

        spnConta.setAdapter(adaptadorConta);
        spnConta.setSelection(POSICAO_INICIAL);

        cetValorAmortizar.setText("00,00");

        cetValorAmortizar.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(final Editable s) {
                        mPresenter.setCredito(
                                new BigDecimal(
                                        s.toString().isEmpty()
                                                ? 0
                                                : cetValorAmortizar.getCurrencyDouble()).setScale(2, BigDecimal.ROUND_HALF_DOWN));

                        mPresenter.getRecebimentos().clear();
                        mPresenter
                                .getRecebimentos()
                                .addAll(mPresenter.obterRecebimentoPorCliente());
                        mPresenter.atualizarRecycleView();

                        if (mPresenter.valorTotalDevidoEhMenorOuIgualAoCredito()) {
                            if (mPresenter.valorDoCreditoEhMaiorDoQueZero()) {
                                if (mPresenter.ehAmortizacaoAutomatica()) {
                                    mPresenter.calcularAmortizacaoAutomatica();
                                }

                            } else {
                                AbstractActivity.showToast(
                                        mPresenter.getContext(), "Saldo Insuficiente!");
                            }

                        } else {
                            // Devo informar que o valor recebido eh maior do que o devido
                            cetValorAmortizar.setError("RecebimentoORM superior ao Valor Devido");
                        }
                        mPresenter.atualizarViewSaldoDevedor();
                    }

                    @Override
                    public void beforeTextChanged(
                            final CharSequence s,
                            final int start,
                            final int count,
                            final int after) {
                    }

                    @Override
                    public void onTextChanged(
                            final CharSequence s,
                            final int start,
                            final int before,
                            final int count) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtil.RESULTADO_INTENCAO_FOTO) {
            if (resultCode == RESULT_OK) {

                mPresenter.getBlocoRecibo().setNomeFoto(nomeFoto + ".jpg");
               mPresenter.alterarBlocoRecibo(mPresenter.getBlocoRecibo());


                AbstractActivity.showToast(
                        mPresenter.getContext(),
                        "Imagem salva: "
                                + CameraUtil
                                .LOCAL_ONDE_A_IMAGEM_FOI_SALVA);

                this.finish();
            } else {
                AbstractActivity.showToast(mPresenter.getContext(), "Imagem não foi salva");
            }

        }
    }

    @Override
    public void atualizarViewSaldoDevedor() {

        txtValorTotalDevido.setTextColor(
                mPresenter.saldoDevidoEhMaiorQueZero() ? Color.RED : Color.GREEN);
        txtValorTotalDevido.setText(
                FormatacaoMoeda.converterParaDolar(
                        mPresenter
                                .getValorTotalDevido()
                                .subtract(mPresenter.getValorTotalAmortizado())
                                .doubleValue()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.fecharConexaoAtiva();
    }

    @Override
    public void configurarViewComDadosDoCliente() {
        txtIdCliente.setText(String.valueOf(mPresenter.getCliente().getId()));
        txtEndereco.setText(mPresenter.getCliente().getEndereco());
        txtNomeFantasia.setText(mPresenter.getCliente().getNome());
    }

    @Override
    public void exibirBotaoComprovante() {
        btnImprimirRecebimento.setVisibility(View.VISIBLE);
        mPresenter.desabilitarBotaoSalvar();
    }

    @Override
    public void exibirBotaoFotografar() {
        btnFotografar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnFotografar)
    public void fotografarComprovante(View view) {

         nomeFoto = String.format("%02d",mPresenter.getRecebimentos().get(0).getIdNucleo())+
                String.format("%03d",new ControleSessao(mPresenter.getContext()).getIdUsuario())+
                String.format("%08d",mPresenter.getRecebimentos().get(0).getIdRecibo());
        CameraUtil cameraUtil = new CameraUtil((Activity) mPresenter.getContext());
        try {
            cameraUtil.tirarFoto(CAMINHO_IMAGEM_RECEBIMENTOS, nomeFoto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getParametros() {
        Bundle args = getIntent().getExtras();
        Cliente cliente = (Cliente) args.getSerializable("keyCliente");
        mPresenter.setCliente(cliente);
    }

    @OnClick(R.id.btnImprimirRecebimento)
    public void imprimirComprovante(View view) {
        for (int i = 0; i < 2; i++) {
            this.mPresenter.imprimirComprovante();
        }
        this.mPresenter.exibirBotaoFotografar();
    }

    @Override
    public void desabilitarBotaoSalvarAmortizacao() {
        this.btnSalvarRecebimento.setClickable(false);
    }

    @OnClick(R.id.btnSalvarRecebimento)
    public void salvarRecebimento() {

      if(!mPresenter.getConta().getId().equals("F"))  {
          if (!new ControleSessao(mPresenter.getContext()).getEnderecoBluetooth().isEmpty()) {

              long idBlocoRecibo= mPresenter.configurarSequenceDoRecebimento();
              if (idBlocoRecibo > 0) {
                  mPresenter.salvarAmortizacao(idBlocoRecibo);
                  mPresenter.atualizarRecycleView();
              }else{

                  AbstractActivity.showToast(
                          mPresenter.getContext(),
                          "Dados do recibo não atualizados com o servidor.\nContate o suporte do sistema");

              }
              mPresenter.esperarPorConexao();
          } else {
              AbstractActivity.showToast(
                      mPresenter.getContext(),
                      "Endereço MAC da impressora não encontrado.\nHabilite no Menu: Configurar impressora");
          }
      }
      else{
          AbstractActivity.showToast(
                  mPresenter.getContext(),
                  "Por favor, selecione um tipo de recebimento.\n");
      }


    }

    @OnCheckedChanged(R.id.swtcAmortize)
    public void setOnSwtcAmortize(CompoundButton compoundButton, boolean b) {

        // Se o valor devido eh maior ou igual ao credito
        if (mPresenter.valorTotalDevidoEhMenorOuIgualAoCredito()) {

            if (b) {
                mPresenter.getRecebimentos().clear();
                mPresenter.getRecebimentos().addAll(mPresenter.obterRecebimentoPorCliente());
                mPresenter.atualizarRecycleView();
                mPresenter.setTypeOfAmortizationIsAutomatic(true);
                mPresenter.setCredito(new BigDecimal(cetValorAmortizar.getCurrencyDouble()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                mPresenter.calcularAmortizacaoAutomatica();
                compoundButton.setText("Quitação Automática de Notas");
                mPresenter.atualizarViewSaldoDevedor();
            } else {
                // REALIZA A QUITACAO MANUAL
                mPresenter.getRecebimentos().addAll(mPresenter.obterRecebimentoPorCliente());
                mPresenter.atualizarRecycleView();
                mPresenter.setTypeOfAmortizationIsAutomatic(false);
                cetValorAmortizar.setText("00,00");
                compoundButton.setText("Quitação Manual de Notas");
                mPresenter.atualizarViewSaldoDevedor();
            }
        } else {
            // Devo informar que o valor recebido eh maior do que o devido
            cetValorAmortizar.setError("Recebimento superior ao Valor Devido");
        }
    }

    @OnItemSelected(R.id.spnTipoRecebimento)
    public void setSpnTipoRecebimentoOnSelected(int position) {

        mPresenter.setConta(
               adaptadorConta.getItem(position));
    }

    @Override
    public void showInsuficentCredit(final String s) {
        AbstractActivity.showToast(mPresenter.getContext(), s);
    }

    @Override
    public void updateRecycleViewAlteredItem(final int position) {
        adapter.notifyItemChanged(position);
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

    @Override
    public void updateRecycleView() {

        adapter.notifyDataSetChanged();
    }

    private void inicializarViews() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecebimentoActivity.this);
        rcvRecebimento.setLayoutManager(layoutManager);

    }
}