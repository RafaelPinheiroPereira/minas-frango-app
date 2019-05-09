package com.br.minasfrango.ui.activity;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IPresenter;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IView;
import com.br.minasfrango.ui.mvp.login.Presenter;

public class LoginActivity extends AppCompatActivity implements IView {

    private static String[] PERMISSOES = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.INTERNET,
            permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION,
    };

    private static int REQUEST_STORAGE = 112;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.edtUser)
    EditText edtUser;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.lnLoginBox)
    LinearLayout lnLoginBox;

    IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        // Internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Init
        presenter = new Presenter(this);
        presenter.iniciarAnimacaoTela();
        presenter.verificarPermissoes();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AbstractActivity.showToast(
                        presenter.getContext(), "Permissões necessárias concedidas");

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @OnClick(R.id.btnLogin)
    public void btnSubmitClicked(View view) {
        // realizar login
        if (presenter.validarLogin()) {
            try {
                presenter.realizarLogin(
                        edtUser.getText().toString(), edtPassword.getText().toString());
            } catch (final Exception e) {
                LoginActivity.this.runOnUiThread(
                        ()->AbstractActivity.showToast(presenter.getContext(), e.getMessage()));
            }
        }
    }

    @Override
    public void iniciarAnimacao() {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        animTranslate.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        lnLoginBox.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Animation animFade =
                                AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                        lnLoginBox.startAnimation(animFade);
                        btnLogin.startAnimation(animFade);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }
                });
        imgLogo.startAnimation(animTranslate);
    }

    @Override
    public void iniciarAnimacaoTela() {
        iniciarAnimacao();
    }

    @Override
    public boolean validarEntradaDados() {

        if (TextUtils.isEmpty(edtUser.getText().toString())) {
            edtUser.setError("Matricula Obrigatória!");
            edtUser.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.setError("Senha Obrigatória!");
            edtPassword.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void verificarPermissoes() {
        verificarPermissao();
    }

    private void initView() {

        lnLoginBox.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);

        // Inicializacao a fim de testes
        edtUser.setText("1");
        edtPassword.setText("1234");
    }

    private void verificarPermissao() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o
                    // check
                    // "nunca mais mostre este alerta"
                    // Podemos mostrar um alerta explicando para o usuário porque a permissão é
                    // importante.
                } else {
                    // Solicita a permissao
                    ActivityCompat.requestPermissions(
                            LoginActivity.this, PERMISSOES, REQUEST_STORAGE);
                }
            }
        } else {
            // ver  o que fazer aqui
        }
    }
}
