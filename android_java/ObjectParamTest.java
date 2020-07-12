package com.tenisme.libjava;

public class ObjectParamTest {

    ObjParam obj;

    void setObj(ObjParam new_obj){
        obj = new_obj;
        obj.print();
    }

    ObjParam getObj(){
        obj.str = "return ObjParam";
        return obj;
    }
}
