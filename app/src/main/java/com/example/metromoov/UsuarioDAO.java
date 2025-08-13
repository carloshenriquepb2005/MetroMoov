package com.example.metromoov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO {

    private SQLiteDatabase db;

    public UsuarioDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public void salvarUsuario(Usuario e) {
        ContentValues valores = new ContentValues();
        valores.put("login", e.getLogin());
        valores.put("senha", e.getSenha());
        valores.put("isAdmin", e.isAdmin() ? 1 : 0);
        db.insert("usuario", null, valores);
    }

    public void atualizarUsuario(Usuario e) {
        ContentValues valores = new ContentValues();
        valores.put("login", e.getLogin());
        valores.put("senha", e.getSenha());
        valores.put("isAdmin", e.isAdmin() ? 1 : 0);
        String[] parametro = { String.valueOf(e.getId()) };
        db.update("usuario", valores, "id = ?", parametro);
    }

    public void excluirUsuario(int id) {
        String[] parametro = { String.valueOf(id) };
        db.delete("usuario", "id = ?", parametro);
    }

    public Usuario consultarUsuario(String login) {
        Usuario e = null;
        String[] parametro = { login };
        String[] campos = { "id", "login", "senha", "isAdmin" };

        Cursor cr = db.query(
                "usuario",
                campos,
                "login = ?",
                parametro,
                null,
                null,
                null
        );

        if (cr.moveToFirst()) {
            e = new Usuario();
            e.setId(cr.getInt(0));
            e.setLogin(cr.getString(1));
            e.setSenha(cr.getString(2));
            e.setAdmin(cr.getInt(3) == 1);
        }

        cr.close();
        return e;
    }
}
