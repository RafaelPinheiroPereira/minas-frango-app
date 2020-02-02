package com.br.minasfrango.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.ui.mvp.venda.IVendaMVP;
import java.math.BigDecimal;
import java.util.List;

public class AlertDialogItemPedido {

    ArrayAdapter<String> adapterUnidade;

    @BindView(R.id.btnCancelDialog)
    Button btnCancelDialog;

    @BindView(R.id.btnSaveDialog)
    Button btnSaveDialog;

    @BindView(R.id.cetPriceDialog)
    CurrencyEditText cetPriceDialog;

    @BindView(R.id.edtQTDProductDialog)
    CurrencyEditText edtQTDProductDialog;

    @BindView(R.id.edtQTDBicoDialog)
    EditText edtQTDBicoDialog;

    IVendaMVP.IPresenter mIPresenter;

    int position;

    @BindView(R.id.spnUnitDialog)
    Spinner spnUnitDialog;

    @BindView(R.id.txtNameProductDialog)
    TextView txtNameProductDialog;

    @BindView(R.id.txtProductIDDialog)
    TextView txtProductIDDialog;

    public AlertDialogItemPedido(final IVendaMVP.IPresenter IPresenter) {
        mIPresenter = IPresenter;
    }

    public AlertDialog builder(int position) {

        this.position = position;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mIPresenter.getContext());
        LayoutInflater inflater = ((Activity) mIPresenter.getContext()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_item_pedido, null);

        ButterKnife.bind(this, dialogView);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edição do Item");

        if (mIPresenter.getFuncionario().getAlteraPreco().equals(ConstantsUtil.TEM_PERMISSAO_PARA_ALTERAR_PRECO)) {
            cetPriceDialog.setEnabled(true);
        } else {
            cetPriceDialog.setEnabled(false);
        }

        txtProductIDDialog.setText(
                String.valueOf(mIPresenter.getItemPedido().getChavesItemPedido().getIdProduto()));
        txtNameProductDialog.setText(mIPresenter.getItemPedido().getDescricao());
        cetPriceDialog.setText(
                FormatacaoMoeda.converterParaDolar(
                        mIPresenter.getItemPedido().getValorUnitario()));
        edtQTDProductDialog.setText( FormatacaoMoeda.converterParaDolar((mIPresenter.getItemPedido().getQuantidade())));
        edtQTDBicoDialog.setText(String.valueOf(mIPresenter.getItemPedido().getBicos()));

        List<Unidade> unidades = mIPresenter.carregarUnidades();
        adapterUnidade =
                new ArrayAdapter<>(
                        mIPresenter.getContext(),
                        android.R.layout.simple_spinner_item,
                        mIPresenter.carregarUnidadesEmString(unidades));

        spnUnitDialog.setAdapter(adapterUnidade);
        // Seta o spinner com a unidade do item e nao a padrao
        spnUnitDialog.setSelection(
                adapterUnidade.getPosition(
                        mIPresenter.getItemPedido().getChavesItemPedido().getIdUnidade()));

        return dialogBuilder.create();
    }

    @OnClick(R.id.btnCancelDialog)
    public void onBtnCancelDialogClicked(View view) {
        mIPresenter.dissmis();
    }

    @OnClick(R.id.btnSaveDialog)
    public void onBtnSaveDialogClicked(View view) {

        mIPresenter
                .getItemPedido()
                .setQuantidade(edtQTDProductDialog.getCurrencyDouble());
        mIPresenter
                .getItemPedido()
                .setBicos(Integer.parseInt(edtQTDBicoDialog.getText().toString()));
        mIPresenter.getItemPedido().setValorUnitario(cetPriceDialog.getCurrencyDouble());
        mIPresenter
                .getItemPedido()
                .setValorTotal(
                        edtQTDProductDialog.getCurrencyDouble()
                                * cetPriceDialog.getCurrencyDouble());
        mIPresenter.getItens().set(position, mIPresenter.getItemPedido());
        mIPresenter.setTotalDaVenda(new BigDecimal(mIPresenter.calcularTotalDaVenda()));

        mIPresenter.dissmis();
    }

    @OnItemSelected(R.id.spnUnitDialog)
    public void setSpnUnityOnSelected(int position) {
        mIPresenter
                .getItemPedido()
                .getChavesItemPedido()
                .setIdUnidade(adapterUnidade.getItem(position));

        /**Não sera mais necessário... visto que se o usuário tiver a permissão para alterar o preço
         * ele pode modifica-lo, caso não tenha, o padrão é zerar os valores */
       /* mIPresenter
                .getItemPedido()
                .setValorUnitario(
                        mIPresenter
                                .pesquisarPrecoDaUnidadePorProduto(adapterUnidade.getItem(position))
                                .getValor());*/
        //cetPriceDialog.setText(FormatacaoMoeda.converterParaDolar(mIPresenter.getItemPedido().getValorUnitario()));
    }
}