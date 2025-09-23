package com.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int numCavalli = 10;

        CavalloThread[] cavalli = new CavalloThread[numCavalli];

        for(int i = 0; i < numCavalli; i++){
            cavalli[i] = new CavalloThread("Cavallo " + (i + 1));
            cavalli[i].start();
        }

        for(CavalloThread c : cavalli) {
            try {
                c.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Arrays.sort(cavalli, (a, b) -> a.getTempo() - b.getTempo());

        System.out.println("Classifica finale: ");
        for(int i = 0; i < cavalli.length; i++){
            System.out.println((i + 1) + " posto: " + cavalli[i].getNome() + ", tempo: " + cavalli[i].getTempo() + " ms");
        }
    }
}

