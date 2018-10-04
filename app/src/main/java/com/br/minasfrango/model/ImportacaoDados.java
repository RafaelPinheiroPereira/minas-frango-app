package com.br.minasfrango.model;

/**
 * Created by 04717299302 on 02/11/2016.
 */

public class ImportacaoDados {
    String codigo = null;
    String descricao = null;
    boolean selected = false;

    public ImportacaoDados(String codigo, String descricao, boolean selected) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.selected = selected;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
