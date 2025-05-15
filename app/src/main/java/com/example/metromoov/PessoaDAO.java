package com.example.metromoov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PessoaDAO {

    private static final String TABELA_PESSOA = "pessoa";

    private static final String COLUNA_ID = "id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_CELULAR = "celular";
    private static final String COLUNA_DATA = "data";
    private static final String COLUNA_HORA = "hora";
    private static final String COLUNA_ENDERECO = "endereco";
    private static final String COLUNA_ORIGEM = "origem";
    private static final String COLUNA_DESTINO = "destino";

    private final SQLiteDatabase db;

    public PessoaDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public void salvarPessoa(Pessoa p) {
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, p.getNome());
        valores.put(COLUNA_CELULAR, p.getCelular());
        valores.put(COLUNA_DATA, p.getData());
        valores.put(COLUNA_HORA, p.getHora());
        valores.put(COLUNA_ENDERECO, p.getEndereco());
        valores.put(COLUNA_ORIGEM, p.getOrigem());
        valores.put(COLUNA_DESTINO, p.getDestino());
        db.insert(TABELA_PESSOA, null, valores);
    }

    public void atualizarPessoa(int id, Pessoa p) {
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, p.getNome());
        valores.put(COLUNA_CELULAR, p.getCelular());
        valores.put(COLUNA_DATA, p.getData());
        valores.put(COLUNA_HORA, p.getHora());
        valores.put(COLUNA_ENDERECO, p.getEndereco());
        valores.put(COLUNA_ORIGEM, p.getOrigem());
        valores.put(COLUNA_DESTINO, p.getDestino());
        String[] parametros = { String.valueOf(id) };
        db.update(TABELA_PESSOA, valores, "id = ?", parametros);
    }

    public void excluirPessoa(int id) {
        String[] parametros = { String.valueOf(id) };
        db.delete(TABELA_PESSOA, "id = ?", parametros);
    }

    public Pessoa consultarPessoaPorId(int id) {
        Pessoa p = null;
        String[] parametros = { String.valueOf(id) };

        Cursor cr = db.query(
                TABELA_PESSOA,
                new String[]{COLUNA_NOME, COLUNA_CELULAR, COLUNA_DATA, COLUNA_HORA, COLUNA_ENDERECO, COLUNA_ORIGEM, COLUNA_DESTINO},
                "id = ?",
                parametros,
                null,
                null,
                null
        );

        if (cr.moveToFirst()) {
            p = new Pessoa(
                    cr.getString(0), // nome
                    cr.getString(1), // celular
                    cr.getString(2), // data
                    cr.getString(3), // hora
                    cr.getString(4), // endereco
                    cr.getString(5), // origem
                    cr.getString(6)  // destino
            );
        }

        cr.close();
        return p;
    }
}
