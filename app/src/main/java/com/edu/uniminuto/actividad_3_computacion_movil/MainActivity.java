 package com.edu.uniminuto.actividad_3_computacion_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edu.uniminuto.actividad_3_computacion_movil.moduls.Activar_Bt;
import com.edu.uniminuto.actividad_3_computacion_movil.moduls.Activar_camara;

 public class MainActivity extends AppCompatActivity {
    private Button btnIngresabl;
    private Button btnIngresaCam;
    private Intent intent;
     private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.reference();
        btnIngresabl.setOnClickListener(this::OpenBluetooth);
        btnIngresaCam.setOnClickListener(this::OpenCamera);
    }

    private void OpenCamera(View view) {
        this.intent = new Intent(MainActivity.this, Activar_camara.class);
        //Cambiarla
        startActivityForResult(intent, 1);

    }

    private void OpenBluetooth(View view) {
        this.intent = new Intent(MainActivity.this, Activar_Bt.class);
        //Cambiarla
        startActivityForResult(intent, 1);
    }
    private void reference(){
        this.btnIngresabl = findViewById(R.id.btnIngresabl);
        this.btnIngresaCam = findViewById(R.id.btnIngresaCam);
    }
}