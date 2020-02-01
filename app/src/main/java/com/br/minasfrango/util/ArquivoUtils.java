package com.br.minasfrango.util;

import android.app.Activity;
import android.os.Environment;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import java.io.File;

public class ArquivoUtils {

    public void criarPastas(Activity activity) {

        String state = Environment.getExternalStorageState();
        File filesDirVendas, filesDirPagamentos;
        // Make sure it's available
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDirPagamentos =
                    new File(
                            Environment.getExternalStorageDirectory() + "/Minas_Frangos/",
                            ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS);
            filesDirVendas =
                    new File(
                            Environment.getExternalStorageDirectory() + "/Minas_Frangos/",
                            ConstantsUtil.CAMINHO_IMAGEM_VENDAS);
        } else {
            filesDirPagamentos =
                    new File(
                            activity.getExternalFilesDir(null),
                            "/Minas_Frangos/" + ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS);
            filesDirVendas =
                    new File(
                            activity.getExternalFilesDir(null),
                            "/Minas_Frangos/" + ConstantsUtil.CAMINHO_IMAGEM_VENDAS);
        }

        if (!filesDirVendas.exists()) filesDirVendas.mkdirs();
        if (!filesDirPagamentos.exists()) filesDirPagamentos.mkdirs();


        if(!filesDirVendas.exists()||!filesDirVendas.exists()  ){
            AbstractActivity.showToast(activity.getApplicationContext(),"Não foi posível criar as pastas de imagens.");
        }else   AbstractActivity.showToast(activity.getApplicationContext(),"Pastas criadas com sucesso.");

    }

    public File[] lerFotosDoDiretorio(String nomeDiretorio) {
        File pastaPai = null;
        pastaPai =
                new File(
                        Environment.getExternalStorageDirectory() + "/Minas_Frangos/",
                        nomeDiretorio);
        return pastaPai.listFiles();
    }
}
