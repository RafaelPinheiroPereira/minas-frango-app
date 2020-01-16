package com.br.minasfrango.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.DispositivoImpressora;
import com.br.minasfrango.ui.adapter.DispositivoAdapter;
import com.br.minasfrango.ui.mvp.impressora.IImpressoraMVP;
import com.br.minasfrango.ui.mvp.impressora.Presenter;
import com.br.minasfrango.util.ControleSessao;
import java.util.Set;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity implements IImpressoraMVP.IView {

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Address preference name 
    public static String PREF_DEVICE_ADDRESS = "device_address";

    private static final int PRIVATE_MODE = 0;

    @BindView(R.id.btnConectar)
    Button btnConectar;

    @BindView(R.id.btnEscanear)
    Button btnEscanear;

    @BindView(R.id.edtEnderecoBluetooth)
    EditText edtEnderecoBluetooth;

    @BindView(R.id.lvDispositivos)
    ListView lvDispositivos;

    IImpressoraMVP.IPresenter mPresenter;

    ControleSessao mControleSessao;

    // Represents the local device Bluetooth adapter.
    private BluetoothAdapter mBtAdapter;

    // Device adapter which handle list of devices.
    private DispositivoAdapter mDevicesAdapter;

    // The on-click listener for all devices in the ListViews
    private final OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int location, long id) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            DispositivoImpressora dispositivoImpressora = (DispositivoImpressora) mDevicesAdapter.getItem(location);
            String address = dispositivoImpressora.getEnderecoBluetooth();

            if (BluetoothAdapter.checkBluetoothAddress(address)) {
                salvarEnderecoBluetoothPreferences(address);
            }
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                boolean bonded = device.getBondState() == BluetoothDevice.BOND_BONDED;
                int iconId = bonded ? R.mipmap.ic_bluetooth_connected_black_48dp : R.mipmap.ic_bluetooth_black_48dp;
                // Find is device is already exists
                DispositivoImpressora dispositivoImpressora = mDevicesAdapter.find(device.getAddress());
                // Skip if device is already in list
                if (dispositivoImpressora == null) {
                    mDevicesAdapter.add(device.getName(), device.getAddress(), iconId);
                } else {
                    dispositivoImpressora.setNome(device.getName());
                    dispositivoImpressora.setIcone(iconId);
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.title_select_device);
                findViewById(R.id.lnlEscanear).setVisibility(View.VISIBLE);
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        ButterKnife.bind(this);

        mControleSessao = new ControleSessao(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        }

        mPresenter = new Presenter(this);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mDevicesAdapter = new DispositivoAdapter(mPresenter);

        // Initialize the button to perform connect
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        edtEnderecoBluetooth = findViewById(R.id.edtEnderecoBluetooth);
        edtEnderecoBluetooth.setText(prefs.getString(PREF_DEVICE_ADDRESS, ""));

        btnConectar.setOnClickListener(v->{
            String address = edtEnderecoBluetooth.getText().toString();
            salvarEnderecoBluetoothPreferences(address);
        });

        lvDispositivos.setAdapter(mDevicesAdapter);
        lvDispositivos.setOnItemClickListener(mDeviceClickListener);

        // Initialize the button to perform device discovery

        btnEscanear.setOnClickListener(v->startDiscovery());

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);

        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            // Get a set of currently paired devices
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

            // If there are paired devices, add each one to the ArrayAdapter
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    mDevicesAdapter
                            .add(device.getName(), device.getAddress(), R.mipmap.ic_bluetooth_connected_black_48dp);
                }
            }
            findViewById(R.id.txtTituloDesabilitado).setVisibility(View.GONE);
        } else {
            findViewById(R.id.lnlEscanear).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREF_DEVICE_ADDRESS, edtEnderecoBluetooth.getText().toString());
        edit.commit();

        // Make sure we're not doing discovery anymore
        cancelDiscovery();

        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_FIRST_USER);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void cancelDiscovery() {
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
    }

    private void salvarEnderecoBluetoothPreferences(String address) {

        mControleSessao.salvarEnderecoBluetooth(address);

        finish();
    }

    private void startDiscovery() {
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.title_scanning);
        findViewById(R.id.lnlEscanear).setVisibility(View.GONE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }
}
