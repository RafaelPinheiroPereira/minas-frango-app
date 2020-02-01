package com.br.minasfrango.ui.mvp.home;

import static com.br.minasfrango.util.ConstantsUtil.CAMINHO_IMAGEM_RECEBIMENTOS;
import static com.br.minasfrango.util.ConstantsUtil.CAMINHO_IMAGEM_VENDAS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.ConfiguracaoGoogleDrive;
import com.br.minasfrango.data.model.Exportacao;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.ListaPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.network.tarefa.ExportacaoTask;
import com.br.minasfrango.network.tarefa.ImportacaoTask;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.activity.RecebimentoActivity;
import com.br.minasfrango.ui.activity.VendasActivity;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IView;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.DriveServiceHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Presenter implements IHomeMVP.IPresenter {

    ControleSessao mControleSessao;

    private List<Cliente> clients = new ArrayList<>();

    private List<ClienteGrupo> redes = new ArrayList<>();

    private DriveServiceHelper mDriveServiceHelper;

    private ConfiguracaoGoogleDrive mConfiguracaoGoogleDrive;

    private IModel model;
    private IView view;

    private String idPastaVenda;


    private String idPastarecebimento;

    private Funcionario mFuncionario;



    public Presenter(final IView view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Funcionario getFuncionario() {
        return mFuncionario;
    }
    @Override
    public void setFuncionario(final Funcionario funcionario) {
        mFuncionario = funcionario;
    }

    @Override
    public void configurarGoogleDrive() {

        ConfiguracaoGoogleDrive configuracaoGoogleDrive =
                this.model.consultarConfiguracaoGoogleDrivePorFuncionario(
                        this.getControleSessao().getIdUsuario());
        this.setConfiguracaoGoogleDrive(configuracaoGoogleDrive);
    }

    @Override
    public void criarPastasDefaultNoDrive(final ConfiguracaoGoogleDrive configuracaoGoogleDrive) {

        getDriveServiceHelper()
                .criarPastaNoDrive(
                        configuracaoGoogleDrive.getIdPastaFuncionario(), CAMINHO_IMAGEM_VENDAS)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {
                                AbstractActivity.showToast(
                                        getContext(),
                                        "Falha ao criar pasta de vendas" + e.getMessage());
                                Log.d("onFailure -> ", "Error: " + e.getMessage());
                            }
                        })
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(final String idPastaVenda) {
                                configuracaoGoogleDrive.setIdPastaVenda(idPastaVenda);

                                getDriveServiceHelper()
                                        .criarPastaNoDrive(
                                                configuracaoGoogleDrive.getIdPastaFuncionario(),
                                                CAMINHO_IMAGEM_RECEBIMENTOS)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(
                                                            final String idPastaRecibo) {
                                                        configuracaoGoogleDrive.setIdPastaRecibo(
                                                                idPastaRecibo);
                                                        model.alterarConfiguracaoGoogleDrive(
                                                                configuracaoGoogleDrive);
                                                        AbstractActivity.showToast(
                                                                getContext(),
                                                                "Pastas criadas no Google Drive  com sucesso.");
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(
                                                            @NonNull final Exception e) {
                                                        AbstractActivity.showToast(
                                                                getContext(),
                                                                "Falha ao criar pasta de recebimentos "
                                                                        + e.getMessage());
                                                    }
                                                });
                            }
                        });
    }

    @Override
    public ConfiguracaoGoogleDrive getConfiguracaoGoogleDrive() {
        return mConfiguracaoGoogleDrive;
    }

    @Override
    public String pesquisarPastaRecebimentos() {
        return this.model.pesquisarIdPastaReciboPorFuncionario(mControleSessao.getIdUsuario());
    }

    @Override
    public void salvarFotosNoDrive() {

        this.model.sincronizarFotos();



    }

    @Override
    public void setConfiguracaoGoogleDrive(final ConfiguracaoGoogleDrive configuracaoGoogleDrive) {
        mConfiguracaoGoogleDrive = configuracaoGoogleDrive;
    }

    @Override
    public void esconderProgressDialog() {
        this.view.onHideProgressDialog();
    }

    @Override
    public void exibirDialogClient(final Cliente cliente) {
        this.view.showDialogClient(cliente);
    }

    @Override
    public void exibirDialogLogout() {
        this.view.showDialogLogout();
    }

    @Override
    public void exibirProgressDialog() {
        this.view.onShowProgressDialog();
    }

    @Override
    public void exibirToast(String msg) {
        AbstractActivity.showToast(getContext(), msg);
    }

    @Override
    public void exportar() {
        List<Pedido> pedidos = this.model.obterTodosPedidos();
        List<Recebimento> recebimentos = this.model.obterTodosRecebimentos();
        ConfiguracaoGoogleDrive configuracaoGoogleDrive =
                this.model.consultarConfiguracaoGoogleDrivePorFuncionario(
                        mControleSessao.getIdUsuario());
        ListaPedido listaPedido = new ListaPedido();
        listaPedido.setPedidos(pedidos);
        Exportacao exportacao = new Exportacao();
        exportacao.setListaPedido(listaPedido);
        exportacao.setRecebimentos(recebimentos);
        exportacao.setConfiguracaoGoogleDrive(configuracaoGoogleDrive);
        new ExportacaoTask(this, exportacao).execute();
    }

    @Override
    public void navigateToReceiptsActivity(Cliente cliente) {
        Intent intent = new Intent(getContext(), RecebimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyCliente", cliente);
        bundle.putString(
                "dataVenda",
                new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        intent.putExtras(bundle);
        AbstractActivity.navigateToActivity(getContext(), intent);
    }

    @Override
    public List<ClienteGrupo> obterTodasRedes() {
        redes.clear();
        redes.addAll(model.obterTodasRedes());
        return redes;
    }

    @Override
    public List<Cliente> obterTodosClientes() {
        clients.clear();
        clients.addAll(model.obterTodosClientes());
        return clients;
    }

    public List<Cliente> pesquisarClientePorRede(final ClienteGrupo clienteGrupo) {
        clients.clear();
        clients.addAll(model.pesquisarClientePorRede(clienteGrupo));
        return clients;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void fecharDrawer() {
        this.view.fecharDrawer();
    }

    @Override
    public String getNomeUsuario() {
        return this.mControleSessao.getUserName();
    }

    @Override
    public long getUserId() {
        return this.mControleSessao.getIdUsuario();
    }

    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return model.pesquisarRecebimentoPorCliente(cliente);
    }

    @Override
    public void navigateToSalesActivity(final Cliente client) {

        Intent intent = new Intent(getContext(), VendasActivity.class);
        Bundle params = new Bundle();
        params.putSerializable("keyCliente", client);
        params.putLong("keyPedido", 0);
        intent.putExtras(params);
        AbstractActivity.navigateToActivity(getContext(), intent);
    }

    @Override
    public void setAdapters() {
        this.view.setAdapters();
    }

    @Override
    public void importar() {

        Funcionario funcionario = this.model.pesquisarFuncionarioDaSessao();
        funcionario.setIdEmpresa(this.model.pesquisarEmpresaRegistrada().getId());
        this.setFuncionario(funcionario);
        new ImportacaoTask(funcionario, this).execute();
    }

    @Override
    public void logout() {

        this.mControleSessao.logout();

    }

    @Override
    public void salvarRecebimento(final Recebimento recebimento) {
        this.model.salvarRecebimento(recebimento);
    }

    @Override
    public void obterClientesAposImportarDados() {
        obterTodosClientes();
        this.view.obterClientesAposImportarDados();
    }

    @Override
    public void obterRotasAposImportarDados() {
        obterTodasRedes();
        this.view.obterRotasAposImportarDados();
    }

    @Override
    public void verificarCredenciaisGoogleDrive() {
        this.view.verificarCredenciaisGoogleDrive();
    }
    @Override
    public ControleSessao getControleSessao() {
        return mControleSessao;
    }
    @Override
    public void setControleSessao(final ControleSessao controleSessao) {
        mControleSessao = controleSessao;
    }

    @Override
    public boolean verificarLogin() {
        this.mControleSessao = new ControleSessao(getContext());
        this.setControleSessao(this.mControleSessao);
        return mControleSessao.checkLogin();
    }

    @Override
    public DriveServiceHelper getDriveServiceHelper() {
        return mDriveServiceHelper;
    }

    @Override
    public void setDriveServiceHelper(final DriveServiceHelper driveServiceHelper) {
        mDriveServiceHelper = driveServiceHelper;
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        this.view.setDrawer(savedInstanceState);
    }

    @Override
    public String pesquisarPastaDeVendas() {
        return this.model.pesquisarIdPastaDeVendas();
    }

    @Override
    public String getIdPastaVenda() {
        return idPastaVenda;
    }

    @Override
    public void setIdPastaVenda(final String idPastaVenda) {
        this.idPastaVenda = idPastaVenda;
    }

    @Override
    public String getIdPastarecebimento() {
        return idPastarecebimento;
    }

    @Override
    public Funcionario pesquisarUsuarioDaSesao() {
        return this.model.pesquisarFuncionarioDaSessao();
    }

    @Override
    public void retirarFuncionarioDaSessao() {
        this.model.deletarFuncionarioDaSessao();
    }

    @Override
    public void setIdPastaRecebimento(final String idPastarecebimento) {
        this.idPastarecebimento = idPastarecebimento;
    }
}
