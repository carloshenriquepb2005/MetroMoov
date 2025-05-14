package com.example.metromoov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PessoaDAO extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "bdMetroMoov";
    public static final int VERSAO_BANCO = 1;
    public static final String TABELA_PESSOA = "pessoa";

    public static final String COLUNA_ID = "id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_CELULAR = "celular";
    public static final String COLUNA_DATA = "data";
    public static final String COLUNA_HORA = "hora";
    public static final String COLUNA_ENDERECO = "endereco";
    public static final String COLUNA_ORIGEM = "origem";
    public static final String COLUNA_DESTINO = "destino";

    public PessoaDAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_PESSOA + " ("
                + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_NOME + " TEXT NOT NULL, "
                + COLUNA_CELULAR + " TEXT, "
                + COLUNA_DATA + " TEXT, "
                + COLUNA_HORA + " TEXT, "
                + COLUNA_ENDERECO + " TEXT, "
                + COLUNA_ORIGEM + " TEXT, "
                + COLUNA_DESTINO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PESSOA);
        onCreate(db);
    }

    public void salvarPessoa(Pessoa p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, p.nome);
        valores.put(COLUNA_CELULAR, p.celular);
        valores.put(COLUNA_DATA, p.data);
        valores.put(COLUNA_HORA, p.hora);
        valores.put(COLUNA_ENDERECO, p.endereco);
        valores.put(COLUNA_ORIGEM, p.origem);
        valores.put(COLUNA_DESTINO, p.destino);
        db.insert(TABELA_PESSOA, null, valores);
        db.close();
    }

    public void atualizarPessoa(int id, Pessoa p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, p.nome);
        valores.put(COLUNA_CELULAR, p.celular);
        valores.put(COLUNA_DATA, p.data);
        valores.put(COLUNA_HORA, p.hora);
        valores.put(COLUNA_ENDERECO, p.endereco);
        valores.put(COLUNA_ORIGEM, p.origem);
        valores.put(COLUNA_DESTINO, p.destino);
        String[] parametros = { String.valueOf(id) };
        db.update(TABELA_PESSOA, valores, "id = ?", parametros);
        db.close();
    }

    public void excluirPessoa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] parametros = { String.valueOf(id) };
        db.delete(TABELA_PESSOA, "id = ?", parametros);
        db.close();
    }

    public Pessoa consultarPessoaPorId(int id) {
        Pessoa p = null;
        SQLiteDatabase db = this.getReadableDatabase();
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
        db.close();
        return p;
    }
}
