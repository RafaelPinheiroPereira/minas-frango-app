package com.br.minasfrango.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dto.RecebimentoDTO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Funcionario;
import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.PrecoID;
import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.TipoRecebimento;
import com.br.minasfrango.data.pojo.Unidade;
import com.br.minasfrango.service.ImportacaoService;
import io.realm.Realm;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pc on 13/01/2016.
 */
public class ImportData extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog dialog;

    private Context ctx;

    Funcionario funcionario;

    RecebimentoDAO recebimentoDAO;

    public ImportData(Context ctx, Funcionario funcionario) {
        dialog = new ProgressDialog(ctx);
        this.ctx = ctx;
        this.funcionario = funcionario;

    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("IMPORTAÇÃO DE DADOS");
        dialog.setMessage("POR FAVOR, AGUARDE IMPORTANDO DADOS...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean importou) {
        super.onPostExecute(importou);

        if (importou) {
            dialog.dismiss();
            Toast.makeText(ctx, "IMPORTAÇÃO REALIZADA", Toast.LENGTH_LONG).show();
        }
    }

    public boolean realizaImportacao() {
        boolean importou = false;
        if (importarClientes()) {

            if (importarTipoRecebimentos()) {
                if (importarProdutos()) {
                    if (importarUnidades()) {
                        if (importarPrecos()) {
                            if (importarRecebimentos()) {
                                importou = true;
                            }

                        }

                    }

                }

            }
        }

        return importou;

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        return realizaImportacao();


    }

    private boolean importarClientes() {

        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Funcionario aux = new Funcionario();
        aux.setId(1);
        Call<List<Cliente>> callCliente = importacaoService.importacaoCliente(aux);
        Response<List<Cliente>> responseCliente = null;
        try {
            responseCliente = callCliente.execute();

            if (responseCliente.isSuccessful()) {
                List<Cliente> clientes = responseCliente.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (Cliente clienteEntityToSave : clientes) {
                    realm.copyToRealmOrUpdate(clienteEntityToSave);
                }
                realm.commitTransaction();
                Log.d("Importacao Clientes", "Sucess");
                return true;

            } else {
                dialog.dismiss();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean importarProdutos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<Produto>> callProdutos = importacaoService.importacaoProduto();
        try {
            Response<List<Produto>> responseProdutos = callProdutos.execute();
            if (responseProdutos.isSuccessful()) {
                List<Produto> produtos = responseProdutos.body();
                Realm realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                for (Produto aux : produtos) {
                    realm.copyToRealmOrUpdate(aux);
                }
                realm.commitTransaction();
                Log.d("Importacao Produtos", "Sucess");
                return true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean importarTipoRecebimentos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<TipoRecebimento>> callTipoRecebimento = importacaoService.importacaoTipoRecebimento();
        try {
            Response<List<TipoRecebimento>> responseTipoRecebimento = callTipoRecebimento.execute();
            List<TipoRecebimento> tipoRecebimentos = responseTipoRecebimento.body();
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            for (TipoRecebimento aux : tipoRecebimentos) {
                realm.copyToRealmOrUpdate(aux);
            }
            realm.commitTransaction();
            Log.d("Importacao Tipo", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean importarUnidades() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<Unidade>> callUnidades = importacaoService.importacaoUnidade();
        try {
            Response<List<Unidade>> responseUnidades = callUnidades.execute();
            if (responseUnidades.isSuccessful()) {
                List<Unidade> unidades = responseUnidades.body();
                Realm realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                for (Unidade aux : unidades) {
                    aux.setId(aux.getChavesUnidade().getIdUnidade() + "-" + aux.getChavesUnidade().getIdProduto());

                    realm.copyToRealmOrUpdate(aux);
                }
                realm.commitTransaction();
                Log.d("Importacao Unidades", "Sucess");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private boolean importarPrecos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<Preco>> callPrecos = importacaoService.importacaoPreco();
        try {
            Response<List<Preco>> responsePrecos = callPrecos.execute();
            if (responsePrecos.isSuccessful()) {
                List<Preco> precos = responsePrecos.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (Preco aux : precos) {
                    PrecoID precoID = new PrecoID(aux.getChavesPreco().getId(), aux.getChavesPreco().getIdProduto(),
                            aux.getChavesPreco().getUnidadeProduto(), aux.getChavesPreco().getIdCliente());
                    aux.setChavesPreco(precoID);
                    aux.setId(aux.getChavesPreco().getId() + "-" + aux.getChavesPreco().getIdCliente() + "-" +
                            aux.getChavesPreco().getIdProduto() + "-" + aux.getChavesPreco().getUnidadeProduto());
                    realm.copyToRealmOrUpdate(aux);

                }
                realm.commitTransaction();
                Log.d("Importacao Precos", "Sucess");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private boolean importarRecebimentos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Call<List<RecebimentoDTO>> callRecebimentos = importacaoService.importacaoRecebimentos(funcionario);
        try {
            Response<List<RecebimentoDTO>> responseRecebimentoDTO = callRecebimentos.execute();
            if (responseRecebimentoDTO.isSuccessful()) {
                List<RecebimentoDTO> recebimentoDTOS = responseRecebimentoDTO.body();
                recebimentoDAO = RecebimentoDAO.getInstace();

                for (RecebimentoDTO aux : recebimentoDTOS) {

                    Recebimento recebimento = RecebimentoDTO.transformaDTOParaModel(aux);
                    recebimento.setId(aux.getIdVenda());
                    recebimentoDAO.addRecibo(recebimento);

                }
            }
            Log.d("Importacao RecebDTos", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

}