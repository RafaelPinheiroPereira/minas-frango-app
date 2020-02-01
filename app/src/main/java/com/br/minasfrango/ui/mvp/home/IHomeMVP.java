package com.br.minasfrango.ui.mvp.home;

import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.DriveServiceHelper;
import java.util.List;

public interface IHomeMVP {

    interface IPresenter {



        Funcionario pesquisarUsuarioDaSesao();

        void retirarFuncionarioDaSessao();

        Funcionario getFuncionario();

        void setFuncionario(Funcionario funcionario);

        void criarPastasNoDrive(Funcionario funcionario);

        void salvarFotosNoDrive();

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

        long getUserId();

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

        ControleSessao getControleSessao();

        void setControleSessao(ControleSessao controleSessao);

        boolean verificarLogin();


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

        void alterarFuncionario(Funcionario funcionario);

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
    }
}
