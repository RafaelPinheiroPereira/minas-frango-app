package com.br.minasfrango.util;

import android.Manifest;
import android.Manifest.permission;

public class ConstantsUtil {

    public static final int BIGGER = 1;

    public static final int SMALLER = -1;

    public static final int EQUAL = 0;



    public static String[] PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.INTERNET,
            permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            permission.ACCESS_WIFI_STATE
    };

    public static int REQUEST_STORAGE = 112;
    public static final int  REQUEST_CODE_SIGN_IN=1;
    public static final int REQUEST_CODE_OPEN_DOCUMENT = 2;


}
