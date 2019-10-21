package com.br.minasfrango.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;

public class CameraUtil {

    public static String LOCAL_ONDE_A_IMAGEM_FOI_SALVA = "";

    public static final int RESULTADO_INTENCAO_FOTO = 72;

    public static final String CAMINHO_IMAGEM_VENDAS = "Comprovantes-Vendas";

    public static final String CAMINHO_IMAGEM_RECEBIMENTOS = "Comprovantes-Recebimentos";

    Activity mActivity;

    public CameraUtil(final Activity activity) {
        mActivity = activity;
    }

    public void tirarFoto(String nomeDiretorio, String nomeFoto) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File output = null;
        output = new File(dir + "/Minas Frango/" + nomeDiretorio, nomeFoto + ".jpeg");
        LOCAL_ONDE_A_IMAGEM_FOI_SALVA = output.getPath();
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

        mActivity.startActivityForResult(i, RESULTADO_INTENCAO_FOTO);
    }
}
