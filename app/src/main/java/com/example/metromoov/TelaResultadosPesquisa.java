package com.example.metromoov;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class TelaResultadosPesquisa extends AppCompatActivity {

    private TextView tvResultados;
    private Button btnZerarTotais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultadospesquisa);

        tvResultados = findViewById(R.id.tvResultados);
        btnZerarTotais = findViewById(R.id.btnZerarTotais);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainResultadosPesquisa), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carregarResultados();

        btnZerarTotais.setOnClickListener(v -> {
            PessoaDAO dao = new PessoaDAO(this);
            dao.zerarTotais(); // Apaga tudo
            carregarResultados();
        });
    }

    private void carregarResultados() {
        PessoaDAO dao = new PessoaDAO(this);
        Cursor cursor = dao.listarPessoas();

        int total = cursor.getCount();

        if (total == 0) {
            tvResultados.setText("Nenhum dado encontrado na pesquisa.");
            cursor.close();
            return;
        }

        int idxOrigem = cursor.getColumnIndex("origem");
        int idxDestino = cursor.getColumnIndex("destino");

        if (idxOrigem == -1 || idxDestino == -1) {
            tvResultados.setText("Erro: colunas 'origem' ou 'destino' não encontradas.");
            cursor.close();
            return;
        }

        Map<String, Integer> origemMap = new HashMap<>();
        Map<String, Integer> destinoMap = new HashMap<>();
        Map<String, Integer> origemDestinoMap = new HashMap<>();

        while (cursor.moveToNext()) {
            String origem = cursor.getString(idxOrigem);
            String destino = cursor.getString(idxDestino);

            origemMap.put(origem, origemMap.getOrDefault(origem, 0) + 1);
            destinoMap.put(destino, destinoMap.getOrDefault(destino, 0) + 1);
            String chave = origem + " → " + destino;
            origemDestinoMap.put(chave, origemDestinoMap.getOrDefault(chave, 0) + 1);
        }

        cursor.close();

        StringBuilder sb = new StringBuilder();
        sb.append("📊 RESULTADOS DA PESQUISA\n");
        sb.append("Total de Respostas: ").append(total).append("\n\n");

        sb.append("📍 Por ORIGEM:\n");
        for (String origem : origemMap.keySet()) {
            int qtd = origemMap.get(origem);
            double perc = (qtd * 100.0 / total);
            sb.append("- ").append(origem).append(": ").append(qtd)
                    .append(" (").append(String.format("%.1f", perc)).append("%)\n");
        }

        sb.append("\n🎯 Por DESTINO:\n");
        for (String destino : destinoMap.keySet()) {
            int qtd = destinoMap.get(destino);
            double perc = (qtd * 100.0 / total);
            sb.append("- ").append(destino).append(": ").append(qtd)
                    .append(" (").append(String.format("%.1f", perc)).append("%)\n");
        }

        sb.append("\n🔄 Por ORIGEM → DESTINO:\n");
        for (String chave : origemDestinoMap.keySet()) {
            int qtd = origemDestinoMap.get(chave);
            double perc = (qtd * 100.0 / total);
            sb.append("- ").append(chave).append(": ").append(qtd)
                    .append(" (").append(String.format("%.1f", perc)).append("%)\n");
        }

        tvResultados.setText(sb.toString());
    }

}
