package com.br.minasfrango.util;

import java.util.HashMap;

public interface ISessionManager {

    boolean checkLogin();

    void createUserLoginSession(String userID, String password, String nameUser);

    HashMap<String, String> getUserDetails();

    int getUserID();

    String getUserName();

    boolean isUserLoggedIn();

    void logout();

}
