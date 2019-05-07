package com.br.minasfrango.network;

import com.br.minasfrango.network.servico.ExportacaoService;
import com.br.minasfrango.network.servico.ImportacaoService;
import com.br.minasfrango.network.servico.LoginService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit =
                new Retrofit.Builder()
                        //.baseUrl("http://10.0.2.2:8080/rest/minasFrango/")
                        .baseUrl("http://192.168.0.11:8080/rest/minasFrango/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
    }

    public ExportacaoService getExportacaoService() {
        return this.retrofit.create(ExportacaoService.class);
    }

    public ImportacaoService getImportacaoService() {
        return this.retrofit.create(ImportacaoService.class);
    }

    public LoginService getLoginService() {
        return this.retrofit.create(LoginService.class);
    }
}
