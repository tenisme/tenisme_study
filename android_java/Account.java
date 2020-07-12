package com.tenisme.libjava;

public class Account {

    int money;

    void dep(int a) {
        money = money + a;
    }

    String wit(int a) {
        if (money - a < 0) {
            return "Not Enough Money";
        } else {
            money = money - a;
            return "O.K";
        }
    }

    void print(){
        System.out.println("My Current money = " + money);
    }
}
