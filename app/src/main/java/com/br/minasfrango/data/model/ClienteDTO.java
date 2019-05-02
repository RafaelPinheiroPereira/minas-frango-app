package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.Localidade;
import lombok.Data;


@Data
public class ClienteDTO {
    private long id;

    private String nome;

    private String razaoSocial;

    private Localidade localidade;

    private String endereco;

    private String numero;

    private String bairro;

    private String cidade;

    private String cep;

    private String referencia;

    private String telefone;

    private String cpf;




}
