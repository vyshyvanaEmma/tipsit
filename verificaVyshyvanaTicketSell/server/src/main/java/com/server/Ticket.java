package com.server;

public class Ticket {
    private int quantitaG;
    private int quantitaPit;
    private int quantitaPar;

    public Ticket(int quantitaG, int quantitaPit, int quantitaPar) {
        this.quantitaG = quantitaG;
        this.quantitaPit = quantitaPit;
        this.quantitaPar = quantitaPar;
    }


    public int getQuantitaG() {
        return quantitaG;
    }

    public int getQuantitaPit() {
        return quantitaPit;
    }

    public int getQuantitaPar() {
        return quantitaPar;
    }

    public void setQuantitaG(int quantitaG) {
        this.quantitaG = quantitaG;
    }


    public void setQuantitaPit(int quantitaPit) {
        this.quantitaPit = quantitaPit;
    }


    public void setQuantitaPar(int quantitaPar) {
        this.quantitaPar = quantitaPar;
    }



    @Override
    public String toString() {
        return "Gold: " + quantitaG + " Pit: " + quantitaPit + " Parterre: " + quantitaPar;
    }
    
}
