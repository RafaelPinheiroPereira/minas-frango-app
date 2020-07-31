package com.br.minasfrango.network.tarefa;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.model.Importacao;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.PrecoID;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ContaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.PrecoORM;
import com.br.minasfrango.data.realm.ProdutoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.data.realm.UnidadeORM;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ImportacaoService;
import com.br.minasfrango.ui.mvp.home.IHomeMVP;
import com.br.minasfrango.util.ControleSessao;
import io.realm.Realm;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/** Created by Pc on 13/01/2016. */
public class ImportacaoTask extends AsyncTask<Void, Void, Boolean> {



    IHomeMVP.IPresenter mHomePresenter;

    ControleSessao mControleSessao;

    public ImportacaoTask( IHomeMVP.IPresenter homePresenter) {

        this.mHomePresenter = homePresenter;
        this.mControleSessao = new ControleSessao(mHomePresenter.getContext());
    }

    public boolean importarDados() {
        boolean importou = false;

        if (importar()) {
            importou = true;
        }

        return importou;
    }

    @Override
    protected Boolean doInBackground(Void... params) {



        return importarDados();
    }

    @Override
    protected void onPostExecute(Boolean importou) {
        super.onPostExecute(importou);

        if (importou) {



           if(mHomePresenter.getFuncionario().getIdPastaVendas()==null & mHomePresenter.getFuncionario().getIdPastaPagamentos()==null)
                this.mHomePresenter.criarPastasNoDrive(
                        mHomePresenter.getFuncionario());
           }


            this.mHomePresenter.esconderProgressDialog();
            this.mHomePresenter.exibirToast("Importação realizada com sucesso!");
            this.mHomePresenter.obterClientesAposImportarDados();
            this.mHomePresenter.obterRotasAposImportarDados();

            this.mHomePresenter.fecharDrawer();
        }


    @Override
    protected void onPreExecute() {

        this.mHomePresenter.exibirProgressDialog();
    }

