package com.br.minasfrango.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IPresenter;

public class AlertDialogClient {

    IHomeMVP.IPresenter mIPresenter;

    @BindView(R.id.txtClientID)
    TextView txtClientID;

    @BindView(R.id.txtSocialName)
    TextView txtSocialName;

    @BindView(R.id.txtOpenNotes)
    TextView txtOpenNotes;

    @BindView(R.id.txtFanstasyName)
    TextView txtFanstasyName;

    @BindView(R.id.txtObs)
    TextView txtObs;

    @BindView(R.id.txtRg)
    TextView txtRg;

    @BindView(R.id.txtEndereco)
    TextView txtEndereco;

    @BindView(R.id.txtReferencia)
    TextView txtReferencia;

    @BindView(R.id.txtCPFCNPJ)
    TextView txtCPFCNPJ;

    @BindView(R.id.txtBairro)
    TextView txtBairro;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.txtContact)
    TextView txtContact;

    @BindView(R.id.txtCEP)
    TextView txtCEP;

    @BindView(R.id.txtCidade)
    TextView txtCidade;

    @BindView(R.id.txtCodeLocal)
    TextView txtCodeLocal;


    public AlertDialogClient(final IPresenter IPresenter) {
        mIPresenter = IPresenter;
    }

    public AlertDialog builder(Cliente cliente) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mIPresenter.getContext());
        LayoutInflater inflater = ((Activity) mIPresenter.getContext()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_detalhe_cliente, null);

        ButterKnife.bind(this, dialogView);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("INFORMAÇÕES DO CLIENTE");

        txtFanstasyName.setText(cliente.getNome());
        txtSocialName.setText(cliente.getRazaoSocial());
        txtClientID.setText(String.valueOf(cliente.getId()));
        txtBairro.setText(cliente.getBairro());
        txtCEP.setText(String.valueOf(cliente.getCep()));
        txtCidade.setText(cliente.getCidade());
        txtPhone.setText(cliente.getTelefone());
        return dialogBuilder.create();


    }

}
