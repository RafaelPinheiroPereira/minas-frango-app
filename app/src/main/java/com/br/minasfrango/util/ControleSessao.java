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

    public void criarSessao(String matricula, String senha, String nome) {

        editor.putBoolean(USUARIO_CONECTADO, true);

        // salva matricula
        editor.putString(CHAVE_MATRICULA, matricula);

        // salva senha
        editor.putString(CHAVE_SENHA, senha);
        editor.putString(CHAVE_NOME, nome);

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

        // return user
        return user;
    }

    public String getEnderecoBluetooth() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return (pref.getString(CHAVE_ENDERECO_BLUETOOTH, ""));
    }

    public int getIdUsuario() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return Integer.parseInt(pref.getString(CHAVE_MATRICULA, ""));
    }

    public String getUserName() {
        pref = this.contexto.getSharedPreferences(PREFERENCIAS, 0);
        return pref.getString(CHAVE_NOME, "");
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