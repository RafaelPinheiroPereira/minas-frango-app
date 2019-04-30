package com.br.minasfrango.ui.listener;

import android.app.ProgressDialog;
import com.datecs.api.printer.Printer;
import java.io.IOException;
import java.text.ParseException;

public interface IPrinterRunnable {

    void run(ProgressDialog dialog, Printer printer) throws IOException, ParseException;
}
