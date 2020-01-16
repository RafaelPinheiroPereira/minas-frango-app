package com.br.minasfrango.network.tarefa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.br.minasfrango.data.model.Configuracao;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ConfiguracaoService;
import com.br.minasfrango.ui.activity.LoginActivity;
import com.br.minasfrango.ui.mvp.configuracao.Presenter;
import com.br.minasfrango.util.HttpConstant;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class ConfiguracaoTask extends AsyncTask<Void, Void, String> {

    Presenter mPresenter;
    ProgressDialog progressDialog;

    public ConfiguracaoTask(final Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(mPresenter.getContext());
        progressDialog.setTitle("Configuração  do Dispositivo");
        progressDialog.setMessage("Configurando dispositivo...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(final String isConfigurado) {
        super.onPostExecute(isConfigurado);
        progressDialog.dismiss();

        if (isConfigurado != null) {

            if (isConfigurado.equals("SUCESSO")) {

                mPresenter
                        .getContext()
                        .startActivity(new Intent(mPresenter.getContext(), LoginActivity.class));

            } else if (isConfigurado.equals("UNAUTHORIZED")) {
                Toast.makeText(
                                mPresenter.getContext(),
                                "Dispositivo não habilitado!",
                                Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(mPresenter.getContext(), isConfigurado, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                            mPresenter.getContext(),
                            "Erro ao conectar ao servidor",
                            Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Override this method to perform a computation on a background thread. The specified
     * parameters are the parameters passed to {@link #execute} by the caller of this task.
     *
     * <p>This method can call {@link #publishProgress} to publish updates on the UI thread.
     *
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(final Void... voids) {
        try {
            return estaConfigurado();
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            return e.getMessage();
        }
    }

    private String estaConfigurado() throws IOException {

        ConfiguracaoService configuracaoService = new RetrofitConfig().getConfiguracaoService();
        Call<Configuracao> configuracaoCall =
                configuracaoService.verificarAtivacao(mPresenter.getCnpj(), mPresenter.getMac());

        Response<Configuracao> configuracaoResponse = null;

        configuracaoResponse = configuracaoCall.execute();
        switch (configuracaoResponse.code()) {
            case HttpConstant.OK:
                Configuracao configuracao = configuracaoResponse.body();

                mPresenter.salvarConfiguracoes(configuracao);
                return "SUCESSO";
            case HttpConstant.UNAUTHORIZED:
                return "UNAUTHORIZED";
        }

        throw new IOException("Dispositivo não cadastrado!");
    }
}
