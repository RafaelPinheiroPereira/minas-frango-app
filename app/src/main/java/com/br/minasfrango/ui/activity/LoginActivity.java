package com.br.minasfrango.ui.activity;

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

    @OnClick(R.id.btnLogin)
    public void btnSubmitClicked(View view) {

        if (presenter.estaoValidadosOsInputs()) {
            try {
                presenter.realizarLogin(edtMatricula.getText().toString(), edtSenha.getText().toString());

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
        btnLogin.setVisibility(View.GONE);

        // Inicializacao a fim de testes
        //edtMatricula.setText("1");
       // edtSenha.setText("1234");
    }

    @Override
    public void onBackPressed() {
        // handle the back press :D close the drawer first and if the drawer is closed close the
        // activity

    }
}