package com.example.metromoov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EstacoesDAO {
    private final SQLiteDatabase db;
    public EstacoesDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void salvarEstacao(Estacoes estacao) {
        ContentValues valores = new ContentValues();
        valores.put("nome", estacao.getNome());
        valores.put("descricao", estacao.getDescricao());
        db.insert("estacoes", null, valores);
    }

    public void atualizarEstacao(Estacoes estacao) {
        ContentValues valores = new ContentValues();
        valores.put("nome", estacao.getNome());
        valores.put("descricao", estacao.getDescricao());
        String[] whereArgs = { String.valueOf(estacao.getId()) };
        db.update("estacoes", valores, "id = ?", whereArgs);
    }

    public void excluirEstacao(int id) {
        String[] whereArgs = { String.valueOf(id) };
        db.delete("estacoes", "id = ?", whereArgs);
    }

    public Estacoes consultarEstacaoPorId(int id) {
        String[] campos = { "id", "nome", "descricao" };
        String[] whereArgs = { String.valueOf(id) };
        Cursor cursor = db.query("estacoes", campos, "id = ?", whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Estacoes estacao = new Estacoes();
            estacao.setId(cursor.getInt(0));
            estacao.setNome(cursor.getString(1));
            estacao.setDescricao(cursor.getString(2));
            cursor.close();
            return estacao;
        }

        cursor.close();
        return null;
    }

    public List<Estacoes> listarTodasEstacoes() {
        List<Estacoes> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, nome, descricao FROM estacoes", null);

        while (cursor.moveToNext()) {
            Estacoes estacao = new Estacoes();
            estacao.setId(cursor.getInt(0));
            estacao.setNome(cursor.getString(1));
            estacao.setDescricao(cursor.getString(2));
            lista.add(estacao);
        }

        cursor.close();
        return lista;
    }
}
