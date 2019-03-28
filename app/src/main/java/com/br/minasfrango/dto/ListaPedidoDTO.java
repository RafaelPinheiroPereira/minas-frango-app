package com.br.minasfrango.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaPedidoDTO implements Serializable {
		
		List<PedidoDTO> pedidosDTO;
}
