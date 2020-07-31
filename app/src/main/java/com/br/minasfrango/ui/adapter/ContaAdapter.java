package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Conta;
import java.util.List;

public class ContaAdapter extends BaseAdapter {
    LayoutInflater inflter;
    Context context;
    List<Conta> mContas;

    public ContaAdapter(Context applicationContext, List<Conta> contas) {
        this.context = applicationContext;
        this.mContas = contas;

        inflter = (LayoutInflater.from(applicationContext));
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return this.mContas.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Conta getItem(final int position) {
        return this.mContas.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(final int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)} to specify a root view and to prevent
     * attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *     we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *     is non-null and of an appropriate type before using. If it is not possible to convert
     *     this view to display the correct data, this method can create a new view. Heterogeneous
     *     lists can specify their number of view types, so that this View is always of the right
     *     type (see {@link #getViewTypeCount()} and {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = inflter.inflate(R.layout.item_conta_adapter, null);


        TextView txtBanco = convertView.findViewById(R.id.txt_banco);


        txtBanco.setText(this.mContas.get(position).getDescricao());


        return convertView;
    }
}
