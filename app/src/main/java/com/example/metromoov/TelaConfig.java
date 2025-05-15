package com.example.metromoov;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaConfig extends AppCompatActivity {

    private Button btSalvar, btApagar, btConsultar;
    private EditText edLogin, edSenha;
    private TextView txtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);

        btSalvar = findViewById(R.id.btSalvar);
        btApagar = findViewById(R.id.btExcluir);
        btConsultar = findViewById(R.id.btConsultar);

        edLogin = findViewById(R.id.edLogin);
        edSenha = findViewById(R.id.edSenha);
        txtId = findViewById(R.id.txtId);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario e = new Usuario();
                e.setLogin(edLogin.getText().toString());
                e.setSenha(edSenha.getText().toString());

                UsuarioDAO dao = new UsuarioDAO(TelaConfig.this);

                // Verifica se o campo ID está preenchido para atualizar ou salvar
                if (!txtId.getText().toString().isEmpty()) {
                    e.setId(Integer.parseInt(txtId.getText().toString()));
                    dao.atualizarUsuario(e);
                    Toast.makeText(TelaConfig.this, "Entrevistador atualizado", Toast.LENGTH_SHORT).show();
                } else {
                    dao.salvarUsuario(e);
                    Toast.makeText(TelaConfig.this, "Entrevistador salvo", Toast.LENGTH_SHORT).show();
                }

                limparCampos();
                esconderTeclado();
            }
        });

        btApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtId.getText().toString().isEmpty()) {
                    UsuarioDAO dao = new UsuarioDAO(TelaConfig.this);
                    dao.excluirUsuario(Integer.parseInt(txtId.getText().toString()));
                    Toast.makeText(TelaConfig.this, "Entrevistador excluído", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    esconderTeclado();
                } else {
                    Toast.makeText(TelaConfig.this, "Nenhum entrevistador para excluir", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioDAO dao = new UsuarioDAO(TelaConfig.this);
                Usuario e = dao.consultarUsuario(edLogin.getText().toString());
                if (e != null) {
                    txtId.setText(String.valueOf(e.getId()));
                    edLogin.setText(e.getLogin());
                    edSenha.setText(e.getSenha());
                } else {
                    Toast.makeText(TelaConfig.this, "Entrevistador não encontrado", Toast.LENGTH_SHORT).show();
                }
                esconderTeclado();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPesquisa), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void limparCampos() {
        edLogin.setText("");
        edSenha.setText("");
        txtId.setText("");
    }

    private void esconderTeclado() {
        View viewAtual = getCurrentFocus();
        if (viewAtual != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewAtual.getWindowToken(), 0);
        }
    }
}
