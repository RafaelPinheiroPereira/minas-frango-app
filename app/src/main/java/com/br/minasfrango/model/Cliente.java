package com.br.minasfrango.model;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@RealmClass
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements RealmModel, Serializable {

    @PrimaryKey
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
