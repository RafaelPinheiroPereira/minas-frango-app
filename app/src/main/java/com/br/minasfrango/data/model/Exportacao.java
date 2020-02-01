package com.br.minasfrango.data.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exportacao  implements Serializable {
    ListaPedido listaPedido;
    List<Recebimento> recebimentos;

}
