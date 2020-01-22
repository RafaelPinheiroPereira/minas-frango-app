package com.br.minasfrango.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.listener.IPrinterRunnable;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ImpressoraUtil {

    private interface PrinterRunnable {

        void run(ProgressDialog dialog, Printer printer) throws IOException;
    }

    AbstractActivity abstractActivity = new AbstractActivity();

    Activity activity;

    ControleSessao mControleSessao;

    private BluetoothSocket mBtSocket;

    private Printer mPrinter;

    private ProtocolAdapter.Channel mPrinterChannel;

    private ProtocolAdapter mProtocolAdapter;

    public ImpressoraUtil(final Activity activity) {
        this.activity = activity;
        mControleSessao = new ControleSessao(this.activity);
    }

    public synchronized boolean esperarPorConexao() {

        status(null);
        fecharConexaoAtiva();
        String adress = mControleSessao.getEnderecoBluetooth();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            // Checa conexao
            if (BluetoothAdapter.checkBluetoothAddress(adress)) {
                estabelecerConexaoBluetooth(adress);
                return true;
            } else {
                AbstractActivity.showToast(
                        activity.getApplicationContext(), "Não foi possível  estabelecer conexão");
                return false;
            }
        } else {
            AbstractActivity.showToast(
                    activity.getApplicationContext(),
                    "Bluetooth desligado!\nPor favor, habilite o bluetooth!");
            return false;
        }
    }

    public void fecharConexaoAtiva() {
        fecharConexaoBluetooth();
    }

    public void imprimirComprovantePedido(final Pedido pedido, Cliente cliente) {

        runTask(
                (dialog, printer) -> {
                    imprimirLogo(printer);

                    StringBuffer textBuffer = configurarLayoutImpressaoPedido(pedido, cliente);

                    printer.reset();
                    printer.printTaggedText(textBuffer.toString());
                    printer.feedPaper(110);
                    printer.flush();
                },
                R.string.acessar);
    }

    private void imprimirLogo(final Printer printer) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final AssetManager assetManager = this.activity.getAssets();
        final Bitmap bitmap =
                BitmapFactory.decodeStream(assetManager.open("logo.png"), null, options);
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int[] argb = new int[width * height];
        bitmap.getPixels(argb, 0, width, 0, 0, width, height);
        bitmap.recycle();

        printer.reset();
        printer.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
        printer.feedPaper(110);
        printer.flush();
    }

    public void imprimirComprovanteRecebimento(
            final List<Recebimento> recebimentos, final Cliente cliente) {
        runTask(
                (dialog, printer) -> {
                    imprimirLogo(printer);
                    StringBuffer textBuffer =
                            configurarLayoutImpressaoRecebimento(recebimentos, cliente);

                    printer.reset();
                    printer.printTaggedText(textBuffer.toString());
                    printer.feedPaper(110);
                    printer.flush();
                },
                R.string.acessar);
    }

    public void iniciarImpressao(InputStream inputStream, OutputStream outputStream)
            throws IOException {

        // Here you can enable various debug information
        // ProtocolAdapter.setDebug(true);
        Printer.setDebug(true);
        EMSR.setDebug(true);

        // Check if printer is into protocol mode. Ones the object is created it can not be released
        // without closing base streams.
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
        if (mProtocolAdapter.isProtocolEnabled()) {

            // Into protocol mode we can callbacks to receive printer notifications
            mProtocolAdapter.setPrinterListener(
                    new ProtocolAdapter.PrinterListener() {
                        @Override
                        public void onBatteryStateChanged(boolean lowBattery) {
                            if (lowBattery) {

                                status("LOW BATTERY");
                            } else {
                                status(null);
                            }
                        }

                        @Override
                        public void onPaperStateChanged(boolean hasPaper) {
                            if (hasPaper) {

                                status("PAPER OUT");
                            } else {
                                status(null);
                            }
                        }

                        @Override
                        public void onThermalHeadStateChanged(boolean overheated) {
                            if (overheated) {

                                status("OVERHEATED");
                            } else {
                                status(null);
                            }
                        }
                    });

            // Get printer instance
            mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            Printer printer =
                    new Printer(
                            mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());

        } else {

            // Protocol mode it not enables, so we should use the row streams.
            mPrinter =
                    new Printer(
                            mProtocolAdapter.getRawInputStream(),
                            mProtocolAdapter.getRawOutputStream());
        }

        mPrinter.setConnectionListener(
                () -> {
                    abstractActivity.errorMSG(this.activity, "Impressora está desconectada");

                    abstractActivity.runOnUiThread(
                            () -> {
                                if (!activity.isFinishing()) {
                                    esperarPorConexao();
                                }
                            });
                });
    }

    public void status(final String text) {

        abstractActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {}
                });

        abstractActivity.runOnUiThread(
                () -> {
                    if (text != null) {
                        AbstractActivity.showToast(activity.getApplicationContext(), text);
                    } else {

                    }
                });
    }

    private StringBuffer configurarLayoutImpressaoPedido(
            final Pedido pedido, final Cliente cliente) {
        StringBuffer textBuffer = new StringBuffer();
        textBuffer.append("{s}AV.PRINCESA LEOPOLDINA,220,TIJUPA QUEIMADO{br}");
        textBuffer.append("{s}CEP:65110000 SAO JOSE DE RIBAMAR, MA{br}");
        textBuffer.append("{s}CNPJ:41627969000174 I.E:121292070{br}");
        textBuffer.append("{s}FONE:(98)988962910{br}{br}");
        textBuffer.append("{center}{b}COMPROVANTE DE VENDAS {br}");
        textBuffer.append("{br}{reset}");
        textBuffer.append(
                "{b}VENDA: "

                        + String.format("%02d", this.mControleSessao.getIdNucleo())
                        + String.format("%03d", pedido.getCodigoFuncionario())

                        + String.format( "%05d",pedido.getIdVenda())
                        + "{br}");
        textBuffer.append(
                "{b}DATA/HORA: "
                        + DateUtils.formatarDateddMMyyyyhhmmParaString(pedido.getDataPedido())
                        + "{br}");
        textBuffer.append("{b}CLIENTE: " + cliente.getNome() + "{br}");

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            textBuffer.append(
                    "{b}BICOS: "
                            + pedido.getItens().stream().mapToInt(ItemPedido::getBicos).sum()
                            + "{br}");
        } else {
            int quantidadeBicos = 0;
            for (ItemPedido itemPedido : pedido.getItens()) {
                quantidadeBicos += itemPedido.getBicos();
            }
            textBuffer.append(
                    "{b}BICOS: "
                            + quantidadeBicos
                            + "{br}");
        }

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            textBuffer.append(
                    "{b}PESO: "
                            + String.format("%.2f", pedido.getItens().stream().mapToDouble(ItemPedido::getQuantidade).sum())
                            + " KG"
                            + "{br}");
        } else {
            Double pesoTotal = 0.0;
            for (ItemPedido itemPedido : pedido.getItens()) {
                pesoTotal += itemPedido.getQuantidade();
            }
            textBuffer.append("{b}PESO: " + String.format("%.2f",pesoTotal) + " KG" + "{br}");
        }
        textBuffer.append("VENDEDOR: " + new ControleSessao(this.activity).getUserName());
        textBuffer.append("{br}");
        textBuffer.append("{reset}{left}{w}{h}________________");
        textBuffer.append("{br}");
        textBuffer.append("{reset}{center}{b}Recebido{br}");
        return textBuffer;
    }

    private StringBuffer configurarLayoutImpressaoRecebimento(
            final List<Recebimento> recebimentos, final Cliente cliente) {

        // Calcular valor total amortizado
        Double valorTotalAmortizado = 0.0;
        String strDataRecebimento = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            valorTotalAmortizado =
                    recebimentos.stream().mapToDouble(Recebimento::getValorAmortizado).sum();
        } else {
            for (Recebimento recebimento : recebimentos) {
                valorTotalAmortizado += recebimento.getValorAmortizado();
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // Filtra um item checkado (Amortizado) para obter a data do recebimento
            Optional<Recebimento> recebimento =
                    recebimentos.stream().filter(item -> item.isCheck()).findAny();

            // Data do recebimento

            strDataRecebimento =
                    DateUtils.formatarDateddMMyyyyhhmmParaString(
                            recebimento.get().getDataRecebimento());
        } else {

            for (Recebimento recebimento : recebimentos) {
                if (recebimento.isCheck()) {
                    strDataRecebimento =
                            DateUtils.formatarDateddMMyyyyhhmmParaString(
                                    recebimento.getDataRecebimento());
                }
            }
        }

        StringBuffer textBuffer = new StringBuffer();
        textBuffer.append("{s}AV.PRINCESA LEOPOLDINA,220,TIJUPA QUEIMADO{br}");
        textBuffer.append("{s}CEP:65110000 SAO JOSE DE RIBAMAR, MA{br}");
        textBuffer.append("{s}CNPJ:41627969000174 I.E:121292070{br}");
        textBuffer.append("{s}FONE:(98)988962910{br}{br}");
        textBuffer.append("{reset}{center}{b}COMPROVANTE DE PAGAMENTOS {br}");
        textBuffer.append("{reset}");
        textBuffer.append(
                "{b}RECIBO: "
                        +recebimentos.get(0).getIdRecibo()

                        + "{br}");
        textBuffer.append("{b}CLIENTE: " + cliente.getNome() + "{br}");
        textBuffer.append(
                "{b}VALOR: " + String.format("%.2f",valorTotalAmortizado) + "{br}");
        textBuffer.append("{b}DATA/HORA: " + strDataRecebimento + "{br}");
        textBuffer.append("VENDEDOR: " + new ControleSessao(this.activity).getUserName());
        textBuffer.append("{br}");
        textBuffer.append("{reset}{left}{w}{h}________________");
        textBuffer.append("{br}");
        textBuffer.append("{reset}{center}{b}Recebido{br}");
        return textBuffer;
    }

    private void estabelecerConexaoBluetooth(final String address) {
        final ProgressDialog dialog = new ProgressDialog(this.activity);
        dialog.setTitle("Por favor , aguarde a conexão...");
        dialog.setMessage("Conectando o dispositivo");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        final Thread t =
                new Thread(
                        () -> {
                            btAdapter.cancelDiscovery();
                            try {
                                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                                BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);

                                InputStream in = null;
                                OutputStream out = null;

                                try {
                                    BluetoothSocket btSocket =
                                            btDevice.createRfcommSocketToServiceRecord(uuid);
                                    btSocket.connect();

                                    mBtSocket = btSocket;
                                    in = mBtSocket.getInputStream();
                                    out = mBtSocket.getOutputStream();
                                } catch (IOException e) {

                                    activity.runOnUiThread(
                                            () -> {
                                                AbstractActivity.showToast(
                                                        this.activity,
                                                        "Impressora desligada/desabilitada.\nFalha ao conectar:  "
                                                                + e.getMessage());
                                            });

                                    return;
                                }

                                try {
                                    iniciarImpressao(in, out);

                                } catch (IOException e) {
                                    abstractActivity.errorMSG(
                                            this.activity,
                                            "FAILED to initiallize: " + e.getMessage());
                                    esperarPorConexao();
                                    return;
                                }
                            } finally {
                                dialog.dismiss();
                            }
                        });
        t.start();
    }

    private synchronized void fecharConexaoBluetooth() {
        // Close Bluetooth connection
        BluetoothSocket s = mBtSocket;
        mBtSocket = null;
        if (s != null) {

            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runTask(final IPrinterRunnable r, final int msgResId) {
        final ProgressDialog dialog = new ProgressDialog(this.activity);
        dialog.setTitle("teste");
        dialog.setMessage("msg");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread t =
                new Thread(
                        () -> {
                            try {
                                r.run(dialog, mPrinter);
                            } catch (IOException e) {
                                e.printStackTrace();

                                abstractActivity.errorMSG(
                                        this.activity, "I/O error occurs: " + e.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                                abstractActivity.errorMSG(
                                        this.activity, "Critical error occurs: " + e.getMessage());

                            } finally {
                                dialog.dismiss();
                            }
                        });
        t.start();
    }
}
