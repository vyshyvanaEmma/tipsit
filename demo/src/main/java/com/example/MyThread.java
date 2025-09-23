package com.example;

public class MyThread extends Thread{

    int n;

    public MyThread (int i) {
        n = i;
    }

    public void run(){
        for(int i = 0; i < n; i++){
            System.out.println(Thread.currentThread().getName() + "" + i);
        }
    }
}
