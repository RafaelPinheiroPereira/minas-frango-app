package com.br.minasfrango.ui.activity;

import static com.br.minasfrango.util.ConstantsUtil.REQUEST_CODE_SIGN_IN;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Nucleo;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IPresenter;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IView;
import com.br.minasfrango.ui.mvp.login.Presenter;
import com.br.minasfrango.util.ConstantsUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.DriveScopes;

public class LoginActivity extends AppCompatActivity implements IView {





    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.edtUser)
    EditText edtMatricula;

    @BindView(R.id.edtPassword)
    EditText edtSenha;

    @BindView(R.id.spnNucleo)
    Spinner spnNucleo;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.lnLoginBox)
    LinearLayout lnLoginBox;

    IPresenter presenter;

    ArrayAdapter mAdapterNucleo;
    @BindView(R.id.btnLoginGoogleDrive)
    Button btnLoginGoogleDrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();

        carregarAnimacaoInicializacao();
        // Internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Init
        presenter = new Presenter(this);
        mAdapterNucleo =
                new ArrayAdapter(
                        this, android.R.layout.simple_list_item_1, presenter.carregarTodosOsNucleos());
        spnNucleo.setAdapter(mAdapterNucleo);
        spnNucleo.setPrompt("Todas os Nucleos");

        Empresa empresa=presenter.pesquisarEmpresaRegistrada();
        presenter.setEmpresa(empresa);


    }
    @OnItemSelected(R.id.spnNucleo)
    void onItemSelected(int position) {
        if (position != 0) {
            presenter.setNucleo((Nucleo) mAdapterNucleo.getItem(position));
            spnNucleo.setSelection(position);
        }
    }

    @OnClick(R.id.btnLoginGoogleDrive)
    public void setBtnLoginGoogleDriveClicked(View view) {

        presenter.solicitarLoginGoogleDrive();
    }


    @OnClick(R.id.btnLogin)
    public void btnSubmitClicked(View view) {

        if (presenter.estaoValidadosOsInputs()) {
            try {
                presenter.realizarLogin(Long.parseLong(edtMatricula.getText().toString()), edtSenha.getText().toString());

            } catch (final Exception e) {
                LoginActivity.this.runOnUiThread(
                        ()->AbstractActivity.showToast(presenter.getContexto(), e.getMessage()));
            }
        }
    }

    @Override
    public void carregarAnimacaoInicializacao() {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        animTranslate.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        lnLoginBox.setVisibility(View.VISIBLE);

                        Animation animFade =
                                AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                        lnLoginBox.startAnimation(animFade);

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
    public boolean validarForm() {

        if (TextUtils.isEmpty(edtMatricula.getText().toString())) {
            edtMatricula.setError("Matricula Obrigatória!");
            edtMatricula.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Senha Obrigatória!");
            edtSenha.requestFocus();
            return false;
        }
        if(spnNucleo.getSelectedItemPosition()==0) {
            Toast.makeText(presenter.getContexto(),"Selecione um Núcleo!",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void initView() {

        lnLoginBox.setVisibility(View.GONE);


        // Inicializacao a fim de testes
        //edtMatricula.setText("1");
       // edtSenha.setText("1234");
    }

    @Override
    public void onBackPressed() {
        // handle the back press :D close the drawer first and if the drawer is closed close the
        // activity

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {

                    handleSignInResult(resultData);

                } else {
                    AbstractActivity.showToast(
                            presenter.getContexto(),
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
                                        presenter.getContexto(),
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
                                            presenter.getContexto(),
                                            "Usuário conectado: " + task.getResult().getEmail());

                                    btnLogin.setVisibility(View.VISIBLE);

                                } else if (task.isCanceled()) {

                                    AbstractActivity.showToast(
                                            presenter.getContexto(),
                                            "Usuário cancelou a operação.");
                                }
                            }
                        });
    }

}