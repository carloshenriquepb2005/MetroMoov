package com.example.metromoov;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaDadosParticipantes extends AppCompatActivity {

    private TextView tvListaParticipantes;
    private Button btnZerarTotais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadosparticipantes);

        tvListaParticipantes = findViewById(R.id.tvListaParticipantes);
        btnZerarTotais = findViewById(R.id.btnZerarTotais);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainDadosParticipantes), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carregarParticipantes();

        btnZerarTotais.setOnClickListener(v -> {
            PessoaDAO dao = new PessoaDAO(this);
            dao.zerarTotais(); // Apaga tudo
            carregarParticipantes();
        });
    }

    private void carregarParticipantes() {
        PessoaDAO dao = new PessoaDAO(this);
        Cursor cursor = dao.listarTodos();

        StringBuilder lista = new StringBuilder();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String nome = cursor.getString(0);
                String celular = cursor.getString(1);
                String data = cursor.getString(2);
                String hora = cursor.getString(3);
                String endereco = cursor.getString(4); // Novo campo

                lista.append("🧍 Nome: ").append(nome).append("\n")
                        .append("📱 Celular: ").append(celular).append("\n")
                        .append("📅 Data: ").append(data).append(" 🕒 Hora: ").append(hora).append("\n")
                        .append("📍 Localização: ").append(endereco).append("\n")
                        .append("------------------------------------------------\n");
            }
        } else {
            lista.append("Nenhum participante encontrado.");
        }

        cursor.close();
        tvListaParticipantes.setText(lista.toString());
    }

}
