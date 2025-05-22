package com.example.metromoov;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "bdMetroMoov";
    public static final int VERSAO_BANCO = 2;  // Atualize conforme necessidade

    // Tabelas e colunas Usuario
    public static final String TABELA_USUARIOS = "usuario";
    public static final String USR_COL_ID = "id";
    public static final String USR_COL_LOGIN = "login";
    public static final String USR_COL_SENHA = "senha";
    public static final String USR_COL_ISADMIN = "isAdmin";

    // Tabelas e colunas Pessoa
    public static final String TABELA_PESSOA = "pessoa";
    public static final String PESSOA_COL_ID = "id";
    public static final String PESSOA_COL_NOME = "nome";
    public static final String PESSOA_COL_CELULAR = "celular";
    public static final String PESSOA_COL_DATA = "data";
    public static final String PESSOA_COL_HORA = "hora";
    public static final String PESSOA_COL_ENDERECO = "endereco";
    public static final String PESSOA_COL_ORIGEM = "origem";
    public static final String PESSOA_COL_DESTINO = "destino";

    // Tabelas e colunas Estacoes
    public static final String TABELA_ESTACOES = "estacoes";
    public static final String EST_COL_ID = "id";
    public static final String EST_COL_NOME = "nome";
    public static final String EST_COL_DESCRICAO = "descricao";

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela Usuario
        String sqlUsuario = "CREATE TABLE " + TABELA_USUARIOS + " ("
                + USR_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USR_COL_LOGIN + " TEXT NOT NULL, "
                + USR_COL_SENHA + " TEXT NOT NULL, "
                + USR_COL_ISADMIN + " INTEGER DEFAULT 0)";
        db.execSQL(sqlUsuario);

        // Criação da tabela Pessoa
        String sqlPessoa = "CREATE TABLE " + TABELA_PESSOA + " ("
                + PESSOA_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PESSOA_COL_NOME + " TEXT NOT NULL, "
                + PESSOA_COL_CELULAR + " TEXT, "
                + PESSOA_COL_DATA + " TEXT, "
                + PESSOA_COL_HORA + " TEXT, "
                + PESSOA_COL_ENDERECO + " TEXT, "
                + PESSOA_COL_ORIGEM + " TEXT, "
                + PESSOA_COL_DESTINO + " TEXT)";
        db.execSQL(sqlPessoa);

        // Criação da tabela Estacoes
        String sqlEstacoes = "CREATE TABLE " + TABELA_ESTACOES + " ("
                + EST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EST_COL_NOME + " TEXT NOT NULL, "
                + EST_COL_DESCRICAO + " TEXT)";
        db.execSQL(sqlEstacoes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
