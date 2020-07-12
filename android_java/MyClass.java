package com.tenisme.libjava;

public class MyClass {


    public static void main(String[] args) {

        ObjParam myobj = new ObjParam();
        myobj.str = "Hello World";

        ObjectParamTest test = new ObjectParamTest();
        test.setObj(myobj);
        ObjParam ret_param = test.getObj();
        System.out.println("new str : " + ret_param.str);

    }
}
