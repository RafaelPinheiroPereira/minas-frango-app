package com.br.minasfrango.util;

import android.os.Environment;
import java.io.File;

public class ArquivoUtils {


    public  File[] lerFotosDoDiretorio(String nomeDiretorio){
       File  pastaPai=null;
       pastaPai = new File(Environment.getExternalStorageDirectory()+ "/Minas_Frangos/",nomeDiretorio);
       return  pastaPai.listFiles();

    }

}
