package com.br.minasfrango.network.tarefa;

import android.os.AsyncTask;
import android.util.Log;
import com.br.minasfrango.data.dao.PrecoIDDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.PrecoID;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.PrecoIDORM;
import com.br.minasfrango.data.realm.PrecoORM;
import com.br.minasfrango.data.realm.ProdutoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.data.realm.TipoRecebimentoORM;
import com.br.minasfrango.data.realm.UnidadeORM;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ImportacaoService;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import io.realm.Realm;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pc on 13/01/2016.
 */
public class ImportacaoDeDados extends AsyncTask<Void, Void, Boolean> {

    Funcionario mFuncionario;

    IHomeMVP.IPresenter mHomePresenter;

    public ImportacaoDeDados(Funcionario funcionario, IHomeMVP.IPresenter homePresenter) {
        this.mFuncionario = funcionario;
        this.mHomePresenter = homePresenter;
    }

    public boolean importData() {
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

        return importData();
    }

    @Override
    protected void onPostExecute(Boolean importou) {
        super.onPostExecute(importou);

        if (importou) {

            this.mHomePresenter.esconderProgressDialog();
            this.mHomePresenter.exibirToast("Importação realizada com sucesso!");
            this.mHomePresenter.obterClientesAposImportarDados();
            this.mHomePresenter.obterRotasAposImportarDados();

            this.mHomePresenter.fecharDrawer();
        }
    }

    @Override
    protected void onPreExecute() {

        this.mHomePresenter.exibirProgressDialog();
    }

    private boolean importarClientes() {

        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Funcionario aux = new Funcionario();
        aux.setId(1);
        Call<List<Cliente>> callCliente = importacaoService.importarCliente(aux);
        Response<List<Cliente>> responseCliente = null;
        try {
            responseCliente = callCliente.execute();

            if (responseCliente.isSuccessful()) {
                List<Cliente> clientes = responseCliente.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                clientes.forEach(cliente->realm.copyToRealmOrUpdate(new ClienteORM(cliente)));
                realm.commitTransaction();
                Log.d("Importacao Clientes", "Sucess");
                return true;

            } else {

                this.mHomePresenter.esconderProgressDialog();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean importarPrecos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<Preco>> callPrecos = importacaoService.importarPreco();
        try {
            Response<List<Preco>> responsePrecos = callPrecos.execute();
            if (responsePrecos.isSuccessful()) {
                List<Preco> precos = responsePrecos.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                precos.forEach(
                        preco->{

                            PrecoID precoID =
                                    new PrecoID(
                                            preco.getChavesPreco().getId(),
                                            preco.getChavesPreco().getIdCliente(),
                                            preco.getChavesPreco().getIdProduto(),
                                            preco.getChavesPreco().getUnidadeProduto(),
                                            preco.getChavesPreco().getDataPreco()
                                    );



                            preco.setChavesPreco(precoID);
                            preco.setId(
                                    preco.getChavesPreco().getId()
                                            + "-"
                                            + preco.getChavesPreco().getIdCliente()
                                            + "-"
                                            + preco.getChavesPreco().getIdProduto()
                                            + "-"
                                            + preco.getChavesPreco().getUnidadeProduto() + "-" + preco
                                            .getChavesPreco().getDataPreco());
                            realm.copyToRealmOrUpdate(new PrecoORM(preco));
                        });

                realm.commitTransaction();
                Log.d("Importacao Precos", "Sucess");

                PrecoIDDAO precoIDDAO = PrecoIDDAO.getInstance(PrecoIDORM.class);


                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean importarProdutos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<Produto>> callProdutos = importacaoService.importarProduto();
        try {
            Response<List<Produto>> responseProdutos = callProdutos.execute();
            if (responseProdutos.isSuccessful()) {
                List<Produto> produtos = responseProdutos.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                produtos.forEach(produto->realm.copyToRealmOrUpdate(new ProdutoORM(produto)));
                realm.commitTransaction();
                Log.d("Importacao de Produtos", "Sucess");
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
        Call<List<Recebimento>> callRecebimentos =
                importacaoService.importarRecebimentos(funcionario);
        try {
            Response<List<Recebimento>> responseRecebimentoDTO = callRecebimentos.execute();
            if (responseRecebimentoDTO.isSuccessful()) {
                List<Recebimento> recebimentos = responseRecebimentoDTO.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                recebimentos.forEach(
                        recebimento->{
                            recebimento.setId(recebimento.getIdVenda());
                            realm.copyToRealmOrUpdate(new RecebimentoORM(recebimento));
                        });
                realm.commitTransaction();
            }

            Log.d("Importacao RecebDTos", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean importarTipoRecebimentos() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
        Call<List<TipoRecebimento>> callTipoRecebimento =
                importacaoService.importarTipoRecebimento();
        try {
            Response<List<TipoRecebimento>> responseTipoRecebimento = callTipoRecebimento.execute();
            List<TipoRecebimento> tipoRecebimentos = responseTipoRecebimento.body();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            tipoRecebimentos.forEach(
                    tipoRecebimento->
                            realm.copyToRealmOrUpdate(new TipoRecebimentoORM(tipoRecebimento)));
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
        Call<List<Unidade>> callUnidades = importacaoService.importarUnidade();
        try {
            Response<List<Unidade>> responseUnidades = callUnidades.execute();
            if (responseUnidades.isSuccessful()) {
                List<Unidade> unidades = responseUnidades.body();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                unidades.forEach(
                        unidade->{
                            unidade.setId(
                                    unidade.getChavesUnidade().getIdUnidade()
                                            + "-"
                                            + unidade.getChavesUnidade().getIdProduto());
                            realm.copyToRealmOrUpdate(new UnidadeORM(unidade));
                        });
                realm.commitTransaction();
                Log.d("Importacao de  Unidades", "Sucess");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}