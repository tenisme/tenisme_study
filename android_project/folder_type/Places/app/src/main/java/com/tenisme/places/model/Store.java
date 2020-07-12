package com.tenisme.places.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {
    String name;
    String addr;
    double lat;
    double lng;
    ArrayList<Store> stores;

    public Store() {

    }

    public Store(String name, String addr, double lat, double lng) {
        this.name = name;
        this.addr = addr;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

//    public ArrayList<String> getAllStoreName() {
//        ArrayList<String> nameList = new ArrayList<>();
//        for(int i = 0;i < stores.size(); i++) {
//            Store store = stores.get(i);
//            nameList.add(store.getName());
//        }
//        return nameList;
//    }
//
//    public ArrayList<String> getAllStoreAddr() {
//        ArrayList<String> addrList = new ArrayList<>();
//        for(int i = 0;i < stores.size(); i++) {
//            Store store = stores.get(i);
//            addrList.add(store.getAddr());
//        }
//        return addrList;
//    }
//
//    public ArrayList<Double> getAllStoreLat() {
//        ArrayList<Double> latList = new ArrayList<>();
//        for(int i = 0;i < stores.size(); i++) {
//            Store store = stores.get(i);
//            latList.add(store.getLat());
//        }
//        return latList;
//    }
//
//    public ArrayList<Double> getAllStoreLng() {
//        ArrayList<Double> lngList = new ArrayList<>();
//        for(int i = 0;i < stores.size(); i++) {
//            Store store = stores.get(i);
//            lngList.add(store.getLng());
//        }
//        return lngList;
//    }
}
