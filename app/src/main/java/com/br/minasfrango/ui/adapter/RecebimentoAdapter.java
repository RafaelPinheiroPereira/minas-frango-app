package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.mvp.recebimento.IPaymentsMVP;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.text.SimpleDateFormat;

public class RecebimentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class Header extends RecyclerView.ViewHolder {

        public Header(@NonNull final View itemView) {
            super(itemView);
        }
    }

    private LayoutInflater mLayoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chbRecebimento)
        CheckBox chbRecebimento;

        @BindView(R.id.txtAmortizationValue)
        TextView txtAmortizationValue;

        @BindView(R.id.txtDateVencimento)
        TextView txtDateVencimento;

        @BindView(R.id.txtOrderSelect)
        TextView txtOrderSelect;

        @BindView(R.id.txtQDT)
        TextView txtQDT;

        @BindView(R.id.txtRecebimentoIDVenda)
        TextView txtRecebimentoIDVenda;

        @BindView(R.id.txtSaldo)
        TextView txtSaldo;

        @BindView(R.id.txtSalesDate)
        TextView txtSalesDate;

        @BindView(R.id.txtSalesValue)
        TextView txtSalesValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static final int ITEM_VIEW_TYPE_HEADER = 0;

    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    IPaymentsMVP.IPresenter mPresenter;

    public RecebimentoAdapter(IPaymentsMVP.IPresenter mPresenter) {

        this.mPresenter = mPresenter;
        this.mLayoutInflater =
                (LayoutInflater)
                        mPresenter.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getRecebimentos().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (isHeader(position)) {
            return;
        } else {

            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            ((MyViewHolder) viewHolder)
                    .txtRecebimentoIDVenda.setText(
                    String.valueOf(mPresenter.getRecebimentos().get(position - 1).getIdVenda()));

            ((MyViewHolder) viewHolder)
                    .txtSalesDate.setText(
                    String.valueOf(
                            formatador.format(
                                    mPresenter
                                            .getRecebimentos()
                                            .get(position - 1)
                                            .getDataVenda())));
            ((MyViewHolder) viewHolder)
                    .txtDateVencimento.setText(
                    String.valueOf(
                            formatador.format(
                                    mPresenter
                                            .getRecebimentos()
                                            .get(position - 1)
                                            .getDataVencimento())));

            ((MyViewHolder) viewHolder)
                    .txtSalesValue.setText(
                    (FormatacaoMoeda.convertDoubleToString(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position - 1)
                                    .getValorVenda())));

            ((MyViewHolder) viewHolder)
                    .txtAmortizationValue.setText(
                    (FormatacaoMoeda.convertDoubleToString(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position - 1)
                                    .getValorAmortizado())));

            if (mPresenter.getRecebimentos().get(position - 1).getValorVenda()
                    - mPresenter.getRecebimentos().get(position - 1).getValorAmortizado()
                    > 0) {
                ((MyViewHolder) viewHolder).txtSaldo.setTextColor(Color.RED);
            } else {
                ((MyViewHolder) viewHolder).txtSaldo.setTextColor(Color.GREEN);
            }
            ((MyViewHolder) viewHolder)
                    .txtSaldo.setText(
                    (FormatacaoMoeda.convertDoubleToString(
                            mPresenter.getRecebimentos().get(position - 1).getValorVenda()
                                    - mPresenter
                                    .getRecebimentos()
                                    .get(position - 1)
                                    .getValorAmortizado())));

            ((MyViewHolder) viewHolder)
                    .txtOrderSelect.setText(
                    String.valueOf(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position - 1)
                                    .getOrderSelected()));
            ((MyViewHolder) viewHolder)
                    .txtQDT.setText((position) + "/" + mPresenter.getRecebimentos().size());
            ((MyViewHolder) viewHolder)
                    .chbRecebimento.setChecked(
                    mPresenter.getRecebimentos().get(position - 1).isCheck());

            // Quando o tipo de amortizacao eh automatico o checkbox deve ficar desabilitado
            if (mPresenter.isAmortizacaoAutomatica()) {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(false);

            } else if (!mPresenter.isAmortizacaoAutomatica() && !mPresenter.getRecebimentos()
                    .get(position - 1).isCheck()
                    && !mPresenter.valorCreditoEhMaiorQueZero()) {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(false);
            } else {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(true);

                ((MyViewHolder) viewHolder)
                        .chbRecebimento.setOnClickListener(
                        (view)->{

                            // Caso o valor do credito  seja maior do que zero
                            if (mPresenter.valorCreditoEhMaiorQueZero())
                            // O item ja estiver selecionado entao quer dizer que estou
                            // desmarcando
                            {
                                if (mPresenter
                                        .getRecebimentos()
                                        .get(position - 1)
                                        .isCheck()) {
                                    mPresenter.removerAmortizacao(position - 1);

                                }
                                // O item nao estiver selecionado entao quer dizer que estou
                                // marcando
                                else {
                                    mPresenter.calcularArmotizacaoManual(position - 1);
                                    mPresenter.processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(position - 1,
                                            mPresenter.getRecebimentos().get(position - 1));

                                }
                            }
                            // Se o valor nao é maior do que zero eu quero desmarcar
                            else {
                                // Esta marcado entao quer dizer que ta desmarcando
                                if (mPresenter
                                        .getRecebimentos()
                                        .get(position - 1)
                                        .isCheck()) {
                                    mPresenter.removerAmortizacao(position - 1);
                                }
                                // Senao quer marcar porem nao ha saldo
                                else {
                                    ((MyViewHolder) viewHolder)
                                            .chbRecebimento.setClickable(false);
                                    this.mPresenter.exibirMensagemDeSaldoInsuficiente(
                                            "Saldo Insuficiente");
                                }
                            }
                        });
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View headerView =
                    mLayoutInflater.inflate(R.layout.header_recebimento, viewGroup, false);
            return new RecebimentoAdapter.Header(headerView);
        } else {
            v = mLayoutInflater.inflate(R.layout.recebimento_item, viewGroup, false);
            RecebimentoAdapter.MyViewHolder mvh = new RecebimentoAdapter.MyViewHolder(v);
            return mvh;
        }
    }
}
