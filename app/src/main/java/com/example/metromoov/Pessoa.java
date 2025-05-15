package com.example.metromoov;

public class Pessoa {
    private String nome;
    private String celular;
    private String data;
    private String hora;
    private String endereco;
    private String origem;
    private String destino;

    public Pessoa(String nome, String celular, String data, String hora, String endereco, String origem, String destino) {
        this.nome = nome;
        this.celular = celular;
        this.data = data;
        this.hora = hora;
        this.endereco = endereco;
        this.origem = origem;
        this.destino = destino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}

