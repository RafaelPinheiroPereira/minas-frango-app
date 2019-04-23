package com.br.minasfrango.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheet {

    @BindView(R.id.lnlDeleteSalesOrder)

    LinearLayout lnlDeleteSalesOrder;

    @BindView(R.id.lnlEditSalesOrder)
    LinearLayout lnlEditSalesOrder;

    @BindView(R.id.lnlViewSalesOrder)
    LinearLayout lnlViewSalesOrder;

    private IPedidoMVP.IPresenter presenter;

    public BottomSheet(IPedidoMVP.IPresenter presenter) {
        this.presenter = presenter;
    }

    public void createBottomSheet() {

        BottomSheetDialog dialog = new BottomSheetDialog(presenter.getContext());
        LayoutInflater mLayout = (LayoutInflater) presenter.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayout.inflate(R.layout.pedido_dialog_bottom_sheet, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);
        dialog.show();
    }

    @OnClick(R.id.lnlDeleteSalesOrder)
    public void lnlDeleteSalesOrderClicked(View view) {
        this.presenter.onShowDialogDeleteOrderSalle(presenter.getPedido());

    }

    @OnClick(R.id.lnlEditSalesOrder)
    public void lnlViewEditOrderClicked(View view) {
        this.presenter.onNavigateToEditSalesOrderActivity(presenter.getPedido());
    }

    @OnClick(R.id.lnlViewSalesOrder)
    public void lnlViewSalesOrderClicked(View view) {
        this.presenter.onNavigateToViewSalesOrderActivity(presenter.getPedido());

    }
}
