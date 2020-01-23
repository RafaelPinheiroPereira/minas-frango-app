package com.br.minasfrango.ui.mvp.configuracao;

import android.content.Context;
import com.br.minasfrango.data.model.Configuracao;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;

public interface IConfiguracaoMVP {

    interface IPresenter {

        Context getContext();


        public void solicitarLoginGoogleDrive();
        String statusSistema();

        public void realizarConfiguracao();

        void salvarConfiguracoes(final Configuracao configuracao);

        String getMac();

        String getCnpj();

        void setMac(String mac);

        void setCnpj(String cnpj);

        GoogleSignInClient getmGoogleSignInClient();

        void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient);

        GoogleAccountCredential getCredential();

        void setCredential(GoogleAccountCredential credential);

        Drive getGoogleDriveService();

        void setGoogleDriveService(Drive googleDriveService);
    }

    interface IModel {



        String statusSistema();

        void salvarConfiguracao(final Configuracao configuracao);
    }

    interface IView {

        public boolean estaVazioOCNPJ();

        public  void solicitarLoginGoogleDrive();
    }
}
