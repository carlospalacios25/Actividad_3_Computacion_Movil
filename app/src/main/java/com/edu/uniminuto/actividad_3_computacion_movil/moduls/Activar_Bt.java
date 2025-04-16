package com.edu.uniminuto.actividad_3_computacion_movil.moduls;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.edu.uniminuto.actividad_3_computacion_movil.R;

import java.util.ArrayList;
import java.util.Set;

public class Activar_Bt extends AppCompatActivity {
    private static final String TAG = "Activar_Bt";
    public static final int REQUEST_ENABLE_BT = 102;

    private Button btnActivarBt;
    private Button btnDesactivarBt;
    private TextView tvBluetooth;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter<String> dispositivosAdapter;
    private ArrayList<String> listaDispositivos = new ArrayList<>();
    private ListView listaBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activar_bt);

        this.reference();
        this.btnActivarBt.setOnClickListener(this::ActivarBt);
        this.btnDesactivarBt.setOnClickListener(this::DesactivarBt);

        detectarEstadoBluetooth();
    }

    private void detectarEstadoBluetooth() {
        Log.d(TAG, "Detectando estado del Bluetooth");
        if (bluetoothAdapter == null) {
            tvBluetooth.setText("Estado del Bluetooth: No compatible");
            Log.d(TAG, "BluetoothAdapter es null");
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_LONG).show();
            return;
        }

        if (bluetoothAdapter.isEnabled()) {
            tvBluetooth.setText("Estado del Bluetooth: Activado");
            Log.d(TAG, "Bluetooth está activado");
            listarDispositivosEmparejados();
        } else {
            tvBluetooth.setText("Estado del Bluetooth: Desactivado");
            Log.d(TAG, "Bluetooth está desactivado");
            limpiarLista();
        }
    }

    private void ActivarBt(View view) {
        Log.d(TAG, "Botón ActivarBt presionado");
        if (bluetoothAdapter == null) {
            tvBluetooth.setText("Estado del Bluetooth: No compatible");
            Log.d(TAG, "BluetoothAdapter es null");
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                Log.d(TAG, "Solicitando permiso BLUETOOTH_CONNECT");
                return;
            }
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Log.d(TAG, "Solicitando activación de Bluetooth");
        } else {
            tvBluetooth.setText("Estado del Bluetooth: Activado");
            Toast.makeText(this, "Bluetooth ya está activado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Bluetooth ya activado");
            listarDispositivosEmparejados();
        }
    }

    private void DesactivarBt(View view) {
        Log.d(TAG, "Botón DesactivarBt presionado");
        if (bluetoothAdapter == null) {
            tvBluetooth.setText("Estado del Bluetooth: No compatible");
            Log.d(TAG, "BluetoothAdapter es null");
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Por favor desactiva Bluetooth manualmente", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Redirigiendo a ajustes para desactivar Bluetooth");
            limpiarLista();
            tvBluetooth.setText("Estado del Bluetooth: Desactivando...");
        } else {
            tvBluetooth.setText("Estado del Bluetooth: Desactivado");
            Toast.makeText(this, "Bluetooth ya estaba desactivado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Bluetooth ya estaba desactivado");
            limpiarLista();
        }
    }

    private void listarDispositivosEmparejados() {
        Log.d(TAG, "Listando dispositivos emparejados");
        if (bluetoothAdapter == null) {
            tvBluetooth.setText("Estado del Bluetooth: No compatible");
            Log.d(TAG, "BluetoothAdapter es null");
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                Log.d(TAG, "Solicitando permiso BLUETOOTH_CONNECT");
                return;
            }
        }

        if (!bluetoothAdapter.isEnabled()) {
            tvBluetooth.setText("Estado del Bluetooth: Desactivado");
            Toast.makeText(this, "Por favor, activa Bluetooth primero", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Bluetooth no está habilitado");
            return;
        }

        listaDispositivos.clear();
        dispositivosAdapter.notifyDataSetChanged();
        Log.d(TAG, "Lista de dispositivos limpiada");

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName() != null ? device.getName() : "Dispositivo desconocido";
                String deviceInfo = deviceName + " (" + device.getAddress() + ")";
                listaDispositivos.add(deviceInfo);
                Log.d(TAG, "Dispositivo emparejado añadido: " + deviceInfo);
            }
            dispositivosAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Dispositivos emparejados cargados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay dispositivos emparejados", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No se encontraron dispositivos emparejados");
        }
    }

    private void limpiarLista() {
        Log.d(TAG, "Limpiando lista de dispositivos");
        listaDispositivos.clear();
        dispositivosAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Lista de dispositivos limpiada", Toast.LENGTH_SHORT).show();
    }

    private void reference() {
        this.btnActivarBt = findViewById(R.id.btnActivarBt);
        this.btnDesactivarBt = findViewById(R.id.btnDesactivarBt);
        this.tvBluetooth = findViewById(R.id.tvBluetooth);
        this.listaBt = findViewById(R.id.lvBluetooth);
        this.dispositivosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDispositivos);
        this.listaBt.setAdapter(dispositivosAdapter);
        Log.d(TAG, "Referencias inicializadas");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                tvBluetooth.setText("Estado del Bluetooth: Activado");
                Toast.makeText(this, "Bluetooth activado", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Bluetooth activado, listando dispositivos emparejados");
                listarDispositivosEmparejados();
            } else {
                tvBluetooth.setText("Estado del Bluetooth: Desactivado");
                Toast.makeText(this, "Bluetooth no fue activado", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Bluetooth no activado por el usuario");
                limpiarLista();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: código=" + requestCode);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permiso concedido");
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Log.d(TAG, "Solicitando activación de Bluetooth tras permiso");
                } else {
                    tvBluetooth.setText("Estado del Bluetooth: Activado");
                    listarDispositivosEmparejados();
                    Log.d(TAG, "Permiso concedido, listando dispositivos");
                }
            } else {
                Toast.makeText(this, "Permiso denegado, no se puede continuar", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Permiso denegado");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        detectarEstadoBluetooth();
    }
}