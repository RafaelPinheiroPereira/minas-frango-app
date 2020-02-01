package com.br.minasfrango.ui.mvp.configuracao;

import android.app.Activity;
import android.content.Context;
import com.br.minasfrango.data.model.Configuracao;
import com.br.minasfrango.network.tarefa.ConfiguracaoTask;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IModel;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IPresenter;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IView;
import com.br.minasfrango.util.ArquivoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Presenter implements IPresenter {

    IView mView;
    IModel mIModel;

    String mac;
    String cnpj;


    private ArquivoUtils mArquivoUtils ;



    private ConfiguracaoTask mConfiguracaoTask;

    public Presenter(final IView view) {
        mView = view;
        mIModel = new Model(this);
    }

    @Override
    public void criarPastasDasImagens() {
        this.mArquivoUtils=new ArquivoUtils();
        this.mArquivoUtils.criarPastas((Activity) this.getContext());
    }

    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

   
    @Override
    public String statusSistema() {
        return this.mIModel.statusSistema();
    }

    @Override
    public void realizarConfiguracao() {
        mConfiguracaoTask = new ConfiguracaoTask(this);
        mConfiguracaoTask.execute();
    }

    @Override
    public void salvarConfiguracoes(final Configuracao configuracao) {
        this.mIModel.salvarConfiguracao(configuracao);
    }

    @Override
    public String getMac() {
        return mac;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public void setMac(final String mac) {
        this.mac = mac;
    }

    @Override
    public void setCnpj(final String cnpj) {
        this.cnpj = cnpj;
    }
    @Override
    public ArquivoUtils getArquivoUtils() {
        return mArquivoUtils;
    }
    @Override
    public void setArquivoUtils(final ArquivoUtils arquivoUtils) {
        mArquivoUtils = arquivoUtils;
    }




}
