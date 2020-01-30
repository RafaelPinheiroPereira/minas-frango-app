package com.br.minasfrango.network.tarefa;

import android.os.AsyncTask;
import com.br.minasfrango.data.model.Exportacao;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ExportacaoService;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class ExportacaoTask extends AsyncTask<Void, Void, Boolean> {

    IHomeMVP.IPresenter mHomePresenter;

    Exportacao mExportacao;

    public ExportacaoTask(IHomeMVP.IPresenter homePresenter, Exportacao exportacao) {

        this.mHomePresenter = homePresenter;
        this.mExportacao = exportacao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        // Aqui vem a chamada do service
        return exportar(mExportacao);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            this.mHomePresenter.esconderProgressDialog();
            this.mHomePresenter.exibirToast("Exportação realizada com sucesso!");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mHomePresenter.exibirProgressDialog();
    }

    private boolean exportar(Exportacao exportacao) {
        ExportacaoService exportacaoService = new RetrofitConfig().getExportacaoService();
        Call<Boolean> callExportacao = exportacaoService.realizarExportacao(exportacao);
        try {
            Response<Boolean> response = callExportacao.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
