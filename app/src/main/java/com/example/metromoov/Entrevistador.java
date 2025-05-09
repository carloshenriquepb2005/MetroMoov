package com.example.metromoov;

public class Entrevistador {
    private int id;
    private String login;
    private String senha;
    private boolean isAdmin;
    public Entrevistador() {
        this.id = 0;
        this.login = "";
        this.senha = "";
    }
    public Entrevistador(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}

