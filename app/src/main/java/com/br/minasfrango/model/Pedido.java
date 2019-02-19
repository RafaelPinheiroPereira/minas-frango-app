package com.br.minasfrango.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido extends RealmObject implements Serializable {
		
		
				@PrimaryKey
				private long id;
				private long codigoCliente;
				private long codigoFuncionario;
				private RealmList<ItemPedido> itens;
				private double valorTotal;
			  private long tipoRecebimento;
				private Date dataPedido;
				
				public List<ItemPedido> realmListToList(){
						List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
						for(ItemPedido aux:getItens()){
								itemPedidos.add(aux);
						}
						return itemPedidos;
				}
				
		}
