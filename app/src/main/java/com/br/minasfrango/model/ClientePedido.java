package com.br.minasfrango.model;

import com.br.minasfrango.util.ParentListItem;
import java.util.ArrayList;
import java.util.List;


public class ClientePedido implements ParentListItem {
    private Cliente mCliente;
    private List<Pedido> mPedidos;

    public ClientePedido(Cliente cliente, List<Pedido> pedidos) {
        this.mCliente = cliente;
        this.mPedidos= new ArrayList<>();
        this.mPedidos .addAll(pedidos);
    }

    public Cliente getCliente() {
        return this.mCliente;
    }



    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }


    @Override
    public List<?> getChildItemList() {
        return this.mPedidos;
    }


}
