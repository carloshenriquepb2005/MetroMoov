package com.example.metromoov;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaLogin extends AppCompatActivity {
    Button btLogin;
    EditText edUsuario, edSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btLogin = findViewById(R.id.btLogin);
        edUsuario = findViewById(R.id.edUsuario);
        edSenha = findViewById(R.id.edSenha);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esconderTeclado();

                String usuario = edUsuario.getText().toString().trim();
                String senha = edSenha.getText().toString().trim();

                if (usuario.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(TelaLogin.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                UsuarioDAO dao = new UsuarioDAO(TelaLogin.this);
                Usuario entrevistador = dao.consultarUsuario(usuario);

                if (entrevistador != null && entrevistador.getSenha().equals(senha)) {
                    if (entrevistador.getLogin().equalsIgnoreCase("admin")) {
                        // Acesso de administrador
                        Intent intent = new Intent(TelaLogin.this, TelaAdmin.class);
                        startActivity(intent);
                    } else {
                        // Acesso de entrevistador comum
                        Intent intent = new Intent(TelaLogin.this, TelaPesquisa.class);
                        startActivity(intent);
                    }
                } else {
                    // Login inválido
                    Toast.makeText(TelaLogin.this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLogin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void esconderTeclado() {
        View viewAtual = getCurrentFocus();
        if (viewAtual != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewAtual.getWindowToken(), 0);
        }
    }
}
