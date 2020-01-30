package com.br.minasfrango.ui.mvp.home;

import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.ConfiguracaoGoogleDrive;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.util.DriveServiceHelper;
import java.util.List;

public interface IHomeMVP {

    interface IPresenter {

        String getIdPastarecebimento();

        void setIdPastaRecebimento(String idPastarecebimento);

        void configurarGoogleDrive();

        void criarPastasDefaultNoDrive(ConfiguracaoGoogleDrive configuracaoGoogleDrive);

        ConfiguracaoGoogleDrive getConfiguracaoGoogleDrive();

        String pesquisarPastaRecebimentos();

        void salvarFotosNoDrive();

        void setConfiguracaoGoogleDrive(ConfiguracaoGoogleDrive configuracaoGoogleDrive);

        void esconderProgressDialog();

        void exibirDialogClient(final Cliente cliente);

        void exibirDialogLogout();

        List<ClienteGrupo> obterTodasRedes();

        List<Cliente> obterTodosClientes();

        List<Cliente> pesquisarClientePorRede(ClienteGrupo ClienteGrupo);

        List<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente);

        void exibirProgressDialog();

        void exibirToast(String msg);

        Context getContext();

        void exportar();

        void fecharDrawer();

        int getUserId();

        void logout();

        void salvarRecebimento(Recebimento recebimento);

        DriveServiceHelper getDriveServiceHelper();

        void setDriveServiceHelper(DriveServiceHelper driveServiceHelper);

        void setDrawer(final Bundle savedInstanceState);

        String getNomeUsuario();

        void importar();

        void setAdapters();

        void navigateToReceiptsActivity(Cliente cliente);

        void navigateToSalesActivity(Cliente cliente);

        void obterClientesAposImportarDados();

        void obterRotasAposImportarDados();



        void verificarCredenciaisGoogleDrive();

        boolean verificarLogin();

        String pesquisarPastaDeVendas();

        String getIdPastaVenda();

        void setIdPastaVenda(String idPastaVenda);
    }

    interface IView {

        void fecharDrawer();


        void setAdapters();

        void setDrawer(final Bundle savedInstanceState);

        void showDialogClient(final Cliente cliente);

        void onShowProgressDialog();

        void onHideProgressDialog();

        void obterClientesAposImportarDados();

        void obterRotasAposImportarDados();

        void showDialogLogout();

        void verificarCredenciaisGoogleDrive();
    }

    interface IModel {

        void alterarConfiguracaoGoogleDrive(ConfiguracaoGoogleDrive configuracaoGoogleDrive);

        ConfiguracaoGoogleDrive consultarConfiguracaoGoogleDrivePorFuncionario(int idUsuario);

        String pesquisarIdPastaReciboPorFuncionario(long idFuncionario);

        void deletarFuncionarioDaSessao();

        List<ClienteGrupo> obterTodasRedes();

        List<Cliente> obterTodosClientes();

        List<Pedido> obterTodosPedidos();

        List<Recebimento> obterTodosRecebimentos();

        List<Cliente> pesquisarClientePorRede(ClienteGrupo clienteGrupo);

        Empresa pesquisarEmpresaRegistrada();

        Funcionario pesquisarFuncionarioDaSessao();

        List<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente);

        void sincronizarFotos();

        void salvarRecebimento(Recebimento recebimento);

        String pesquisarIdPastaDeVendas();
    }
}
