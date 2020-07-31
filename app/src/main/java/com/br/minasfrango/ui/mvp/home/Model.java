package com.br.minasfrango.ui.mvp.home;

import android.app.ProgressDialog;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.br.minasfrango.data.dao.BlocoPedidoDAO;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.ClienteGrupoDAO;
import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.model.BlocoRecibo;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.BlocoReciboORM;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import com.br.minasfrango.util.ArquivoUtils;
import com.br.minasfrango.util.ConstantsUtil;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Model implements IModel {

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);
    BlocoPedidoDAO mBlocoPedidoDAO= BlocoPedidoDAO.getInstace(BlocoReciboORM.class);
    PedidoDAO mPedidoDAO=PedidoDAO.getInstace(PedidoORM.class);

    ClienteGrupoDAO mClienteGrupoDAO = ClienteGrupoDAO.getInstace(ClienteGrupoORM.class);

    EmpresaDAO mEmpresaDAO = EmpresaDAO.getInstace(EmpresaORM.class);
    FuncionarioDAO mFuncionarioDAO = FuncionarioDAO.getInstace(FuncionarioORM.class);



    int contadorVendas=0;
    int contadorRecibos=0;

    private Presenter mPresenter;
    ProgressDialog progressDialog;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }



    @Override
    public void alterarFuncionario(final Funcionario funcionario) {
        FuncionarioORM funcionarioORM = new FuncionarioORM(funcionario);
        mFuncionarioDAO.alterar(funcionarioORM);
    }

    @Override
    public void atualizarBlocoReciboParaMigrado(final BlocoRecibo blocoRecibo) {
        this.mBlocoPedidoDAO.alterar(new BlocoReciboORM(blocoRecibo));
    }

    @Override
    public void atualizarPedidoParaMigrado(final Pedido pedido) {
        this.mOrderDAO.alterar(new PedidoORM(pedido));
    }

    @Override
    public Pedido consultarPedidoPorNomeDaFoto(final String name) {

        return  this.mOrderDAO.consultarPedidoPorNomeDaFoto(name);
    }

    @Override
    public void deletarFuncionarioDaSessao() {
        FuncionarioORM funcionarioORM = this.mFuncionarioDAO.where().equalTo("id",mPresenter.getFuncionario().getId()).findFirst();
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

    @Override
    public BlocoRecibo pesquisarBlocoReciboPorNomeDaFoto(final String name) {

        return this.mBlocoPedidoDAO.consultarBlocoReciboPorNome(name);
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

        FuncionarioORM funcionarioORM = mFuncionarioDAO.where().equalTo("id",mPresenter.getControleSessao().getIdUsuario()).findFirst();
        Funcionario funcionario = new Funcionario(funcionarioORM);
        return funcionario;
    }

    @Override
    public List<Pedido> pesquisarPedidosNaoMigrados() {
       return this.mPedidoDAO.getPedidosNaoMigrados();
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }

    @Override
    public List<BlocoRecibo> pesquisarRecibosNaoMigrados() {
        return this.mBlocoPedidoDAO.getRecibosNaoMigrados();
    }

    @Override
    public void sincronizarFotos() {
        ArquivoUtils mArquivoUtils = new ArquivoUtils();

        File[] fotosVendas = mArquivoUtils.lerFotosDoDiretorio(ConstantsUtil.CAMINHO_IMAGEM_VENDAS);
        File[] fotosRecebimentos = mArquivoUtils.lerFotosDoDiretorio(ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS);

        List<File> fotosVendasNaoMigradas= new ArrayList<>();
        List<File> fotosRecibosNaoMigrados= new ArrayList<>();

        for(Pedido pedido:mPresenter.getFotosPedidos()){
            for (File foto: fotosVendas){
                if(pedido.getNomeFoto().equals(foto.getName())){
                    fotosVendasNaoMigradas.add(foto);
                }
            }

        }

        for(BlocoRecibo blocoRecibo: mPresenter.getFotosRecibos()){
            for (File foto: fotosRecebimentos){
                if(blocoRecibo.getNomeFoto().equals(foto.getName())){
                    fotosRecibosNaoMigrados.add(foto);
                }
            }
        }

        progressDialog = new ProgressDialog(mPresenter.getContext());

        if (fotosVendasNaoMigradas.size() > 0) {

            progressDialog.setTitle("Sincronização Google Drive");
            progressDialog.setMessage("Sincronizando fotos ...");
            progressDialog.show();

            for (File foto : fotosVendasNaoMigradas) {


                mPresenter
                        .getDriveServiceHelper()
                        .salvarFoto(mPresenter.getFuncionario().getIdPastaVendas(), foto)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(final String s) {
                                        Log.d("onSucess", "Foto Salva " + s);

                                        contadorVendas++;

                                        mPresenter.atualizarPedidoPorNomeDaFoto(foto.getName());
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
                                        if (task.isSuccessful()) {
                                            if (contadorVendas == fotosVendasNaoMigradas.size()) {

                                                progressDialog.dismiss();
                                                contadorVendas = 0;

                                                if(fotosRecibosNaoMigrados.size()>0){
                                                    if (!progressDialog.isShowing()) {
                                                        progressDialog.show();
                                                    }
                                                    for (File foto : fotosRecibosNaoMigrados) {

                                                        sincronizarFotosRecebimentos(fotosRecibosNaoMigrados, foto);
                                                    }
                                                }

                                                else{

                                                    AbstractActivity.showToast(
                                                            mPresenter.getContext(), "Fotos de vendas  sincronizadas e não existem fotos de recebimentos para serem salvas.");
                                                }
                                            }
                                        }
                                    }
                                })
                        .addOnCanceledListener(
                                new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {

                                        AbstractActivity.showToast(
                                                mPresenter.getContext(),
                                                "Sincronização cancelada.");
                                        progressDialog.dismiss();
                                    }
                                });
}
        }else if (fotosVendasNaoMigradas.size()==0 && fotosRecibosNaoMigrados.size()==0){
            AbstractActivity.showToast(
                    mPresenter.getContext(), "Não existem fotos de vendas e recebimentos para serem salvas.");
        }

        else {

            progressDialog.setTitle("Sincronização Google Drive");
            progressDialog.setMessage("Sincronizando fotos ...");


            if(fotosRecibosNaoMigrados.size()>0){
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                for (File foto : fotosRecibosNaoMigrados) {

                    sincronizarFotosRecebimentos(fotosRecibosNaoMigrados, foto);
                }
            }

            else  if (fotosVendas.length>0 && fotosRecebimentos.length==0){

                AbstractActivity.showToast(
                        mPresenter.getContext(), "Não existem fotos de recebimentos para serem salvas.");
            }


        }




    }

    private void sincronizarFotosRecebimentos(final List<File> fotosRecebimentos, final File foto) {
        mPresenter
                .getDriveServiceHelper()
                .salvarFoto(mPresenter.getFuncionario().getIdPastaPagamentos(), foto)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(final String s) {
                                Log.d("onSucess", "Foto Salva " + s);
                                contadorRecibos++;
                                mPresenter.atualizarBlocoReciboPorNomeDaFoto(foto.getName());
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
                                if (task.isSuccessful()) {
                                    if (contadorRecibos == fotosRecebimentos.size()) {
                                        AbstractActivity.showToast(
                                                mPresenter.getContext(),
                                                "Todas as fotos de vendas e  recebimento foram sincronizadas.");
                                        progressDialog.dismiss();
                                        contadorRecibos = 0;
                                    }
                                }
                            }
                        })
                .addOnCanceledListener(
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                AbstractActivity.showToast(
                                        mPresenter.getContext(),
                                        "Sincronização cancelada.");
                                progressDialog.dismiss();
                            }
                        });
    }

    @Override
    public void salvarRecebimento(final Recebimento recebimento) {
        RecebimentoORM recebimentoORM = new RecebimentoORM(recebimento);
        this.mRecebimentoDAO.alterar(recebimentoORM);
    }

    @Override
    public void excluirRecebimentos() {
                List<Recebimento> recebimentos= this.mRecebimentoDAO.pesquisarTodosRecebimentos();
                if(recebimentos!=null && recebimentos.size()>0){
                for(Recebimento recebimento: recebimentos){
                    this.mRecebimentoDAO.deletar(new RecebimentoORM(recebimento));
                }
                }

    }


}
