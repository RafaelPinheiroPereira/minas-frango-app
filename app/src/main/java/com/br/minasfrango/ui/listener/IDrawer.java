package com.br.minasfrango.ui.listener;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

public interface IDrawer {

    Drawer builder(Activity activity, Toolbar toolbar, Bundle savedInstanceState,String userName);
    AccountHeader getAccountHeader(Bundle savedInstanceState,String userName);
    void addItemInDrawer(Drawer drawer);

}
