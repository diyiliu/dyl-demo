package com.diyiliu.strategy;

/**
 * Description: Main
 * Author: DIYILIU
 * Update: 2020-12-02 15:10
 */
public class Main {

    public static void main(String[] args) {
        int a = 3;
        int b = 4;

        System.out.println(Calculator.getCalc("+").exec(a, b));

        System.out.println(Calculator.getCalc("-").exec(a, b));
    }
}
