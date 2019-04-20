package com.br.minasfrango.network.tasks;

import android.os.AsyncTask;
import com.br.minasfrango.data.dto.ListaPedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.service.ExportacaoService;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;


public class DataExport extends AsyncTask<Void, Void, Boolean> {

    IHomeMVP.IPresenter mHomePresenter;

    List<Pedido> pedidoDTOS;

    public DataExport(IHomeMVP.IPresenter homePresenter, List<Pedido> pedidoDTOS) {

        this.mHomePresenter = homePresenter;
        this.pedidoDTOS = pedidoDTOS;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        //Aqui vem a chamada do service
        return exportData(pedidoDTOS);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            this.mHomePresenter.hideProgressDialog();
            this.mHomePresenter.showToast("Exportação realizada com sucesso!");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mHomePresenter.showProgressDialog();
    }

    private boolean exportData(List<Pedido> pedidoDTOS) {
        ExportacaoService exportacaoService = new RetrofitConfig().getExportacaoService();
        ListaPedido listaPedido = new ListaPedido();
        listaPedido.setMPedidos(pedidoDTOS);
        Call<Boolean> callExportacao = exportacaoService.exportacaoPedido(listaPedido);
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
