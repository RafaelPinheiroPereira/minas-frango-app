package com.br.minasfrango.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.Toast;
import com.br.minasfrango.R;
import com.br.minasfrango.adapter.RecebimentoAdapter;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.dao.RecebimentoDAO;
import com.br.minasfrango.dao.TipoRecebimentoDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Recebimento;

import com.br.minasfrango.util.FormatacaoMoeda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecebimentoActivity extends AppCompatActivity
        implements RecyclerViewOnClickListenerHack {

    private Cliente cliente;

    Toolbar toolbar;

    private RecyclerView recebimentosRecyclerView;

    RecebimentoDAO recebimentoDAO;

    Switch amortizarSwitch;


    TipoRecebimentoDAO tipoRecebimentoDAO;

    List<Recebimento> recebimentos;

    private ArrayAdapter<String> adapterTipoRecebimento;

    Button btnPrint;

    Spinner spinnerFormaPagamento;

    EditText valorAmortizacaoEditText;

    RecebimentoAdapter adapter;

    int idTipoRecebimento;

    Button confirmaAmortizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebimento);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cliente = getParams();
        initView();
    }

    @Override
    public void onClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.chk_recebimento:
                Recebimento recebimento = adapter.getItem(position);
                CheckBox checkBox = (CheckBox) view;
                if (!checkBox.isChecked()) {
                    retiraAmortizacaoRegistro(recebimento, position);
                } else {

                    Double numberValorAmortizado = FormatacaoMoeda
                            .converteStringDoubleValorMoeda(valorAmortizacaoEditText.getText().toString());
                    if (verificaSaldo(recebimentos, numberValorAmortizado) && (numberValorAmortizado <= recebimento
                            .getValorVenda())) {

                        simularAmortizacaoRegistro(new BigDecimal(numberValorAmortizado),
                                adapter.getItem(position),
                                position);

                    } else if (!verificaSaldo(recebimentos, numberValorAmortizado)) {

                        Toast.makeText(RecebimentoActivity.this, "Saldo Insuficiente!", Toast.LENGTH_LONG).show();
                        retiraAmortizacaoRegistro(adapter.getItem(position), position);
                    }  else if(numberValorAmortizado > recebimento
                            .getValorVenda()){
                        valorAmortizacaoEditText.setError("Recebimento superior ao devido!");
                        checkBox.setChecked(false);
                    }
                    if (numberValorAmortizado == 0) {
                        valorAmortizacaoEditText.setError("Valor deve ser maior que zero!");
                        retiraAmortizacaoRegistro(adapter.getItem(position), position);
                    }


                }
                break;
        }
    }

    @Override
    public void onLongPressClickListener(final View view, final int position) {

    }

    private void initView() {

        TextView codigoClienteTextView;
        TextView nomeFantasiaTextView;
        TextView enderecoTextView;

        codigoClienteTextView = findViewById(R.id.txt_codigo_cliente);
        nomeFantasiaTextView = findViewById(R.id.txt_nome_fantasia);
        enderecoTextView = findViewById(R.id.txt_endereco);

        valorAmortizacaoEditText = findViewById(R.id.edt_amortizacao);
        spinnerFormaPagamento = findViewById(R.id.spinner_tipo_recebimento);
        confirmaAmortizacao = findViewById(R.id.btn_confirmar_amortizacao);

        codigoClienteTextView.setText(String.valueOf(cliente.getId()));
        enderecoTextView.setText(cliente.getEndereco());
        nomeFantasiaTextView.setText(cliente.getRazaoSocial());

        btnPrint = findViewById(R.id.btn_imprimir_recibo);
        amortizarSwitch = findViewById(R.id.amortizar_switch);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RECEBIMENTOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recebimentosRecyclerView = findViewById(R.id.recycleview_recebimentos);


    }

    private Cliente getParams() {
        Bundle args = getIntent().getExtras();
        Cliente cliente = (Cliente) args.getSerializable("keyCliente");
        return cliente;
    }

    @Override
    protected void onStart() {
        super.onStart();
        recebimentoDAO = RecebimentoDAO.getInstace();
        recebimentos = recebimentoDAO.recebimentosPorCliente(cliente);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RecebimentoActivity.this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        recebimentosRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecebimentoAdapter(RecebimentoActivity.this, recebimentos);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recebimentosRecyclerView.setAdapter(adapter);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();
        ArrayList<String> tiposRecebimentos = tipoRecebimentoDAO.carregaFormaPagamentoAmortizacao();

        adapterTipoRecebimento = new ArrayAdapter<String>(RecebimentoActivity.this,
                android.R.layout.simple_spinner_item, tiposRecebimentos);
        spinnerFormaPagamento.setAdapter(adapterTipoRecebimento);
        spinnerFormaPagamento.setSelection(0);

        valorAmortizacaoEditText.setText("0,00");
        spinnerFormaPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idTipoRecebimento = tipoRecebimentoDAO
                        .codigoFormaPagamento((String) parent.getAdapter().getItem(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amortizarSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                if (b) {
                    simularAmortizacaoTodosRegistros(valorAmortizacaoEditText.getText().toString(), recebimentos);
                }else{
                    valorAmortizacaoEditText.setText("0,00");
                    simularAmortizacaoTodosRegistros(valorAmortizacaoEditText.getText().toString(), recebimentos);
                }
            }
        });

        valorAmortizacaoEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_DEL == keyEvent.getKeyCode()) {

                    amortizarSwitch.setChecked(false);
                    return false;
                }

                return false;
            }
        });
        confirmaAmortizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simularAmortizacaoTodosRegistros(valorAmortizacaoEditText.getText().toString(), recebimentos);
            }
        });

    }


    private void retiraAmortizacaoRegistro(Recebimento recebimento, int position) {
        Recebimento entityToUpdate = recebimentoDAO.findByID(recebimento.getId());
        adapter.updateRetiraAmortizacao(entityToUpdate, position);
        adapter.notifyDataSetChanged();

    }


    private BigDecimal simularAmortizacaoRegistro(BigDecimal valorAmortizado, Recebimento recebimento,
            int position) {
        BigDecimal saldo = null;
        saldo = valorAmortizado;
        if (saldo.compareTo(new BigDecimal(0.000)) > 0) {

            BigDecimal valBigVenda = new BigDecimal(recebimento.getValorVenda());
            saldo = valorAmortizado.subtract(valBigVenda);
            if (saldo.compareTo(new BigDecimal(0.000)) >= 0) {
                recebimento.setValorAmortizado(recebimento.getValorVenda());

            } else {
                recebimento.setValorAmortizado(valorAmortizado.doubleValue());

            }
            recebimento.setTipoRecebimento(idTipoRecebimento);
            //  recebimentoDAO.updateRecebimento(recebimentos.get(i));
            adapter.updateAmortizacao(recebimento, position);
            adapter.notifyDataSetChanged();
        }

        return saldo;
    }


    private void simularAmortizacaoTodosRegistros(String valorAmortizacao, List<Recebimento> recebimentos) {

        int i = 0;

        BigDecimal valorTotalDevido = null;
        BigDecimal bigValorAmortizado = null;
        Double somaTotalDevida = 0.00;

        for (Recebimento recebimento : recebimentos) {
            somaTotalDevida += recebimento.getValorVenda();
        }

        Double numberValorAmortizado = FormatacaoMoeda.converteStringDoubleValorMoeda(valorAmortizacao);

        if (numberValorAmortizado == 0) {
            for (int k = 0; k < recebimentos.size(); k++) {
                retiraAmortizacaoRegistro(recebimentos.get(k), k);
            }
        } else {
            bigValorAmortizado = BigDecimal.valueOf(numberValorAmortizado);
            valorTotalDevido = BigDecimal.valueOf(somaTotalDevida);
            if (bigValorAmortizado.compareTo(valorTotalDevido) <= 0) {
                while (bigValorAmortizado.compareTo(new BigDecimal(0.000)) > 0) {
                    bigValorAmortizado = simularAmortizacaoRegistro(bigValorAmortizado, recebimentos.get(i), i);
                    i++;
                }
            } else {
                valorAmortizacaoEditText.setError("Valor recebido superou o devido");
            }
        }


    }

    private boolean verificaSaldo(List<Recebimento> recebimentos, double valorAmortizado) {
        Double valorTotalDevido = 0.0;
        for (Recebimento aux : recebimentos) {
            if (aux.isCheck()) {
                valorTotalDevido += aux.getValorVenda();
            }
        }
        if (valorTotalDevido - valorAmortizado >= 0) {
            return false;
        }

        return true;
    }
}
