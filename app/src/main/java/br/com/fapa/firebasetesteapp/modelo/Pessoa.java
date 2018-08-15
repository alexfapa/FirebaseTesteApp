package br.com.fapa.firebasetesteapp.modelo;

/**
 * Created by alexpinto on 01/05/18.
 */

public class Pessoa {
    private String uid;
    private String nome;
    private String email;

    public Pessoa() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nome;
    }
}
