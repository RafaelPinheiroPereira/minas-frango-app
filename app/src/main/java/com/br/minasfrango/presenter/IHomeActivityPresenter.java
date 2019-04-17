package com.br.minasfrango.presenter;

import android.content.Context;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.Rota;
import java.util.List;


public interface IHomeActivityPresenter extends IAbstractActivityPresenter {

    boolean checkLogin();

    List<Cliente> findClientsByRoute(Rota route);

    List<Recebimento> findReceiptsByClient(Cliente cliente);

    List<Cliente> getAllClients();

    List<Rota> getAllRoutes();

    Context getContext();

    String getUserName();

    void logout();

    void navigateToReceiptsActivity(Cliente client);

    void navigateToSalesActivity(Cliente client);

    void setAdapters();


}
