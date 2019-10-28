package com.br.minasfrango.network.tarefa;

import android.os.AsyncTask;
import com.br.minasfrango.data.model.ListaPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ExportacaoService;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class ExportacaoTask extends AsyncTask<Void, Void, Boolean> {

    IHomeMVP.IPresenter mHomePresenter;

    List<Pedido> mPedidos;

    public ExportacaoTask(IHomeMVP.IPresenter homePresenter, List<Pedido> pedidos) {

        this.mHomePresenter = homePresenter;
        this.mPedidos = pedidos;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        //Aqui vem a chamada do service
        return exportData(mPedidos);

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

    private boolean exportData(List<Pedido> pedidos) {
        ExportacaoService exportacaoService = new RetrofitConfig().getExportacaoService();
        ListaPedido listaPedido = new ListaPedido();
        listaPedido.setMPedidoORMS(pedidos);
        Call<Boolean> callExportacao = exportacaoService.realizarExportacao(listaPedido);
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
