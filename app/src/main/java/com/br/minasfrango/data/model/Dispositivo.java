package com.br.minasfrango.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispositivo {

    private String enderecoBluetooth;

    private int icone;

    private String nome;
}
