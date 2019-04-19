package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.pojo.ImportacaoDados;
import java.util.ArrayList;

public class ImportacaoDadosAdapter extends ArrayAdapter<ImportacaoDados> {
		
		private ArrayList<ImportacaoDados> importacoes;
		Context ctx;
		
		public ImportacaoDadosAdapter(Context context, int textViewResourceId,
		                              ArrayList<ImportacaoDados> importacoes) {
				super(context, textViewResourceId, importacoes);
				this.importacoes = new ArrayList<ImportacaoDados>();
				this.ctx = context;
				this.importacoes.addAll(importacoes);
		}
		
		private class ViewHolder {
				TextView descricao;
				CheckBox checkBox;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
				
				ViewHolder holder = null;
				Log.v("ConvertView", String.valueOf(position));
				
				if (convertView == null) {
						LayoutInflater vi = (LayoutInflater) ctx.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);
						convertView = vi.inflate(R.layout.item_importacao_dados, null);
						
						holder = new ViewHolder();
                    holder.descricao = convertView.findViewById(R.id.txt_descricao);
                    holder.checkBox = convertView.findViewById(R.id.check_box);
						convertView.setTag(holder);
						
						holder.checkBox.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
										CheckBox cb = (CheckBox) v;
										ImportacaoDados importacaoDados = (ImportacaoDados) cb.getTag();
										
										importacaoDados.setSelected(cb.isChecked());
								}
						});
				} else {
						holder = (ViewHolder) convertView.getTag();
				}
				
				ImportacaoDados importacaoDados = importacoes.get(position);
				holder.descricao.setText(importacaoDados.getDescricao());
				
				holder.checkBox.setChecked(importacaoDados.isSelected());
				holder.checkBox.setText(" ");
				holder.checkBox.setTag(importacaoDados);
				
				return convertView;
				
		}
}
