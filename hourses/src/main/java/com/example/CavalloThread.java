package com.example;

public class CavalloThread extends Thread{

    private String nome;
    private int tempo;

    public String getNome() {
        return nome;
    }


    public int getTempo() {
        return tempo;
    }


    public CavalloThread (String nome) {
        this.nome = nome;
    }


    public void run () {
        try {
            tempo = (int) (Math.random() * 5000) + 1000;
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     
}
