package com.br.minasfrango.network.tarefa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.ui.activity.HomeActivity;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IPresenter;
import com.br.minasfrango.util.HttpConstant;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<Void, Void, String> {

    IPresenter mPresenter;

    ProgressDialog progressDialog;

    public LoginTask(IPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {

            return acessar();
        } catch (IOException e) {
            return e.getMessage();
        }
        // return validateAcessOffLine(idUser, password);
    }

    @Override
    protected void onPostExecute(String islogin) {
        super.onPostExecute(islogin);
        progressDialog.dismiss();
        if (islogin != null) {
            if (islogin.equals("SUCESS")) {


                mPresenter
                        .getContexto()
                        .startActivity(new Intent(mPresenter.getContexto(), HomeActivity.class));


            } else if (islogin.equals("UNAUTHORIZED")) {
                Toast.makeText(
                                mPresenter.getContexto(),
                                "Matricula/Senha inválidos!",
                                Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(mPresenter.getContexto(), islogin, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                            mPresenter.getContexto(),
                            "Erro ao conectar ao servidor",
                            Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(mPresenter.getContexto());
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Realizando Login...");
        progressDialog.show();
    }

    private String acessar() throws IOException {
        Call<Funcionario> autenticaLoginCall =
                mPresenter.autenticarLogin(mPresenter.getIdUsuario(), mPresenter.getSenha(), mPresenter.getEmpresa().getId());
        Response<Funcionario> response = autenticaLoginCall.execute();

        switch (response.code()) {
            case HttpConstant.OK:
                Funcionario funcionario = response.body();
                funcionario.setSenha( mPresenter.getSenha());

                mPresenter.salvarFuncionario(funcionario);
                if (funcionario.getMaxIdRecibo() > 0) {
                    String codigoReciboFormatado =
                            String.valueOf(funcionario.getMaxIdRecibo()).substring(2);
                    funcionario.setMaxIdRecibo(Long.parseLong(codigoReciboFormatado));
                }
                mPresenter.criarSessao(
                        mPresenter.getIdUsuario(),
                        mPresenter.getSenha(),
                        funcionario.getNome(),
                        mPresenter.getNucleo().getId(),
                        funcionario.getMaxIdVenda(),funcionario.getMaxIdRecibo());
                return "SUCESS";

            case HttpConstant.UNAUTHORIZED:
                return "UNAUTHORIZED";
        }

        throw new IOException("Funcionário não cadastrado!");
    }

    private String validateAcessOffLine(String idUser, String password) {
        // mPresenter.criarSessao(idUser, password, "teste-off-line",mPresenter.getNucleo().getId(),
        // funcionario.getMaxIdVenda());
        return "SUCESS";
    }
}
