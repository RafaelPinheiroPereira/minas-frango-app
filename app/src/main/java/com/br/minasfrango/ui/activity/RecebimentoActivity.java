package com.br.minasfrango.ui.activity;

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
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.RecebimentoAdapter;
import com.br.minasfrango.ui.mvp.payments.IPaymentsMVP;
import com.br.minasfrango.ui.mvp.payments.Presenter;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.math.BigDecimal;

public class RecebimentoActivity extends AppCompatActivity implements IPaymentsMVP.IView {

    private static final int INITIAL_POSITION = 0;

    ArrayAdapter<String> adapterTipoRecebimento;

    @BindView(R.id.btnImprimirRecebimento)
    Button btnImprimirRecebimento;

    @BindView(R.id.btnSalvarRecebimento)
    Button btnSalvarRecebimento;

    @BindView(R.id.edtValueAmortize)
    CurrencyEditText edtValueAmortize;

    IPaymentsMVP.IPresenter mPresenter;

    @BindView(R.id.rcvRecebimento)
    RecyclerView rcvRecebimento;

    @BindView(R.id.spnTipoRecebimento)
    Spinner spnTipoRecebimento;

    @BindView(R.id.swtcAmortize)
    Switch swtcAmortize;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtClientID)
    TextView txtClientID;

    @BindView(R.id.txtEndereco)
    TextView txtEndereco;

    @BindView(R.id.txtFanstasyName)
    TextView txtFantasyName;

    @BindView(R.id.txtQTDNotasAbertas)
    TextView txtQTDNotasAbertas;

    RecebimentoAdapter adapter;

    @BindView(R.id.txtTotalDevido)
    TextView txtTotalDevido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebimento);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = new Presenter(this);
        mPresenter.getParams();
        mPresenter.setClientViews();

        // Inicia

        swtcAmortize.setChecked(false);
        mPresenter.getRecebimentos().addAll(mPresenter.loadReceiptsByClient());
        txtQTDNotasAbertas.setText("Notas Abertas: " + mPresenter.getRecebimentos().size());

        txtTotalDevido.setText(
                FormatacaoMoeda.convertDoubleToString(
                        mPresenter.getValueTotalDevido().doubleValue()));
        adapter = new RecebimentoAdapter(mPresenter);
        rcvRecebimento.setAdapter(adapter);

        try {
            adapterTipoRecebimento =
                    new ArrayAdapter<>(
                            RecebimentoActivity.this,
                            android.R.layout.simple_spinner_item,
                            mPresenter.loadTipoRecebimentosAVista());
        } catch (Throwable throwable) {
            RecebimentoActivity.this.runOnUiThread(
                    ()->{
                        AbstractActivity.showToast(
                                RecebimentoActivity.this, throwable.getMessage());
                    });
        }

        spnTipoRecebimento.setAdapter(adapterTipoRecebimento);
        spnTipoRecebimento.setSelection(INITIAL_POSITION);
        edtValueAmortize.setText("00,00");

        edtValueAmortize.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(final Editable s) {
                        mPresenter.setCredit(
                                new BigDecimal(
                                        s.toString().isEmpty()
                                                ? 0
                                                : edtValueAmortize.getCurrencyDouble()));

                        mPresenter.getRecebimentos().clear();
                        mPresenter.getRecebimentos().addAll(mPresenter.loadReceiptsByClient());
                        mPresenter.updateRecycleView();

                        if (mPresenter.totalValueOfDebtISLessTranCreditOrEquals()) {
                            if (mPresenter.creditValueIsGranThenZero()) {
                                /*Se o tipo da quitacao for automatica e o valor de credito for
                                 * maior do que zero entao calcula automaticamente a amortizacao*/
                                if (mPresenter.isTypeOfAmortizationIsAutomatic()) {
                                    mPresenter.calcularAmortizacaoAutomatica();
                                }

                            } else {
                                AbstractActivity.showToast(
                                        mPresenter.getContext(), "Saldo Insuficiente!");
                            }

                        } else {
                            // Devo informar que o valor recebido eh maior do que o devido
                            edtValueAmortize.setError("Recebimento superior ao Valor Devido");
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
    public void atualizarViewSaldoDevedor() {

        txtTotalDevido.setTextColor(
                mPresenter.saldoDevidoEhMaiorQueZero() ? Color.RED : Color.GREEN);
        txtTotalDevido.setText(
                FormatacaoMoeda.convertDoubleToString(
                        mPresenter
                                .getValueTotalDevido()
                                .subtract(mPresenter.getValorTotalAmortizado())
                                .doubleValue()));
    }

    @OnClick(R.id.btnSalvarRecebimento)

    public void btnConfirmeAmortizeOnClicked() {
        mPresenter.salvarAmortizacao();
        mPresenter.exibirBotaoGerarRecibo();
        mPresenter.updateRecycleView();

    }

    @Override
    public void getParams() {
        Bundle args = getIntent().getExtras();
        Cliente cliente = (Cliente) args.getSerializable("keyCliente");
        mPresenter.setCliente(cliente);
    }



    @Override
    public void setClientViews() {
        txtClientID.setText(String.valueOf(mPresenter.getCliente().getId()));
        txtEndereco.setText(mPresenter.getCliente().getEndereco());
        txtFantasyName.setText(mPresenter.getCliente().getNome());
    }

    @OnCheckedChanged(R.id.swtcAmortize)
    public void setOnSwtcAmortize(CompoundButton compoundButton, boolean b) {

        // Se o valor devido eh maior ou igual ao credito
        if (mPresenter.totalValueOfDebtISLessTranCreditOrEquals()) {

            if (b) {
                mPresenter.getRecebimentos().clear();
                mPresenter.getRecebimentos().addAll(mPresenter.loadReceiptsByClient());
                mPresenter.updateRecycleView();
                mPresenter.setTypeOfAmortizationIsAutomatic(true);
                mPresenter.setCredit(new BigDecimal(edtValueAmortize.getCurrencyDouble()));
                mPresenter.calcularAmortizacaoAutomatica();
                compoundButton.setText("Quitação Automática de Notas");
                mPresenter.atualizarViewSaldoDevedor();
            } else {
                // REALIZA A QUITACAO MANUAL
                mPresenter.getRecebimentos().addAll(mPresenter.loadReceiptsByClient());
                mPresenter.updateRecycleView();
                mPresenter.setTypeOfAmortizationIsAutomatic(false);
                edtValueAmortize.setText("00,00");
                compoundButton.setText("Quitação Manual de Notas");
                mPresenter.atualizarViewSaldoDevedor();
            }
        } else {
            // Devo informar que o valor recebido eh maior do que o devido
            edtValueAmortize.setError("Recebimento superior ao Valor Devido");
        }
    }

    @OnItemSelected(R.id.spnTipoRecebimento)
    public void setSpnTipoRecebimentoOnSelected(int position) {

        mPresenter.setIdTipoRecebimento(
                mPresenter.findIdTipoRecebimento(adapterTipoRecebimento.getItem(position)));
    }

    @Override
    public void exibirBotaoGerarRecibo() {
        btnImprimirRecebimento.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInsuficentCredit(final String s) {
        AbstractActivity.showToast(mPresenter.getContext(), s);
    }

    @Override
    public void updateRecycleView() {

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateRecycleViewAlteredItem(final int position) {
        adapter.notifyItemChanged(position);
    }

    private void initView() {
        toolbar.setTitle("Recebimentos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecebimentoActivity.this);
        rcvRecebimento.setLayoutManager(layoutManager);
    }
}
