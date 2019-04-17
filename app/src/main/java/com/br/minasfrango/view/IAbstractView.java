package com.br.minasfrango.view;

import android.content.Intent;
import android.os.Bundle;

public interface IAbstractView {

    void navigateToActivity(Intent intent);

    void setDrawer(final Bundle savedInstanceState);

    void showToast(String msg);

}
