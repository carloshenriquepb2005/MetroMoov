package com.example.metromoov;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//importações da geolocalização
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONObject;

public class TelaPesquisa extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;
    TextView tvData, tvLocalizacao;
    EditText edCelular, edNome;
    Button btFinalizar, btSair;
    Spinner spOrigem, spDestino;
    private String enderecoAtual = "Endereço não disponível";

    private PessoaDAO pessoaDAO; // 🔹 Novo: DAO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa);

        // Inicializações
        tvLocalizacao = findViewById(R.id.tvLocalizacao);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tvLocalizacao.setText("Obtendo endereço...");
        obterLocalizacao();

        tvData = findViewById(R.id.tvData);
        edCelular = findViewById(R.id.edCelular);
        spOrigem = findViewById(R.id.spOrigem);
        spDestino = findViewById(R.id.spDestino);
        edNome = findViewById(R.id.edNome);
        btFinalizar = findViewById(R.id.btFinalizar);
        btSair = findViewById(R.id.btSair);

        pessoaDAO = new PessoaDAO(this);

        // Data/hora atual
        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dataAtual = data.format(new Date());
        String horaAtual = hora.format(new Date());
        tvData.setFocusable(false);
        tvData.setClickable(false);
        tvData.setText(String.format("Data: %s\n\nHora: %s", dataAtual, horaAtual));

        // Spinners
        String[] opcoes = {
                "Clique para escolher a origem",
                // Linha 2 - Verde
                "Vila Prudente",
                "Tamanduateí",
                "Sacomã",
                "Alto do Ipiranga",
                "Santos-Imigrantes",
                "Chácara Klabin",
                "Ana Rosa",
                "Paraíso",
                "Brigadeiro",
                "Trianon-Masp",
                "Consolação",
                "Clínicas",
                "Sumaré",
                "Vila Madalena",

                "Clique para escolher o destino",
                // Linha 4 - Amarela
                "Luz",
                "República",
                "Higienópolis-Mackenzie",
                "Paulista",
                "Faria Lima",
                "Pinheiros",
                "Butantã",
                "São Paulo-Morumbi",
                "Fradique Coutinho",
                "Oscar Freire",
                "Vila Sônia"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        spOrigem.setAdapter(adapter);
        spDestino.setAdapter(adapter);

        btFinalizar.setOnClickListener(v -> {
            String nome = edNome.getText().toString().trim();
            String celular = edCelular.getText().toString().trim();
            String origem = spOrigem.getSelectedItem().toString();
            String destino = spDestino.getSelectedItem().toString();

            if (nome.isEmpty() || celular.isEmpty() || origem.equals("Clique para escolher a origem") || destino.equals("Clique para escolher o destino")) {
                Toast.makeText(TelaPesquisa.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            Pessoa pessoa = new Pessoa(nome, celular, dataAtual, horaAtual, enderecoAtual, origem, destino);
            pessoaDAO.salvarPessoa(pessoa);

            Toast.makeText(TelaPesquisa.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

            // Reinicia a própria Activity para nova pesquisa (limpando campos)
            finish();
            startActivity(getIntent());
        });

        btSair.setOnClickListener(v -> {
            startActivity(new Intent(TelaPesquisa.this, TelaLogin.class));
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPesquisa), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    // localização (mesmo código anterior)
    private void obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    obterEnderecoGoogleAPI(latitude, longitude);
                }
            }
        }, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obterLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de localização negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obterEnderecoGoogleAPI(double lat, double lon) {
        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                lat + "," + lon + "&key=" + BuildConfig.MAPS_API_KEY;

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    sb.append(linha);
                }

                JSONObject respostaJson = new JSONObject(sb.toString());
                JSONArray results = respostaJson.getJSONArray("results");

                if (results.length() > 0) {
                    String enderecoCompleto = results.getJSONObject(0).getString("formatted_address");
                    enderecoAtual = enderecoCompleto;
                    runOnUiThread(() -> tvLocalizacao.setText("Endereço: " + enderecoCompleto));
                } else {
                    runOnUiThread(() -> tvLocalizacao.setText("Endereço não encontrado."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> tvLocalizacao.setText("Erro ao consultar endereço."));
            }
        }).start();
    }
}

