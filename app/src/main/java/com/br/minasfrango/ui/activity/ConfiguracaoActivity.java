package com.br.minasfrango.ui.activity;

import static com.br.minasfrango.util.ConstantsUtil.PERMISSIONS;
import static com.br.minasfrango.util.ConstantsUtil.REQUEST_CODE_SIGN_IN;
import static com.br.minasfrango.util.ConstantsUtil.REQUEST_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IPresenter;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IView;
import com.br.minasfrango.ui.mvp.configuracao.Presenter;
import com.br.minasfrango.util.ConstantsUtil;
import com.br.minasfrango.util.Mask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.DriveScopes;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class ConfiguracaoActivity extends AppCompatActivity implements IView {

    @BindView(R.id.edtCNPJ)
    EditText edtCNPJ;

    @BindView(R.id.btnConfigurar)
    Button btnConfigurar;

    @BindView(R.id.btnLoginGoogleDrive)
    Button btnLoginGoogleDrive;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    IPresenter mPresenter;
    private TextWatcher cnpjMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        ButterKnife.bind(this);
        inicializarViews();
        concederPermissoes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Init
        mPresenter = new Presenter(this);
        mPresenter.setMac(getMacAddr());

        btnConfigurar.setVisibility(View.INVISIBLE);

        if (mPresenter.statusSistema().equals("DISPOSITIVO_HABILITADO")) {
            mPresenter
                    .getContext()
                    .startActivity(new Intent(mPresenter.getContext(), LoginActivity.class));
        } else if (mPresenter.statusSistema().equals("EMPRESA_INATIVADA")) {
            Toast.makeText(
                            mPresenter.getContext(),
                            "EMPRESA INATIVADA , POR FAVOR ENTRE EM CONTATO COM O SUPORTE DO SISTEMA!",
                            Toast.LENGTH_LONG)
                    .show();
        } else if (mPresenter.statusSistema().equals("DISPOSITIVO_INATIVADO")) {
            Toast.makeText(
                            mPresenter.getContext(),
                            "DISPOSITIVO INATIVADO , POR FAVOR ENTRE EM CONTATO COM O SUPORTE DO SISTEMA!",
                            Toast.LENGTH_LONG)
                    .show();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @OnClick(R.id.btnLoginGoogleDrive)
    public void setBtnLoginGoogleDriveClicked(View view) {

        mPresenter.solicitarLoginGoogleDrive();
    }

    @OnClick(R.id.btnConfigurar)
    public void btnSubmitClicked(View view) {

        if (estaVazioOCNPJ()) {

            edtCNPJ.setError("CNPJ é obrigatório!");

        } else {

            mPresenter.setCnpj(edtCNPJ.getText().toString());
            mPresenter.realizarConfiguracao();
        }
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            // handle exception
        }
        return "";
    }

    public void inicializarViews() {
        setSupportActionBar(mToolbar);
        cnpjMask = Mask.insert("##.###.###/####-##", edtCNPJ);
        edtCNPJ.addTextChangedListener(cnpjMask);
        edtCNPJ.setText("41.627.969/0001-74");
    }

    public void concederPermissoes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        ConfiguracaoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o
                    // check
                    // "nunca mais mostre este alerta"
                    // Podemos mostrar um alerta explicando para o usuário porque a permissão é
                    // importante.
                } else {
                    // Solicita a permissao
                    ActivityCompat.requestPermissions(
                            ConfiguracaoActivity.this, PERMISSIONS, REQUEST_STORAGE);
                }
            }
        } else {
            // ver  o que fazer aqui
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                AbstractActivity.showToast(
                        mPresenter.getContext(), "Permissões necessárias concedidas");

                btnConfigurar.setEnabled(true);

            } else {
                btnConfigurar.setEnabled(false);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public boolean estaVazioOCNPJ() {
        return edtCNPJ.getText().toString().isEmpty();
    }

    @Override
    public void solicitarLoginGoogleDrive() {

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        startActivityForResult(client.getSignInIntent(), ConstantsUtil.REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {

                    handleSignInResult(resultData);

                } else {
                    AbstractActivity.showToast(
                            mPresenter.getContext(),
                            "Não foi possível realizar o vínculo  da conta com Google Drive");
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void handleSignInResult(Intent resultData) {

        GoogleSignIn.getSignedInAccountFromIntent(resultData)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {
                                AbstractActivity.showToast(
                                        mPresenter.getContext(),
                                        "Não foi possível obter as credenciais do usuário: "
                                                + e.getMessage());
                            }
                        })
                .addOnCompleteListener(
                        new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull final Task<GoogleSignInAccount> task) {

                                if (task.isSuccessful()) {
                                    AbstractActivity.showToast(
                                            mPresenter.getContext(),
                                            "Usuário conectado: " + task.getResult().getEmail());

                                    btnConfigurar.setVisibility(View.VISIBLE);

                                } else if (task.isCanceled()) {

                                    AbstractActivity.showToast(
                                            mPresenter.getContext(),
                                            "Usuário cancelou a operação.");
                                }
                            }
                        });
    }
}
