package com.br.minasfrango.network.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.br.minasfrango.data.realm.Funcionario;
import com.br.minasfrango.ui.activity.HomeActivity;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IPresenter;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IView;
import com.br.minasfrango.util.HttpConstant;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<Void, Void, String> {

    String idUser, password;

    IView mLoginView;

    IPresenter mPresenter;

    ProgressDialog progressDialog;

    public LoginTask(IPresenter presenter, IView loginView, String idUser, String password) {
        this.mPresenter = presenter;
        this.mLoginView = loginView;
        this.idUser = idUser;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {

        /*try {

            return validateAcess(idUser, password);
        } catch (IOException e) {
            return e.getMessage();
        }*/

        return validateAcessOffLine(idUser, password);
    }

    @Override
    protected void onPostExecute(String islogin) {
        super.onPostExecute(islogin);
        progressDialog.dismiss();
        if (islogin.equals("SUCESS")) {
            mPresenter
                    .getContext()
                    .startActivity(new Intent(mPresenter.getContext(), HomeActivity.class));
        } else if (islogin.equals("UNAUTHORIZED")) {
            Toast.makeText(mPresenter.getContext(), "Matricula/Senha inválidos!", Toast.LENGTH_LONG)
                    .show();
        } else {
            Toast.makeText(mPresenter.getContext(), islogin, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(mPresenter.getContext());
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Realizando Login...");
        progressDialog.show();
    }

    private String validateAcess(String idUser, String password) throws IOException {
        Call<Funcionario> autenticaLoginCall = mPresenter.autenticateLogin(idUser, password);
        Response<Funcionario> response = autenticaLoginCall.execute();

        switch (response.code()) {
            case HttpConstant.OK:
                Funcionario funcionario = response.body();
                mPresenter.createSession(idUser, password, funcionario.getNome());
                return "SUCESS";

            case HttpConstant.UNAUTHORIZED:
                return "UNAUTHORIZED";
        }

        throw new IOException("Funcionário não cadastrado!");
    }

    private String validateAcessOffLine(String idUser, String password) {
        mPresenter.createSession(idUser, password, "teste-off-line");
        return "SUCESS";
    }
}
