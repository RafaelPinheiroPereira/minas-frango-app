package com.br.minasfrango.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.br.minasfrango.ui.activity.LoginActivity;
import java.util.HashMap;

/**
 * Created by 04717299302 on 26/08/2016.
 */
public class ControleSessao {

    private static final String PREFERENCIAS = "TrinityMobilePref";

    private static final String USUARIO_CONECTADO = "IsUserLoggedIn";

    public static final String CHAVE_MATRICULA = "matricula";

    public static final String CHAVE_SENHA = "senha";

    public static final String CHAVE_NOME = "nome";
    public static final String CHAVE_NUCLEO = "idNucleo";
    public static final String CHAVE_ID_VENDA_MAXIMA = "idVendaMaxima";
    public static final String CHAVE_ID_RECIBO_MAXIMO = "idReciboMaximo";


    public static final String CHAVE_ENDERECO_BLUETOOTH = "device_address";

    int MODO_PRIVADO = 0;

    Context contexto;

    SharedPreferences.Editor editor;

    SharedPreferences pref;

    public ControleSessao(Context contexto) {
        this.contexto = contexto;
        pref = contexto.getSharedPreferences(PREFERENCIAS, MODO_PRIVADO);
        editor = pref.edit();
    }

    /**
     * Check login method will check user login status If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.usuarioConectado()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(contexto, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            contexto.startActivity(i);

            return true;
        }
        return false;
    }

    public void criarSessao(long matricula, String senha, String nome, long idNucleo, long idVendaMaxima,
            final long maxIdRecibo) {

        editor.putBoolean(USUARIO_CONECTADO, true);

        // salva matricula
        editor.putLong(CHAVE_MATRICULA, matricula);

        // salva senha
        editor.putString(CHAVE_SENHA, senha);
        editor.putString(CHAVE_NOME, nome);
        editor.putLong(CHAVE_NUCLEO,idNucleo);
        editor.putLong(CHAVE_ID_VENDA_MAXIMA,idVendaMaxima);
        editor.putLong(CHAVE_ID_RECIBO_MAXIMO,maxIdRecibo);



        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getDadosDoUsuario() {

        // Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(CHAVE_MATRICULA, pref.getString(CHAVE_MATRICULA, null));

        user.put(CHAVE_SENHA, pref.getString(CHAVE_SENHA, null));

        user.put(CHAVE_NOME, pref.getString(CHAVE_NOME, null));

        user.put(CHAVE_NUCLEO,pref.getString(CHAVE_NUCLEO,null));

        // return user
        return user;
    }

    public String getEnderecoBluetooth() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return (pref.getString(CHAVE_ENDERECO_BLUETOOTH, ""));
    }

    public long getIdUsuario() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getLong(CHAVE_MATRICULA, 0);
    }

    public String getUserName() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getString(CHAVE_NOME, "");
    }
    public long getIdNucleo(){
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getLong(CHAVE_NUCLEO, 0);

    }

    public long getIVendaMaxima(){
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getLong(CHAVE_ID_VENDA_MAXIMA, 0);

    }
    public long getIdReciboMaximo(){
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getLong(CHAVE_ID_RECIBO_MAXIMO, 0);

    }


    /** Clear session details */
    public void logout() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(contexto, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        contexto.startActivity(i);

    }

    // Check for login

    public void salvarEnderecoBluetooth(String endereco) {
        editor.putString(CHAVE_ENDERECO_BLUETOOTH, endereco);
        // commit changes
        editor.commit();
    }

    public boolean usuarioConectado() {
        return pref.getBoolean(USUARIO_CONECTADO, false);
    }
}
