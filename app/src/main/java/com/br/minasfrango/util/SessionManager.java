package com.br.minasfrango.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.br.minasfrango.ui.activity.LoginActivity;
import java.util.HashMap;


/**
 * Created by 04717299302 on 26/08/2016.
 */
public class SessionManager  {

    private static final String PREFER_NAME = "TrinityMobilePref";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_MATRICULA = "matricula";

    public static final String KEY_SENHA = "senha";

    public static final String KEY_NOME = "nome";

    public static final String KEY_ENDERECO_BLUETOOTH = "device_address";

    int PRIVATE_MODE = 0;

    Context context;

    SharedPreferences.Editor editor;

    SharedPreferences pref;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */

    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public String getEnderecoBluetooth() {
        pref = this.context.getSharedPreferences(PREFER_NAME, 0);
        return (pref.getString(KEY_ENDERECO_BLUETOOTH, ""));

    }

    public void createUserLoginSession(String matricula, String senha, String nome) {

        editor.putBoolean(IS_USER_LOGIN, true);

        // salva matricula
        editor.putString(KEY_MATRICULA, matricula);

        // salva senha
        editor.putString(KEY_SENHA, senha);
        editor.putString(KEY_NOME, nome);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_MATRICULA, pref.getString(KEY_MATRICULA, null));

        user.put(KEY_SENHA, pref.getString(KEY_SENHA, null));

        user.put(KEY_NOME, pref.getString(KEY_NOME, null));

        // return user
        return user;
    }

    public void salvarEnderecoBluetooth(String endereco) {
        editor.putString(KEY_ENDERECO_BLUETOOTH, endereco);
        // commit changes
        editor.commit();
    }
    public int getUserID() {
        pref = this.context.getSharedPreferences(PREFER_NAME, 0);
        return Integer.parseInt(pref.getString(KEY_MATRICULA, ""));
    }



    public String getUserName() {
        pref = this.context.getSharedPreferences(PREFER_NAME, 0);
        return pref.getString(KEY_NOME, "");
    }

    // Check for login

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    /**
     * Clear session details
     */

    public void logout() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }
}
