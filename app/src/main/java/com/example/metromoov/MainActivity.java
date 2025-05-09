package com.example.metromoov;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EntrevistadorDAO dao = new EntrevistadorDAO(this);
        Entrevistador admin = dao.consultarEntrevistadorPorLogin("admin");

        if (admin == null) {
            Entrevistador novoAdmin = new Entrevistador();
            novoAdmin.setLogin("admin");
            novoAdmin.setSenha("1234");
            novoAdmin.setAdmin(true);
            dao.salvarEntrevistador(novoAdmin);
        }
        Entrevistador existente = dao.consultarEntrevistadorPorLogin("ent");

        if (existente == null) {
            Entrevistador e = new Entrevistador();
            e.setLogin("ent");
            e.setSenha("1234");
            e.setAdmin(false); // não é administrador
            dao.salvarEntrevistador(e);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }, 2000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
