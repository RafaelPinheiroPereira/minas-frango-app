package com.br.minasfrango.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.br.minasfrango.R;
import com.br.minasfrango.model.ErrorResponse;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.service.LoginService;
import com.br.minasfrango.util.HttpConstant;
import com.br.minasfrango.util.RetrofitConfig;
import com.br.minasfrango.util.SessionManager;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    SessionManager session;

    LinearLayout lnLoginBox;

    Button btnSubmit;

    ImageView imgLogo;

    EditText edtUserId, edtPassword;



    private static int REQUEST_STORAGE = 112;

    private static String[] PERMISSIONS = {Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        verifyPermission();
        loadAnimation();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());
        btnSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.submit:
                //validar forms
                if(validateForm()){
                    // realizar login
                    try {
                        LoginTask loginTask = new LoginTask(LoginActivity.this);
                        loginTask.execute();

                    } catch (final Exception e) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
                break;
        }
    }

    private boolean validateForm() {
           if(TextUtils.isEmpty(edtUserId.getText().toString())) {
               edtUserId.setError("Matricula Obrigatória!");
               edtUserId.requestFocus();
               return false;
           }
           if(TextUtils.isEmpty(edtPassword.getText().toString())){
               edtPassword.setError("Senha Obrigatória!");
               edtPassword.requestFocus();
               return false;
           }

            return  true;
    }

    private void loadAnimation() {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                lnLoginBox.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                lnLoginBox.startAnimation(animFade);
                btnSubmit.startAnimation(animFade);

            }
        });
        imgLogo.startAnimation(animTranslate);
    }

    private void initView() {
        lnLoginBox = findViewById(R.id.LoginBox);
        btnSubmit = findViewById(R.id.submit);
        lnLoginBox.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        imgLogo = findViewById(R.id.logo);
        edtUserId = findViewById(R.id.edtUser);
        edtUserId.setText("1");
        edtPassword = findViewById(R.id.edtSenha);
        edtPassword.setText("1234");
    }

    private void verifyPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                    // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                } else {
                    // Solicita a permissao
                    ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, REQUEST_STORAGE);
                }
            }
        } else {
            //ver  o que fazer aqui
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão para acessar Arquivo de Dados concedida", Toast.LENGTH_LONG).show();

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    }


    private class LoginTask extends AsyncTask<Void, Void, String>  {

        Context ctx;

        ProgressDialog progressDialog;

        public LoginTask(final Context ctx) {
            this.ctx = ctx;

        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Realizando Login...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String islogin) {
            super.onPostExecute(islogin);
            progressDialog.dismiss();
            if(islogin.equals("SUCESS")) {
                startActivity(new Intent(getApplicationContext(), ClienteActivity.class));
            }else if (islogin.equals("UNAUTHORIZED")){
                Toast.makeText(this.ctx,"Matricula/Senha inválidos!",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.ctx,islogin,Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                return validarAcesso(edtUserId, edtPassword);
            } catch (IOException e) {
                return e.getMessage();
            }


        }

        private String validarAcesso(final EditText edtMatricula, final EditText edtSenha) throws IOException {
            // tenho que chamar a api/rest e validar se der tudo certo crio a sessao do usuario

            LoginService loginService = new RetrofitConfig().getLoginService();
            Call<Funcionario> autenticaLoginCall = loginService
                    .autenticaLogin(edtSenha.getText().toString(), Long.parseLong(edtMatricula.getText().toString()));

            Response<Funcionario> response = autenticaLoginCall.execute();


                switch (response.code()) {
                    case HttpConstant
                            .OK:
                        Funcionario funcionario = response.body();
                        session.createUserLoginSession(edtMatricula.getText().toString(),
                                edtSenha.getText().toString(),
                                funcionario.getNome());
                        return "SUCESS";

                    case HttpConstant
                            .UNAUTHORIZED:
                        return "UNAUTHORIZED";


                }


          throw new IOException("Funcionário não cadastrado!");
        }
    }


}

		

