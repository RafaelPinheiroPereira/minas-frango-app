package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.ConfiguracaoGoogleDrive;
import com.br.minasfrango.data.realm.ConfiguracaoGoogleDriveORM;

public class ConfiguracaoGoogleDriveDAO extends GenericsDAO<ConfiguracaoGoogleDriveORM> {

    public ConfiguracaoGoogleDriveDAO(final Class<ConfiguracaoGoogleDriveORM> entity) {
        super(entity);
    }

    public static ConfiguracaoGoogleDriveDAO getInstace(final Class<ConfiguracaoGoogleDriveORM> type) {
        return new ConfiguracaoGoogleDriveDAO(type);
    }

    public ConfiguracaoGoogleDrive pesquisarPorIdDoFuncionario(final int idUsuario) {

        ConfiguracaoGoogleDriveORM configuracaoGoogleDriveORM = where().equalTo("idFuncionario",idUsuario).findFirst();
        return new ConfiguracaoGoogleDrive(configuracaoGoogleDriveORM);
    }
}
