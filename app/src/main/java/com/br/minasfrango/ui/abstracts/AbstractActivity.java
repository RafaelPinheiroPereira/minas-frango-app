package com.br.minasfrango.ui.abstracts;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AbstractActivity extends AppCompatActivity {

    public static void navigateToActivity(Context context,Intent intent) {
         context.startActivity(intent);
    }
    public static void showToast(Context context ,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