    private void salvarClientes(List<Cliente> clientes) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            clientes.forEach(cliente -> realm.copyToRealmOrUpdate(new ClienteORM(cliente)));
        } else {
            for (Cliente cliente : clientes) {
                realm.copyToRealmOrUpdate(new ClienteORM(cliente));
            }
        }
        realm.commitTransaction();
        Log.d("Importacao Clientes", "Sucess");
    }

    private void salvarClientesGrupos(List<ClienteGrupo> clienteGrupos) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            clienteGrupos.forEach(
                    clienteGrupo -> realm.copyToRealmOrUpdate(new ClienteGrupoORM(clienteGrupo)));
        } else {
            for (ClienteGrupo clienteGrupo : clienteGrupos) {
                realm.copyToRealmOrUpdate(new ClienteGrupoORM(clienteGrupo));
            }
        }
        realm.commitTransaction();
        Log.d("Importacao Grupos", "Sucess");
    }

    private void salvarContas(List<Conta> contas) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (VERSION.SDK_INT >= VERSION_CODES.N) {

            contas.forEach(conta -> realm.copyToRealmOrUpdate(new ContaORM(conta)));
        } else {
            for (Conta conta : contas) {
                realm.copyToRealmOrUpdate(new ContaORM(conta));
            }
        }
        realm.commitTransaction();
        Log.d("Importacao Contas", "Sucess");
    }

    private void salvarPrecos(List<Preco> precos) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            precos.forEach(
                    preco -> {
                        PrecoID precoID =
                                new PrecoID(
                                        preco.getChavesPreco().getId(),
                                        preco.getChavesPreco().getIdCliente(),
                                        preco.getChavesPreco().getIdProduto(),
                                        preco.getChavesPreco().getUnidadeProduto(),
                                        preco.getChavesPreco().getDataPreco());

                        preco.setChavesPreco(precoID);
                        preco.setId(
                                preco.getChavesPreco().getId()
                                        + "-"
                                        + preco.getChavesPreco().getIdCliente()
                                        + "-"
                                        + preco.getChavesPreco().getIdProduto()
                                        + "-"
                                        + preco.getChavesPreco().getUnidadeProduto()
                                        + "-"
                                        + preco.getChavesPreco().getDataPreco());
                        realm.copyToRealmOrUpdate(new PrecoORM(preco));
                    });
        } else {
            for (Preco preco : precos) {

                PrecoID precoID =
                        new PrecoID(
                                preco.getChavesPreco().getId(),
                                preco.getChavesPreco().getIdCliente(),
                                preco.getChavesPreco().getIdProduto(),
                                preco.getChavesPreco().getUnidadeProduto(),
                                preco.getChavesPreco().getDataPreco());

                preco.setChavesPreco(precoID);
                preco.setId(
                        preco.getChavesPreco().getId()
                                + "-"
                                + preco.getChavesPreco().getIdCliente()
                                + "-"
                                + preco.getChavesPreco().getIdProduto()
                                + "-"
                                + preco.getChavesPreco().getUnidadeProduto()
                                + "-"
                                + preco.getChavesPreco().getDataPreco());
                realm.copyToRealmOrUpdate(new PrecoORM(preco));
            }
        }

        realm.commitTransaction();
        Log.d("Importacao Precos", "Sucess");
    }

    private void salvarProdutos(List<Produto> produtos) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            produtos.forEach(produto -> realm.copyToRealmOrUpdate(new ProdutoORM(produto)));
        } else {
            for (Produto produto : produtos) {
                realm.copyToRealmOrUpdate(new ProdutoORM(produto));
            }
        }
        realm.commitTransaction();
        Log.d("Importacao de Produtos", "Sucess");
    }

    private void salvarRecebimentos(List<Recebimento> recebimentos) {

      //  this.mHomePresenter.excluirRecebimentos();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();



        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            recebimentos.forEach(
                    recebimento -> {
                        recebimento.setIdVenda(recebimento.getIdVenda());
                        realm.copyToRealmOrUpdate(new RecebimentoORM(recebimento));
                    });
        } else {
            for (Recebimento recebimento : recebimentos) {
                recebimento.setIdVenda(recebimento.getIdVenda());
                realm.copyToRealmOrUpdate(new RecebimentoORM(recebimento));
            }
        }
        realm.commitTransaction();

        Log.d("Importacao Recebimentos", "Sucess");
    }

    private void salvarUnidades(List<Unidade> unidades) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            unidades.forEach(
                    unidade -> {
                        unidade.setId(
                                unidade.getChavesUnidade().getIdUnidade()
                                        + "-"
                                        + unidade.getChavesUnidade().getIdProduto());
                        realm.copyToRealmOrUpdate(new UnidadeORM(unidade));
                    });
        } else {
            for (Unidade unidade : unidades) {
                unidade.setId(
                        unidade.getChavesUnidade().getIdUnidade()
                                + "-"
                                + unidade.getChavesUnidade().getIdProduto());
                realm.copyToRealmOrUpdate(new UnidadeORM(unidade));
            }
        }
        realm.commitTransaction();
        Log.d("Importacao de  Unidades", "Sucess");
    }

    private boolean importar() {
        ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();

        Call<Importacao> importacaoCall =
                importacaoService.realizarImportacao(
                        mHomePresenter.getFuncionario().getId(),
                        mHomePresenter.getFuncionario().getIdEmpresa(),
                        mControleSessao.getIdNucleo());
        try {
            Response<Importacao> importacaoResponse = importacaoCall.execute();
            if (importacaoResponse.isSuccessful()) {

                Importacao importacao = importacaoResponse.body();

                salvarClientes(importacao.getClientes());
                salvarProdutos(importacao.getProdutos());
                salvarUnidades(importacao.getUnidades());
                salvarPrecos(importacao.getPrecos());
                salvarRecebimentos(importacao.getRecebimentosDTO());
                salvarContas(importacao.getContas());
                salvarClientesGrupos(importacao.getClientesGrupos());
                this.atualizarStatusSincronizacaoDoFuncionario();

            }
        } catch (IOException e) {
            Log.d("Error", e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    private void atualizarStatusSincronizacaoDoFuncionario() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        FuncionarioORM funcionarioORM= new FuncionarioORM( mHomePresenter.getFuncionario());
        funcionarioORM.setSincronizou(true);
        realm.copyToRealmOrUpdate(funcionarioORM);
        realm.commitTransaction();

    }


}
