package com.example.metromoov;

public class Estacoes {
    private int id;
    private String nome;
    private String linha;

    public Estacoes() {
    }

    public Estacoes(int id, String nome, String linha) {
        this.id = id;
        this.nome = nome;
        this.linha = linha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLinha() {
        return linha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }
}
