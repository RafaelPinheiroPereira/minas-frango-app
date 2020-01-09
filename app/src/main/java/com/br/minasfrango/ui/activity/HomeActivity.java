package com.br.minasfrango.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ClienteAdapter;
import com.br.minasfrango.ui.listener.IDrawer;
import com.br.minasfrango.ui.listener.NavigateDrawer;
import com.br.minasfrango.ui.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IPresenter;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IView;
import com.br.minasfrango.ui.mvp.home.Presenter;
import com.br.minasfrango.util.AlertDialogClient;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class HomeActivity extends AppCompatActivity
        implements RecyclerViewOnClickListenerHack, IView {

    ClienteAdapter mClientAdapter;

    ArrayAdapter mRedeAdapter;

    IPresenter presenter;

    @BindView(R.id.rcvCliente)
    RecyclerView rvCliente;

    @BindView(R.id.spnClienteGrupo)
    Spinner spnClienteGrupo;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchView searchView;

    private Drawer result;

    private IDrawer navigateDrawer;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        // Verifica se o user esta logado
        presenter = new Presenter(this);
        if (presenter.verificarLogin()) {
            finish();
        }
        initViews();
        presenter.setDrawer(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setAdapters();
        mClientAdapter.setRecyclerViewOnClickListenerHack(this);
    }

    @Override
    public void fecharDrawer() {

        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
    }

    @Override
    public void obterClientesAposImportarDados() {
        mClientAdapter.notifyDataSetChanged();
    }

    @Override
    public void obterRotasAposImportarDados() {
        mRedeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHideProgressDialog() {
        if (getProgressDialog().isShowing()) {
            getProgressDialog().dismiss();
        }
    }

    @Override
    public void onShowProgressDialog() {
        if (getProgressDialog().isShowing()) {
            return;
        }
        getProgressDialog().show();
    }

    public ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Sincronização de dados");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Carregando dados, Por favor aguarde...");
        }
        return mProgressDialog;
    }

    @Override
    public void onBackPressed() {
        // handle the back press :D close the drawer first and if the drawer is closed close the
        // activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public void onClickListener(View view, int position) {

        switch (view.getId()) {
            case R.id.btnVender:
                presenter.navigateToSalesActivity(mClientAdapter.getItem(position));
                break;

            case R.id.btnReceber:
                if (presenter
                        .pesquisarRecebimentoPorCliente(mClientAdapter.getItem(position))
                        .size()
                        > 0) {
                    presenter.navigateToReceiptsActivity(mClientAdapter.getItem(position));
                } else {
                    AbstractActivity.showToast(
                            presenter.getContext(), "ClienteORM sem notas em aberto!");
                }
                break;
            case R.id.imgInfo:
                presenter.exibirDialogClient(mClientAdapter.getItem(position));

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_cliente_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        // filter recycler view when text is changed
                        mClientAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // filter recycler view when query submitted
                        mClientAdapter.getFilter().filter(query);
                        return false;
                    }
                });
        return true;
    }

    @Override
    public void onLongPressClickListener(final View view, final int position) {
    }

    @Override
    public void setAdapters() {

        mClientAdapter = new ClienteAdapter(this, presenter.obterTodosClientes());

        rvCliente.setAdapter(mClientAdapter);

        mRedeAdapter =
                new ArrayAdapter(
                        this, android.R.layout.simple_list_item_1, presenter.obterTodasRedes());

        spnClienteGrupo.setAdapter(mRedeAdapter);
        spnClienteGrupo.setSelection(0);
        spnClienteGrupo.setPrompt("Todas as Redes");
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {

        navigateDrawer = new NavigateDrawer(this);
        result = navigateDrawer.builder(this, toolbar, savedInstanceState, presenter.getNomeUsuario());

        result.setOnDrawerItemClickListener(
                new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                AbstractActivity.navigateToActivity(
                                        presenter.getContext(),
                                        new Intent(presenter.getContext(), HomeActivity.class));
                                break;
                            case 2:
                                AbstractActivity.navigateToActivity(
                                        presenter.getContext(),
                                        new Intent(presenter.getContext(), PedidoActivity.class));
                                break;
                            case 3:
                                break;

                            case 4:
                                presenter.importar();
                                break;
                            case 5:
                                presenter.exportar();
                                break;

                            case 6:
                                AbstractActivity.navigateToActivity(
                                        presenter.getContext(),
                                        new Intent(
                                                presenter.getContext(), DeviceListActivity.class));
                                break;

                            case 7:
                                AbstractActivity.navigateToActivity(
                                        presenter.getContext(),
                                        new Intent(
                                                presenter.getContext(), ExclusaoActivity.class));
                                break;


                            case 8:
                                presenter.exibirDialogLogout();
                                break;
                        }
                        return true;
                    }
                });

        navigateDrawer.addItemInDrawer(result);
    }

    @Override
    public void showDialogLogout() {
        AlertDialog.Builder builder = new Builder(presenter.getContext());
        builder.setTitle("Realizar Logout");
        builder.setMessage("Deseja realmente sair do sistema?");
        String positiveText = "Sim";
        builder.setPositiveButton(
                positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        presenter.logout();
                        dialog.dismiss();
                        // edtConfirmaEmail.setText("");

                        return;
                    }
                });

        String negativeText = "Não";
        builder.setNegativeButton(
                negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        // display mProgressDialog
        dialog.show();
    }

    @Override
    public void showDialogClient(final Cliente cliente) {

        AlertDialogClient alertDialogClient = new AlertDialogClient(presenter);
        AlertDialog b = alertDialogClient.builder(cliente);
        b.show();
    }

    @OnItemSelected(R.id.spnClienteGrupo)
    void onItemSelected(int position) {

            presenter.pesquisarClientePorRede((ClienteGrupo) mRedeAdapter.getItem(position));
            mClientAdapter.notifyDataSetChanged();
            spnClienteGrupo.setSelection(position);
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        rvCliente.setLayoutManager(layoutManager);
        toolbar.setTitle("Trinity Mobile - Minas Frango");
        setSupportActionBar(toolbar);
    }
}
