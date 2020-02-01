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
import com.br.minasfrango.ui.mvp.recebimento.IRecebimentoMVP;
import com.br.minasfrango.util.DateUtils;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.util.Date;

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



    IRecebimentoMVP.IPresenter mPresenter;

    public RecebimentoAdapter(IRecebimentoMVP.IPresenter mPresenter) {

        this.mPresenter = mPresenter;
        this.mLayoutInflater =
                (LayoutInflater)
                        mPresenter.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getRecebimentos().size();
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {




            ((MyViewHolder) viewHolder)
                    .txtRecebimentoIDVenda.setText(
                    String.valueOf(mPresenter.getRecebimentos().get(position ).getIdVenda()));

            ((MyViewHolder) viewHolder)
                    .txtSalesDate.setText(
                    DateUtils.formatarDateddMMyyyyParaString(
                                    mPresenter
                                            .getRecebimentos()
                                            .get(position )
                                            .getDataVenda()));
            ((MyViewHolder) viewHolder)
                    .txtDateVencimento.setText(
                    mPresenter
                            .getRecebimentos()
                            .get(position )
                            .getDataVencimento()!=null?
                    DateUtils.formatarDateddMMyyyyParaString(
                                    mPresenter
                                            .getRecebimentos()
                                            .get(position )
                                            .getDataVencimento()): DateUtils.formatarDateddMMyyyyParaString(new Date()));

            ((MyViewHolder) viewHolder)
                    .txtSalesValue.setText(
                    (FormatacaoMoeda.converterParaDolar(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position )
                                    .getValorVenda())));

            ((MyViewHolder) viewHolder)
                    .txtAmortizationValue.setText(
                    (FormatacaoMoeda.converterParaDolar(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position )
                                    .getValorAmortizado())));

            if (mPresenter.getRecebimentos().get(position ).getValorVenda()
                    - mPresenter.getRecebimentos().get(position ).getValorAmortizado()
                    > 0) {
                ((MyViewHolder) viewHolder).txtSaldo.setTextColor(Color.RED);
            } else {
                ((MyViewHolder) viewHolder).txtSaldo.setTextColor(Color.GREEN);
            }
            ((MyViewHolder) viewHolder)
                    .txtSaldo.setText(
                    (FormatacaoMoeda.converterParaDolar(
                            mPresenter.getRecebimentos().get(position ).getValorVenda()
                                    - mPresenter
                                    .getRecebimentos()
                                    .get(position )
                                    .getValorAmortizado())));

            ((MyViewHolder) viewHolder)
                    .txtOrderSelect.setText(
                    String.valueOf(
                            mPresenter
                                    .getRecebimentos()
                                    .get(position )
                                    .getOrderSelected()));
            ((MyViewHolder) viewHolder)
                    .txtQDT.setText((position+1) + "/" + mPresenter.getRecebimentos().size());
            ((MyViewHolder) viewHolder)
                    .chbRecebimento.setChecked(
                    mPresenter.getRecebimentos().get(position ).isCheck());

            // Quando o tipo de amortizacao eh automatico o checkbox deve ficar desabilitado
            if (mPresenter.ehAmortizacaoAutomatica()) {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(false);

            } else if (!mPresenter.ehAmortizacaoAutomatica() && !mPresenter.getRecebimentos()
                    .get(position ).isCheck()
                    && !mPresenter.valorDoCreditoEhMaiorDoQueZero()) {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(false);
            } else {
                ((MyViewHolder) viewHolder).chbRecebimento.setClickable(true);

                ((MyViewHolder) viewHolder)
                        .chbRecebimento.setOnClickListener(
                        (view)->{

                            // Caso o valor do credito  seja maior do que zero
                            if (mPresenter.valorDoCreditoEhMaiorDoQueZero())
                            // O item ja estiver selecionado entao quer dizer que estou
                            // desmarcando
                            {
                                if (mPresenter
                                        .getRecebimentos()
                                        .get(position )
                                        .isCheck()) {
                                    mPresenter.removerAmortizacao(position );

                                }
                                // O item nao estiver selecionado entao quer dizer que estou
                                // marcando
                                else {
                                    mPresenter.calcularArmotizacaoManual(position );
                                    mPresenter.processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(position ,
                                            mPresenter.getRecebimentos().get(position ));

                                }
                            }
                            // Se o valor nao é maior do que zero eu quero desmarcar
                            else {
                                // Esta marcado entao quer dizer que ta desmarcando
                                if (mPresenter
                                        .getRecebimentos()
                                        .get(position )
                                        .isCheck()) {
                                    mPresenter.removerAmortizacao(position );
                                }
                                // Senao quer marcar porem nao ha saldo
                                else {
                                    ((MyViewHolder) viewHolder)
                                            .chbRecebimento.setClickable(false);
                                    this.mPresenter.showInsuficentCredit(
                                            "Saldo Insuficiente");
                                }
                            }
                        });
            }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

            v = mLayoutInflater.inflate(R.layout.recebimento_item, viewGroup, false);
            RecebimentoAdapter.MyViewHolder mvh = new RecebimentoAdapter.MyViewHolder(v);
            return mvh;

    }
}