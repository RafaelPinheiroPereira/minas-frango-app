package com.br.minasfrango.ui.mvp.exclusao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.ui.mvp.exclusao.IExclusaoMVP.IModel;
import java.text.ParseException;
import java.util.List;

public class Model implements IModel {

    Presenter mPresenter ;

    PedidoDAO mPedidoDAO= PedidoDAO.getInstace(PedidoORM.class);

    public Model(final Presenter presenter) {
        this.mPresenter = presenter;
    }

     @Override
     public void excluirPedido() {

         try {
             List<Pedido> pedidos= mPedidoDAO.pesquisarPedidosPorPeriodo(mPresenter.getDataInicial(),mPresenter.getDataFinal());
             if (VERSION.SDK_INT >= VERSION_CODES.N) {
                 pedidos.forEach(pedido->mPedidoDAO.deletar(new PedidoORM(pedido)));
             } else {
                 for (Pedido pedido: pedidos) {
                     mPedidoDAO.deletar(new PedidoORM(pedido));
                 }
             }

         } catch (ParseException e) {
             e.printStackTrace();
         }

     }
 }
