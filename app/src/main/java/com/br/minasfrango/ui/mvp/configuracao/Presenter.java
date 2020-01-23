package com.br.minasfrango.ui.mvp.configuracao;

import android.content.Context;

import com.br.minasfrango.data.model.Configuracao;
import com.br.minasfrango.network.tarefa.ConfiguracaoTask;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IModel;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IPresenter;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IView;
import com.br.minasfrango.util.ConstantsUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.br.minasfrango.util.ConstantsUtil.*;
import static com.br.minasfrango.util.ConstantsUtil.REQUEST_CODE_SIGN_IN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Presenter implements IPresenter {
    IView mView;
    IModel mIModel;

    String mac;
    String cnpj;


    private GoogleSignInClient mGoogleSignInClient;
    GoogleAccountCredential credential;
    Drive googleDriveService;



    private ConfiguracaoTask mConfiguracaoTask;

    public Presenter(final IView view) {
        mView = view;
        mIModel = new Model(this);
    }


    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

    @Override
    public void solicitarLoginGoogleDrive() {
        this.mView.solicitarLoginGoogleDrive();
    }


    @Override
    public String statusSistema() {
        return this.mIModel.statusSistema();
    }

    @Override
    public void realizarConfiguracao() {
        mConfiguracaoTask = new ConfiguracaoTask(this);
        mConfiguracaoTask.execute();


    }

    @Override
    public void salvarConfiguracoes(final Configuracao configuracao) {
        this.mIModel.salvarConfiguracao(configuracao);
    }


    @Override
    public String getMac() {
        return mac;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public void setMac(final String mac) {
        this.mac = mac;
    }

    @Override
    public void setCnpj(final String cnpj) {
        this.cnpj = cnpj;
    }


    @Override
    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }


    @Override
    public GoogleAccountCredential getCredential() {
        return credential;
    }

    @Override
    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }
    @Override
    public Drive getGoogleDriveService() {
        return googleDriveService;
    }
    @Override
    public void setGoogleDriveService(Drive googleDriveService) {
        this.googleDriveService = googleDriveService;
    }
}
