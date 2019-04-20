package com.br.minasfrango.data.dto;

import com.br.minasfrango.data.pojo.Pedido;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaPedido implements Serializable {

	List<Pedido> mPedidos;
}
