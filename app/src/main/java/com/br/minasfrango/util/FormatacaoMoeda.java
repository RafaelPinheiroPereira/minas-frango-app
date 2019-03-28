package com.br.minasfrango.util;

import android.util.Log;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class FormatacaoMoeda {





    public static double converteStringDoubleValorMoeda(String valorMoedaString)  {
        valorMoedaString=valorMoedaString.replace(".","");
        valorMoedaString=valorMoedaString.replace(",",".");
        return Double.valueOf(valorMoedaString);

    }

}
