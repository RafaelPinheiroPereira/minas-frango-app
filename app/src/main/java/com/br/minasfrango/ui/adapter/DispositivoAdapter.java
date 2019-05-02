package com.br.minasfrango.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Dispositivo;
import com.br.minasfrango.ui.mvp.impressora.IImpressoraMVP.IPresenter;
import java.util.ArrayList;
import java.util.List;

public class DispositivoAdapter extends BaseAdapter {

    private List<Dispositivo> mDispositivos = new ArrayList<>();

    private IPresenter mPresenter;

    public DispositivoAdapter(final IPresenter presenter) {

        this.mPresenter = presenter;
    }

    public void add(String nome, String enderecoBluetooth, int icone) {
        Dispositivo impressora = new Dispositivo(enderecoBluetooth, icone, nome);
        mDispositivos.add(impressora);
    }

    public void clear() {
        mDispositivos.clear();
    }

    public Dispositivo find(String address) {
        for (Dispositivo d : mDispositivos) {
            if (address.equals(d.getEnderecoBluetooth())) {
                return d;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return mDispositivos.size();
    }

    @Override
    public Object getItem(int location) {
        return mDispositivos.get(location);
    }

    @Override
    public long getItemId(int location) {
        return location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get layout to populate
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(mPresenter.getContext());
            v = vi.inflate(R.layout.device_node, null);
        }

        // Populate the layout with new data
        Dispositivo impressora = (Dispositivo) getItem(position);
        ((ImageView) v.findViewById(R.id.icon)).setImageResource(impressora.getIcone());
        ((TextView) v.findViewById(R.id.name)).setText(impressora.getNome());
        ((TextView) v.findViewById(R.id.address)).setText(impressora.getEnderecoBluetooth());

        return v;
    }
}

