package com.tenisme.libjava;

public class Student {
    String name;
    int kor;
    int eng;
    int math;
    int sum;
    double average;

    void sum(){
        sum = kor + eng + math;
    }

    void avg(){
        average = (double)(kor + eng + math) /3;
    }

    void print(){
        System.out.println(name + ", " + sum + ", " + average);
    }

    int mul(int a){
        int ret = a * kor;
        return ret;
    }

    void grade(int x){
        String a = "A Grade";
        String f = "F Grade";
        if(x > average){
            System.out.println(a);
        }else{
            System.out.println(f);
        }
    }

    String grade1(int y){
        if(y > average){
            return "A Grade";
        }else{
            return "F Grade";
        }
    }


}
