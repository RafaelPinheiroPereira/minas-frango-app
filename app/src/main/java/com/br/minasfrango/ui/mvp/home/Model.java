package com.br.minasfrango.ui.mvp.home;

import android.app.ProgressDialog;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.ClienteGrupoDAO;
import com.br.minasfrango.data.dao.ConfiguracaoGoogleDriveDAO;
import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.ConfiguracaoGoogleDrive;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ConfiguracaoGoogleDriveORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import com.br.minasfrango.util.ArquivoUtils;
import com.br.minasfrango.util.ConstantsUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.File;
import java.util.List;

public class Model implements IModel {

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    ClienteGrupoDAO mClienteGrupoDAO = ClienteGrupoDAO.getInstace(ClienteGrupoORM.class);

    EmpresaDAO mEmpresaDAO = EmpresaDAO.getInstace(EmpresaORM.class);
    FuncionarioDAO mFuncionarioDAO = FuncionarioDAO.getInstace(FuncionarioORM.class);
    ConfiguracaoGoogleDriveDAO mConfiguracaoGoogleDriveDAO =
            ConfiguracaoGoogleDriveDAO.getInstace(ConfiguracaoGoogleDriveORM.class);


    int contadorVendas=0;
    int contadorRecibos=0;

    private Presenter mPresenter;
    ProgressDialog progressDialog;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void alterarConfiguracaoGoogleDrive(
            final ConfiguracaoGoogleDrive configuracaoGoogleDrive) {
        this.mConfiguracaoGoogleDriveDAO.alterar(
                new ConfiguracaoGoogleDriveORM(configuracaoGoogleDrive));
    }

    @Override
    public ConfiguracaoGoogleDrive consultarConfiguracaoGoogleDrivePorFuncionario(
            final int idUsuario) {
        return mConfiguracaoGoogleDriveDAO.pesquisarPorIdDoFuncionario(idUsuario);
    }


        @Override
        public String pesquisarIdPastaReciboPorFuncionario(final long idFuncionario) {
            ConfiguracaoGoogleDrive configuracaoGoogleDrive= mConfiguracaoGoogleDriveDAO.pesquisarPorIdDoFuncionario(
                    (int) idFuncionario);
            return  configuracaoGoogleDrive.getIdPastaRecibo();

    }

    @Override
    public void deletarFuncionarioDaSessao() {
        FuncionarioORM funcionarioORM = this.mFuncionarioDAO.where().findFirst();
        Funcionario funcionarioDeletado = new Funcionario(funcionarioORM);
        this.mFuncionarioDAO.deletar(new FuncionarioORM(funcionarioDeletado));
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<ClienteGrupo> obterTodasRedes() {
        return mClienteGrupoDAO.todos();
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Cliente> obterTodosClientes() {
        return mClienteDAO.todos();
    }

    @Override
    public List<Pedido> obterTodosPedidos() {

        return mOrderDAO.todos();
    }

    @Override
    public List<Recebimento> obterTodosRecebimentos() {
        return this.mRecebimentoDAO.pesquisarTodosRecebimentos();
    }

    public List<Cliente> pesquisarClientePorRede(final ClienteGrupo clienteGrupo) {
        return mClienteDAO.pesquisarClientePorRede(clienteGrupo);
    }

    @Override
    public Empresa pesquisarEmpresaRegistrada() {
        return mEmpresaDAO.pesquisarEmpresaRegistradaNoDispositivo();
    }

    @Override
    public Funcionario pesquisarFuncionarioDaSessao() {

        FuncionarioORM funcionarioORM = mFuncionarioDAO.where().findFirst();
        Funcionario funcionario = new Funcionario(funcionarioORM);
        return funcionario;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }

    @Override
    public void sincronizarFotos() {
        ArquivoUtils mArquivoUtils = new ArquivoUtils();
        progressDialog = new ProgressDialog(mPresenter.getContext());
        progressDialog.setTitle("Sincronização Google Drive");
        progressDialog.setMessage("Sincronizando fotos...");

        File[] fotosVendas = mArquivoUtils.lerFotosDoDiretorio(ConstantsUtil.CAMINHO_IMAGEM_VENDAS);
        File[] fotosRecebimentos = mArquivoUtils.lerFotosDoDiretorio(ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS);

        if (fotosVendas.length > 0) {
            progressDialog.show();
            for (File foto : fotosVendas) {
                mPresenter
                        .getDriveServiceHelper()
                        .salvarFoto(mPresenter.getIdPastaVenda(), foto)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(final String s) {
                                        Log.d("onSucess", "Foto Salva " + s);
                                        contadorVendas++;
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull final Exception e) {
                                        AbstractActivity.showToast(
                                                mPresenter.getContext(),
                                                "Não foi possível realizar a operação."
                                                        + e.getMessage());
                                        progressDialog.dismiss();
                                    }
                                })
                        .addOnCompleteListener(
                                new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<String> task) {
                                        if (task.isSuccessful())
                                            if (contadorVendas == fotosVendas.length) {
                                                AbstractActivity.showToast(
                                                        mPresenter.getContext(),
                                                        "Todas as fotos de vendas foram sincronizadas.");
                                                progressDialog.dismiss();
                                            }
                                    }
                                });
}
        } else {


            AbstractActivity.showToast(
                    mPresenter.getContext(), "Não existem fotos de vendas para serem salvas.");

        }

        if(fotosRecebimentos.length>0){

            if(!progressDialog.isShowing()){
                progressDialog.show();
            }

            for (File foto : fotosRecebimentos) {

                mPresenter
                        .getDriveServiceHelper()
                        .salvarFoto(mPresenter.getIdPastarecebimento(), foto)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(final String s) {
                                        Log.d("onSucess", "Foto Salva " + s);
                                        contadorRecibos++;
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull final Exception e) {
                                        AbstractActivity.showToast(
                                                mPresenter.getContext(),
                                                "Não foi possível realizar a operação."
                                                        + e.getMessage());
                                        progressDialog.dismiss();
                                    }
                                })
                        .addOnCompleteListener(
                                new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<String> task) {
                                        if (task.isSuccessful())
                                            if (contadorRecibos == fotosRecebimentos.length) {
                                                AbstractActivity.showToast(
                                                        mPresenter.getContext(),
                                                        "Todas as fotos do recebimento foram sincronizadas.");
                                                progressDialog.dismiss();
                                            }
                                    }
                                });
        }
            }

        else{
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            AbstractActivity.showToast(
                    mPresenter.getContext(), "Não existem fotos de recebimentos para serem salvas.");
        }
    }

    @Override
    public void salvarRecebimento(final Recebimento recebimento) {
        RecebimentoORM recebimentoORM = new RecebimentoORM(recebimento);
        this.mRecebimentoDAO.alterar(recebimentoORM);
    }

    @Override
    public String pesquisarIdPastaDeVendas() {
        ConfiguracaoGoogleDrive configuracaoGoogleDrive =
                mConfiguracaoGoogleDriveDAO.pesquisarPorIdDoFuncionario(
                        mPresenter.getUserId());
        return configuracaoGoogleDrive.getIdPastaVenda();
    }
}
