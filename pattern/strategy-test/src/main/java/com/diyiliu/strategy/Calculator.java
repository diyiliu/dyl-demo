package com.diyiliu.strategy;

/**
 * Description: Calculator
 * Author: DIYILIU
 * Update: 2020-12-02 15:03
 */
public enum Calculator {

    ADD("+") {
        public int exec(int a, int b) {
            return a + b;
        }
    },
    SUB("-") {
        public int exec(int a, int b) {
            return a - b;
        }
    };

    String type;
    Calculator(String type) {
        this.type = type;
    }

    public static Calculator getCalc(String type) {
        Calculator[] calculators = Calculator.values();
        for (Calculator calc: calculators){
            if (calc.type.equals(type)){
                return calc;
            }
        }

        return null;
    }

    abstract int exec(int a, int b);
}
