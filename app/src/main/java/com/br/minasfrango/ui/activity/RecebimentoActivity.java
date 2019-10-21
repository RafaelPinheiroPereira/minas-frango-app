package com.br.minasfrango.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.br.minasfrango.ui.adapter.RecebimentoAdapter;
import com.br.minasfrango.ui.mvp.recebimento.IRecebimentoMVP;
import com.br.minasfrango.ui.mvp.recebimento.Presenter;
import com.br.minasfrango.util.CameraUtil;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.math.BigDecimal;
import java.util.Date;

public class RecebimentoActivity extends AppCompatActivity implements IRecebimentoMVP.IView {

    private static final int POSICAO_INICIAL = 0;

    private static final long ID_TIPO_RECEBIMENTO_A_VISTA = 1;

    ArrayAdapter<String> adaptadorTipoRecebimento;

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
    Spinner spnTipoRecebimento;

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

        swtcAmortiza.setChecked(false);

        mPresenter.getRecebimentos().addAll(mPresenter.obterRecebimentoPorCliente());

        txtQuantidadeDeNotasAbertas.setText("Notas Abertas: " + mPresenter.getRecebimentos().size());

        txtValorTotalDevido.setText(
                FormatacaoMoeda.converterParaDolar(
                        mPresenter.getValorTotalDevido().doubleValue()));
        adapter = new RecebimentoAdapter(mPresenter);
        rcvRecebimento.setAdapter(adapter);

        try {
            adaptadorTipoRecebimento =
                    new ArrayAdapter<>(
                            RecebimentoActivity.this,
                            android.R.layout.simple_spinner_item,
                            mPresenter.obterTipoRecebimentos(ID_TIPO_RECEBIMENTO_A_VISTA));
        } catch (Throwable throwable) {
            RecebimentoActivity.this.runOnUiThread(
                    ()->{
                        AbstractActivity.showToast(
                                RecebimentoActivity.this, throwable.getMessage());
                    });
        }

        spnTipoRecebimento.setAdapter(adaptadorTipoRecebimento);
        spnTipoRecebimento.setSelection(POSICAO_INICIAL);

        cetValorAmortizar.setText("00,00");

        cetValorAmortizar.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(final Editable s) {
                        mPresenter.setCredit(
                                new BigDecimal(
                                        s.toString().isEmpty()
                                                ? 0
                                                : cetValorAmortizar.getCurrencyDouble()));

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

                AbstractActivity.showToast(
                        mPresenter.getContext(),
                        "Imagem salva em :" + data.getStringExtra(CameraUtil.LOCAL_ONDE_A_IMAGEM_FOI_SALVA));
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

        String nomeFoto = mPresenter.getCliente().getId()
                + DateUtils.formatarDateddMMyyyyhhmmParaString(
                new Date(System.currentTimeMillis())).replace("/", "-")
                + mPresenter.getCliente().getNome();
        CameraUtil cameraUtil = new CameraUtil((Activity) mPresenter.getContext());
        cameraUtil.tirarFoto(CameraUtil.CAMINHO_IMAGEM_RECEBIMENTOS, nomeFoto);

    }

    @Override
    public void getParametros() {
        Bundle args = getIntent().getExtras();
        Cliente cliente = (Cliente) args.getSerializable("keyCliente");
        mPresenter.setCliente(cliente);
    }

    @OnClick(R.id.btnImprimirRecebimento)
    public void imprimirComprovante(View view) {
        this.mPresenter.imprimirComprovante();
        this.mPresenter.exibirBotaoFotografar();
    }

    @Override
    public void inabilitarBotaoSalvarAmortizacao() {
        this.btnSalvarRecebimento.setClickable(false);
    }

    @OnClick(R.id.btnSalvarRecebimento)
    public void salvarRecebimento() {
        if (!new ControleSessao(mPresenter.getContext()).getEnderecoBluetooth().isEmpty()) {
            mPresenter.salvarAmortizacao();
            mPresenter.atualizarRecycleView();
            mPresenter.esperarPorConexao();
        } else {
            AbstractActivity.showToast(
                    mPresenter.getContext(),
                    "Endereço MAC da impressora não encontrado.\nHabilite no Menu: Configurar impressora");
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
                mPresenter.setCredit(new BigDecimal(cetValorAmortizar.getCurrencyDouble()));
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
            cetValorAmortizar.setError("RecebimentoORM superior ao Valor Devido");
        }
    }

    @OnItemSelected(R.id.spnTipoRecebimento)
    public void setSpnTipoRecebimentoOnSelected(int position) {

        mPresenter.setIdTipoRecebimento(
                mPresenter.findIdTipoRecebimento(adaptadorTipoRecebimento.getItem(position)));
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
    public void updateRecycleView() {

        adapter.notifyDataSetChanged();
    }

    private void inicializarViews() {
        toolbar.setTitle("Recebimentos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecebimentoActivity.this);
        rcvRecebimento.setLayoutManager(layoutManager);
    }
}