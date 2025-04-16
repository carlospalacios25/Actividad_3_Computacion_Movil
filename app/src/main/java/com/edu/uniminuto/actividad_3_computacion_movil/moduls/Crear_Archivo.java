package com.edu.uniminuto.actividad_3_computacion_movil.moduls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.edu.uniminuto.actividad_3_computacion_movil.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;

public class Crear_Archivo extends AppCompatActivity {
    private EditText edNombreArc;
    private TextView tvBaterry;
    private TextView tvVersion;
    private TextView estudianteUno;
    private TextView estudianteDos;
    private Button btnSave;
    private int versionSDK;
    private IntentFilter baterryFilter;
    private String nombreArchivo;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archivo_nuevo);

        this.reference();
        baterryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broReceiver,baterryFilter);
        btnSave.setOnClickListener(this::guardarArchivo);
    }

    private void guardarArchivo(View view){
        this.nombreArchivo = edNombreArc.getText().toString();

        if (nombreArchivo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre para el archivo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }

        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, nombreArchivo + ".txt");

            FileOutputStream fos = new FileOutputStream(file);
            String contenido = tvVersion.getText().toString() + "\n" + tvBaterry.getText().toString() +
                                "\n" + estudianteUno.getText().toString() +"\n" + estudianteDos.getText().toString();
            fos.write(contenido.getBytes());
            fos.flush();
            fos.close();

            Toast.makeText(this, "Archivo guardado en Descargas", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo guardar el archivo", Toast.LENGTH_SHORT).show();
        }
    }
    BroadcastReceiver broReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBaterry = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            tvBaterry.setText("Nivel de baterria: "+levelBaterry+"%");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        String versionSo= Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        tvVersion.setText("Verion SO: "+versionSo+" / SDK: "+versionSDK);
    }
    public void reference(){
        this.btnSave = findViewById(R.id.btnSave);
        this.edNombreArc = findViewById(R.id.edNombreArc);
        this.tvBaterry = findViewById(R.id.tvBaterry);
        this.tvVersion = findViewById(R.id.tvVersion);
        this.estudianteUno = findViewById(R.id.tvEstudiateUno);
        this.estudianteDos = findViewById(R.id.tvEstudiateDos);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, intentar guardar de nuevo
                guardarArchivo(null);
            } else {
                Toast.makeText(this, "Permiso denegado, no se puede guardar el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
