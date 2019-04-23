package com.br.minasfrango.ui.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import androidx.appcompat.widget.Toolbar;
import com.br.minasfrango.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class NavigateDrawer implements IDrawer {

    Activity mActivity;
    Drawer result;


    public NavigateDrawer(final Activity activity) {
        mActivity = activity;
    }

    @Override
    public Drawer builder(Activity activity, Toolbar toolbar, Bundle savedInstanceState,String userName) {
        return  new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(getAccountHeader(savedInstanceState,userName))
                .withTranslucentStatusBar(false)
                .withSelectedItem(0).build();
    }

    @Override
    public AccountHeader getAccountHeader(final Bundle savedInstanceState,String userName) {
        return  new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.colorPrimary)
                .withSelectionSecondLineShown(false) // E-mail
                .addProfiles(
                        new ProfileDrawerItem().withName(userName).withNameShown(true)
                                .withIcon(mActivity.getResources().getDrawable(R.mipmap.ic_profile))
                )

                .withAlternativeProfileHeaderSwitching(false)
                .withProfileImagesClickable(true)
                .withSelectionListEnabled(false)
                .build();
    }

    @Override
    public void addItemInDrawer(Drawer drawer) {
        drawer.addItem(
                new PrimaryDrawerItem().withName("HomeActivity").withIcon(mActivity.getResources().getDrawable(R.mipmap.ic_home)));
        drawer.addItem(
                new PrimaryDrawerItem().withName("Pedidos").withIcon(mActivity.getResources().getDrawable(R.mipmap.ic_cart)));
        drawer.addItem(new PrimaryDrawerItem().withName("Clientes")
                .withIcon(mActivity.getResources().getDrawable(R.drawable.ic_face_black_24dp)));
        drawer.addItem(
                new PrimaryDrawerItem().withName("Importar Dados").withIcon(mActivity.getResources().getDrawable(R.drawable.ic_file_download_black_24dp)));
        drawer.addItem(
                new PrimaryDrawerItem().withName("Exportar Dados").withIcon(mActivity.getResources().getDrawable(R.drawable.ic_file_upload_black_24dp)));
        drawer.addItem(
                new PrimaryDrawerItem().withName("Sair").withIcon(mActivity.getResources().getDrawable(R.mipmap.ic_logout)));

    }
}
