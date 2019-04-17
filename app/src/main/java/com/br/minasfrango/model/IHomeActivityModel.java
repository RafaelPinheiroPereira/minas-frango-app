package com.br.minasfrango.model;

import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.Rota;
import java.util.List;

public interface IHomeActivityModel {

    List<Cliente> findClientsByRoute(Rota rota);

    List<Recebimento> findReceiptsByClient(Cliente cliente);

    List<Cliente> getAllClients();

    List<Rota> getAllRoutes();

}
