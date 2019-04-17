package com.br.minasfrango.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import com.br.minasfrango.R;
import com.br.minasfrango.data.adapter.ClienteAdapter;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Rota;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.presenter.HomeActivityPresenter;
import com.br.minasfrango.presenter.IHomeActivityPresenter;
import com.br.minasfrango.view.IHomeActivityView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements RecyclerViewOnClickListenerHack,
        IHomeActivityView {


    ClienteAdapter mClientAdapter;

    ArrayAdapter mRouteAdapter;

    IHomeActivityPresenter presenter;

    @BindView(R.id.rcvCliente)
    RecyclerView rvCliente;

    @BindView(R.id.spnRoute)
    Spinner spnRoute;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchView searchView;

    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomeActivityPresenter(this);
        ButterKnife.bind(this);
        //Verifica se o user esta logado
        if (presenter.checkLogin()) {
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

    public void alertaConfirmacao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
        builder.setTitle("Realizar Logout");
        builder.setMessage("Deseja realmente sair do sistema?");

        String positiveText = "Sim";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        presenter.logout();
                        dialog.dismiss();
                        //edtConfirmaEmail.setText("");

                        return;
                    }
                });

        String negativeText = "Não";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClickListener(View view, int position) {

        switch (view.getId()) {

            case R.id.btnVender:
                presenter.navigateToSalesActivity(mClientAdapter.getItem(position));
                break;

            case R.id.btnReceber:
                if (presenter.findReceiptsByClient(mClientAdapter.getItem(position)).size() > 0) {
                    presenter.navigateToReceiptsActivity(mClientAdapter.getItem(position));
                } else {
                    showToast("Cliente sem notas em aberto!");
                }
                break;
            case R.id.imgInfo:
                dialogDetalheCliente(mClientAdapter.getItem(position));
                break;


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_cliente_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        mClientAdapter = new ClienteAdapter(this,
                presenter.getAllClients().isEmpty() ? new ArrayList<>() : presenter.getAllClients());

        rvCliente.setAdapter(mClientAdapter);

        mRouteAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, presenter.getAllRoutes());

        spnRoute.setAdapter(mRouteAdapter);
        spnRoute.setPrompt("Todas as Rotas");
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(getAccountHeader(savedInstanceState))
                .withTranslucentStatusBar(false)
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position,
                            IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                navigateToActivity(new Intent(presenter.getContext(), HomeActivity.class));
                                break;
                            case 2:
                                navigateToActivity(new Intent(presenter.getContext(), PedidoActivity.class));
                                break;
                            case 3:
                                break;
                            case 4:
                                navigateToActivity(
                                        new Intent(presenter.getContext(), SincronizarDadosActivity.class));
                                break;

                            case 5:
                                alertaConfirmacao();
                                break;
                        }
                        return true;
                    }
                })
                .build();

        addItemInDrawer();


    }

    @Override
    public void showToast(final String msg) {
        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();

    }

    @OnItemSelected(R.id.spnRoute)
    void onItemSelected(int position) {
        if (position != 0) {
            presenter.findClientsByRoute((Rota) mRouteAdapter.getItem(position));
            mClientAdapter.notifyDataSetChanged();
            spnRoute.setSelection(position);
        }
    }


    private void dialogDetalheCliente(Cliente cliente) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_detalhe_cliente, null);
        dialogBuilder.setView(dialogView);

        TextView codigoClienteTextView;
        TextView razaoSocialTextView, qtdNotasAbertasTextView, valorMedioCompraTextView;
        TextView nomeFantasiaTextView, obsTextView, rgTextView;
        TextView enderecoTextView, pontoReferenciaTextView, cpfcnpjTextView;
        TextView bairroClienteTextView, telefoneTextView, contatoTextView;
        TextView cepClienteTextView, cidadeTextView, codigoLocalidadeTextView;

        //	dialogView.setContentView(R.layout.dialog_detalhe_cliente);

        codigoClienteTextView = dialogView.findViewById(R.id.txt_codigo_cliente);
        razaoSocialTextView = dialogView.findViewById(R.id.txt_razao_social);
        nomeFantasiaTextView = dialogView.findViewById(R.id.txt_nome_fantasia);
        enderecoTextView = dialogView.findViewById(R.id.txt_endereco);
        bairroClienteTextView = dialogView.findViewById(R.id.txt_bairro);
        cepClienteTextView = dialogView.findViewById(R.id.txt_cep);
        cidadeTextView = dialogView.findViewById(R.id.txt_cidade);
        obsTextView = dialogView.findViewById(R.id.txt_observacao);
        rgTextView = dialogView.findViewById(R.id.txt_rg);
        valorMedioCompraTextView = dialogView.findViewById(R.id.txt_vlr_medio_compra);

        codigoLocalidadeTextView = dialogView.findViewById(R.id.txt_codigo_localidade);
        telefoneTextView = dialogView.findViewById(R.id.txt_telefone);
        contatoTextView = dialogView.findViewById(R.id.txt_contato);
        pontoReferenciaTextView = dialogView.findViewById(R.id.txt_ponto_REF);
        cpfcnpjTextView = dialogView.findViewById(R.id.txt_cpf_cnpj);
        qtdNotasAbertasTextView = dialogView.findViewById(R.id.txt_qtd_notas_abertas);
        // Custom Android Allert Dialog Title
        dialogBuilder.setTitle("Informações do Cliente");
        nomeFantasiaTextView.setText(cliente.getNome());
        razaoSocialTextView.setText(cliente.getRazaoSocial());
        codigoClienteTextView.setText(String.valueOf(cliente.getId()));
        enderecoTextView.setText(cliente.getEndereco());
        bairroClienteTextView.setText(cliente.getBairro());
        cepClienteTextView.setText(String.valueOf(cliente.getCep()));
        cidadeTextView.setText(cliente.getCidade());
        telefoneTextView.setText(cliente.getTelefone());
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void addItemInDrawer() {
        result.addItem(
                new PrimaryDrawerItem().withName("HOME").withIcon(getResources().getDrawable(R.mipmap.ic_home)));
        result.addItem(
                new PrimaryDrawerItem().withName("PEDIDOS").withIcon(getResources().getDrawable(R.mipmap.ic_cart)));
        result.addItem(new PrimaryDrawerItem().withName("CLIENTES")
                .withIcon(getResources().getDrawable(R.drawable.ic_face_black_24dp)));
        result.addItem(new PrimaryDrawerItem().withName("SINCRONIZAR DADOS")
                .withIcon(getResources().getDrawable(R.drawable.sicronizacao_dados)));
        result.addItem(
                new PrimaryDrawerItem().withName("SAIR").withIcon(getResources().getDrawable(R.mipmap.ic_logout)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AccountHeader getAccountHeader(final Bundle savedInstanceState) {
        return new AccountHeaderBuilder()
                .withActivity(HomeActivity.this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.colorPrimary)
                .withSelectionSecondLineShown(false) // E-mail
                .addProfiles(
                        new ProfileDrawerItem().withName(presenter.getUserName()).withNameShown(true)
                                .withIcon(getResources().getDrawable(R.mipmap.ic_profile))
                )

                .withAlternativeProfileHeaderSwitching(false)
                .withProfileImagesClickable(true)
                .withSelectionListEnabled(false)
                .build();
    }

    private void initViews() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvCliente.setLayoutManager(layoutManager);

        toolbar.setTitle("TRINITY MOBILE");
        setSupportActionBar(toolbar);

    }
}
