package com.br.minasfrango.network;

import com.br.minasfrango.network.servico.AutenticacaoService;
import com.br.minasfrango.network.servico.ConfiguracaoService;
import com.br.minasfrango.network.servico.ExportacaoService;
import com.br.minasfrango.network.servico.ImportacaoService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private Retrofit retrofit;

    public RetrofitConfig() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.retrofit =
                new Retrofit.Builder()
                       // .baseUrl("http://10.0.2.2:8080/api/")
                    //  .baseUrl("http://192.168.25.5:8080/api/")
                        .baseUrl("https://ws-minas-frango.herokuapp.com/api/")
                        /** AMBIENTE DE DESENVOLVIMENTO*/

                        .client(okHttpClient)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
    }

    public ExportacaoService getExportacaoService() {
        return this.retrofit.create(ExportacaoService.class);
    }

    public ImportacaoService getImportacaoService() {



        return this.retrofit.create(ImportacaoService.class);
    }

    public AutenticacaoService getLoginService() {
        return this.retrofit.create(AutenticacaoService.class);
    }

    public ConfiguracaoService getConfiguracaoService(){return  this.retrofit.create(ConfiguracaoService.class);}
}
