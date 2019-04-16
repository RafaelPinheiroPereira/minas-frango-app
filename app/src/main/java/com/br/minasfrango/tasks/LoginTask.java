package com.br.minasfrango.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.br.minasfrango.activity.ClienteActivity;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.presenter.ILoginPresenter;
import com.br.minasfrango.util.HttpConstant;
import com.br.minasfrango.view.ILoginView;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<Void, Void, String> {


    String idUser, password;

    ILoginView mLoginView;

    ILoginPresenter mPresenter;

    ProgressDialog progressDialog;

    public LoginTask(ILoginPresenter presenter, ILoginView loginView, String idUser, String password) {
        this.mPresenter = presenter;
        this.mLoginView = loginView;
        this.idUser = idUser;
        this.password = password;

    }

    @Override
    protected String doInBackground(Void... params) {

        try {

            return validateAcess(idUser, password);
        } catch (IOException e) {
            return e.getMessage();
        }


    }

    @Override
    protected void onPostExecute(String islogin) {
        super.onPostExecute(islogin);
        progressDialog.dismiss();
        if (islogin.equals("SUCESS")) {
            mPresenter.getContext().startActivity(new Intent(mPresenter.getContext(), ClienteActivity.class));
        } else if (islogin.equals("UNAUTHORIZED")) {
            Toast.makeText(mPresenter.getContext(), "Matricula/Senha inválidos!", Toast.LENGTH_LONG).show();
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
            case HttpConstant
                    .OK:
                Funcionario funcionario = response.body();
                mPresenter.createSession(idUser, password, funcionario.getNome());
                return "SUCESS";

            case HttpConstant
                    .UNAUTHORIZED:
                return "UNAUTHORIZED";


        }

        throw new IOException("Funcionário não cadastrado!");
    }
}


