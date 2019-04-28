package com.br.minasfrango.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatacaoMoeda {

    public static String convertDoubleToString(Double value) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
    }
}
