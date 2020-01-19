package com.br.minasfrango.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import com.br.minasfrango.BuildConfig;
import java.io.File;
import java.io.IOException;

public class CameraUtil {

    public static String LOCAL_ONDE_A_IMAGEM_FOI_SALVA = "";

    public static final int RESULTADO_INTENCAO_FOTO = 72;

    public static final String CAMINHO_IMAGEM_VENDAS = "Comprovantes-Vendas";

    public static final String CAMINHO_IMAGEM_RECEBIMENTOS = "Comprovantes-Recebimentos";

    Activity mActivity;

    public CameraUtil(final Activity activity) {
        mActivity = activity;
    }

    public void tirarFoto(String nomeDiretorio, String nomeFoto) throws IOException {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File output = null;

        if (VERSION.SDK_INT >= VERSION_CODES.N) {

            File dir = this.mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            output =new File(dir + "/Minas_Frango/" + nomeDiretorio, nomeFoto + ".jpeg");

            Uri fotoURI =
                    FileProvider.getUriForFile(
                            this.mActivity, BuildConfig.APPLICATION_ID + ".provider", output);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);

        } else {
            File dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            output = new File(dir + "/Minas_Frango/" + nomeDiretorio, nomeFoto + ".jpeg");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        }

        LOCAL_ONDE_A_IMAGEM_FOI_SALVA = output.getPath();

        mActivity.startActivityForResult(i, RESULTADO_INTENCAO_FOTO);
    }
}
