package com.example.metromoov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaAdmin extends AppCompatActivity {

    Button btConfig, btDadosEntrevistadores, btDadosParticipantes, btResultadosPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        btConfig = findViewById(R.id.btConfig);
        btDadosParticipantes = findViewById(R.id.btDadosParticipantes);
        btDadosEntrevistadores = findViewById(R.id.btDadosEntrevistadores);
        btResultadosPesquisa = findViewById(R.id.btResultadosPesquisa);

        btConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAdmin.this, TelaConfig.class);
                startActivity(intent);
            }
        });

        btDadosParticipantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAdmin.this, TelaDadosParticipantes.class);
                startActivity(intent);
            }
        });

        btDadosEntrevistadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAdmin.this, TelaDadosEntrevistadores.class);
                startActivity(intent);
            }
        });

        btResultadosPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAdmin.this, TelaResultadosPesquisa.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAdmin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
