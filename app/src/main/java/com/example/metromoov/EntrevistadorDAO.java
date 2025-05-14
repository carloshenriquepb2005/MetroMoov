package com.example.metromoov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EntrevistadorDAO extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "bdMetroMoov";
    public static final int VERSAO_BANCO = 1;
    public static final String TABELA_ENTREVISTADOR = "entrevistador";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_LOGIN = "login";
    public static final String COLUNA_SENHA = "senha";
    public static final String COLUNA_ISADMIN = "isAdmin"; // NOVO CAMPO

    public EntrevistadorDAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELA_ENTREVISTADOR + " ("
                + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_LOGIN + " TEXT NOT NULL, "
                + COLUNA_SENHA + " TEXT NOT NULL, "
                + COLUNA_ISADMIN + " INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ENTREVISTADOR);
        onCreate(db);
    }

    public void salvarEntrevistador(Entrevistador e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_LOGIN, e.getLogin());
        valores.put(COLUNA_SENHA, e.getSenha());
        valores.put(COLUNA_ISADMIN, e.isAdmin() ? 1 : 0); // salvar isAdmin como inteiro
        db.insert(TABELA_ENTREVISTADOR, null, valores);
        db.close();
    }

    public void atualizarEntrevistador(Entrevistador e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_LOGIN, e.getLogin());
        valores.put(COLUNA_SENHA, e.getSenha());
        valores.put(COLUNA_ISADMIN, e.isAdmin() ? 1 : 0);
        String[] parametro = { String.valueOf(e.getId()) };
        db.update(TABELA_ENTREVISTADOR, valores, "id = ?", parametro);
        db.close();
    }

    public void excluirEntrevistador(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] parametro = { String.valueOf(id) };
        db.delete(TABELA_ENTREVISTADOR, "id = ?", parametro);
        db.close();
    }

    public Entrevistador consultarEntrevistadorPorLogin(String plogin) {
        Entrevistador e = null;
        String[] parametro = { plogin };
        String[] campos = { "id", "login", "senha", "isAdmin" };

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.query(
                TABELA_ENTREVISTADOR,
                campos,
                "login = ?",
                parametro,
                null,
                null,
                null
        );

        if (cr.moveToFirst()) {
            e = new Entrevistador();
            e.setId(cr.getInt(0));
            e.setLogin(cr.getString(1));
            e.setSenha(cr.getString(2));
            e.setAdmin(cr.getInt(3) == 1); // ler isAdmin como boolean
        }

        db.close();
        return e;
    }
}
