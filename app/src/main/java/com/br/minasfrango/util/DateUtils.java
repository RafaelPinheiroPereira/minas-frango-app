package com.br.minasfrango.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static java.util.Date formatDateDDMMYYYY(java.util.Date dateToFormat) throws ParseException {

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatador.format(dateToFormat);

        return formatador.parse(strDate);
    }


}
