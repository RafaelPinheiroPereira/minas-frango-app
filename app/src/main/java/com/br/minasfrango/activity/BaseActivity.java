package com.br.minasfrango.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.br.minasfrango.R;
import com.br.minasfrango.util.SessionManager;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Drawer.Result fullView;

    private AccountHeader.Result headerNavigationLeft;

    private LinearLayout ln;

    Bundle savedInstanceState;

    SessionManager session;

    String icon;

    protected void initDrawer(Bundle bundle) {

        //initialize and create the image loader logic
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
        headerNavigationLeft = new AccountHeader()
                .withActivity(BaseActivity.this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.colorPrimary)
                .withSelectionFistLineShown(true) // Nome
                .withSelectionSecondLineShown(false) // E-mail
                .addProfiles(
                        new ProfileDrawerItem().withName(session.getNome()).withNameShown(true)
                                .withIcon(getResources().getDrawable(R.mipmap.ic_profile))
                )

                .withAlternativeProfileHeaderSwitching(false)
                .withProfileImagesClickable(true)
                .withSelectionListEnabled(false)
                .build();

        fullView = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                .withTranslucentStatusBar(false)
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id,
                            IDrawerItem drawerItem) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(BaseActivity.this, PedidoActivity.class));
                                break;
                            case 2:
                                break;
                            case 3:
                                startActivity(new Intent(BaseActivity.this, SincronizarDadosActivity.class));
                                break;

                            case 4:
                                alertaConfirmacao();
                                break;
                        }
                    }
                })
                .build();

        fullView.addItem(
                new PrimaryDrawerItem().withName("HOME").withIcon(getResources().getDrawable(R.mipmap.ic_home)));
        fullView.addItem(
                new PrimaryDrawerItem().withName("PEDIDOS").withIcon(getResources().getDrawable(R.mipmap.ic_cart)));
        fullView.addItem(new PrimaryDrawerItem().withName("CLIENTES")
                .withIcon(getResources().getDrawable(R.drawable.ic_face_black_24dp)));
        fullView.addItem(new PrimaryDrawerItem().withName("SINCRONIZAR DADOS")
                .withIcon(getResources().getDrawable(R.drawable.sicronizacao_dados)));
        fullView.addItem(
                new PrimaryDrawerItem().withName("SAIR").withIcon(getResources().getDrawable(R.mipmap.ic_logout)));
        if (Build.VERSION.SDK_INT >= 19) {
            fullView.getDrawerLayout().setFitsSystemWindows(false);
        }

    }


    protected void setDrawer(Bundle savedInstanceState, int layoutId, String titulo) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        session = new SessionManager(getApplicationContext());
        if (session.checkLogin()) {
            finish();
        }
        ln = findViewById(R.id.nav_header_container);
        toolbar = findViewById(R.id.toolbar);

        if (!titulo.equals("")) {
            toolbar.setTitle(titulo);
        } else {
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (fullView != null && fullView.isDrawerOpen()) {
            fullView.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void alertaConfirmacao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("Realizar Logout");
        builder.setMessage("Deseja realmente sair do sistema?");

        String positiveText = "SIM";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        session.logoutUser();
                        dialog.dismiss();
                        //edtConfirmaEmail.setText("");

                        return;
                    }
                });

        String negativeText = "NÃO";
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
    protected void onStart() {
        super.onStart();
    }
}

