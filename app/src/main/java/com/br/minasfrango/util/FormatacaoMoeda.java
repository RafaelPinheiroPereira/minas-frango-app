package com.br.minasfrango.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatacaoMoeda {

    public static String converterParaDolar(Double value) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(value);
    }

    public static String converterParaReal(double doubleValue) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(doubleValue);
    }

    public static String formatarValorSemSimboloMonetarioComDuasCasasDecimais(double doubleValue) {
        return new DecimalFormat("0.00").format(doubleValue);
    }
}
