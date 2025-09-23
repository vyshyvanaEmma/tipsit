package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int i = 50;

        System.out.println("Hello world!");
        //System.out.println(Thread.currentThread().getName());

        //Scanner scaner = new Scanner(System.in);

        MyThread t1 = new MyThread(i);
        MyThread t2 = new MyThread(i);

        t1.start();
        //scaner.nextLine();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("main finito");
    }
}