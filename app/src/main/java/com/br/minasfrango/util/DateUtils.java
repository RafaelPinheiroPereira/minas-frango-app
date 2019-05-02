package com.br.minasfrango.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    private static final SimpleDateFormat FORMATADOR_dd_MM_YYYY_hh_mm =
            new SimpleDateFormat("dd/MM/yyyy hh:mm");

    private static final SimpleDateFormat FORMATADOR_dd_MM_YYYY =
            new SimpleDateFormat("dd/MM/yyyy");

    public static String formatarDateddMMyyyyParaString(java.util.Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY.format(dateToFormat);
    }

    public static java.util.Date formatarDateddMMyyyyhhmm(java.util.Date dateToFormat)
            throws ParseException {

        String strDate = FORMATADOR_dd_MM_YYYY_hh_mm.format(dateToFormat);

        return FORMATADOR_dd_MM_YYYY_hh_mm.parse(strDate);
    }

    public static String formatarDateddMMyyyyhhmmParaString(java.util.Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY_hh_mm.format(dateToFormat);
    }

    public static String retirarBarrasDaDataNoPadraoddMMyyyyParaString(
            java.util.Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY.format(dateToFormat).replace("/", "");
    }
}
