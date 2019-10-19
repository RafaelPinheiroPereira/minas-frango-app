package com.br.minasfrango.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import com.hussain_chachuliya.customcamera.CustomCamera;

public class CameraUtil {

    Context mContext;

    public CameraUtil(final Context context) {
        mContext = context;
    }

    public void inicializarCamera(String caminhoDaImagem, String nomeImagem) {
        CustomCamera.init()
                .with((Activity) this.mContext)
                .setRequiredMegaPixel(1.5f)
                .setPath(
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/Minas Frango/" + caminhoDaImagem)
                .setImageName(nomeImagem
                )
                .start();
    }

}
