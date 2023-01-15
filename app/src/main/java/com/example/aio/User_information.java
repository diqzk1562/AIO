package com.example.aio;

import java.util.ArrayList;

public class User_information {
    private String uid;
    private int point;
    private String name;
    private Address address;
    private String phone_num;
    private Favorites favorites;
    private Shopping_cart shopping_cart;
    private ArrayList<Search_history> search_history;

    public User_information(UserID userID, int point, String name, String street_name, String detail_address, String phone_num){
        this.uid = userID.getID();
        this.point = point;
        this.name = name;
        this.address = new Address(street_name, detail_address);
        this.phone_num = phone_num;
    }
    public String getUid(){ //추가된거
        return uid;
    }
    public int getPoint(){
        return this.point;
    }
    public String getName() {
        return name;
    }
    public Address getAddress() {
        return address;
    }
    public String getPhone_num() {
        return phone_num;
    }
    public Favorites getFavorites() {
        return favorites;
    }
    public Shopping_cart getShopping_cart() {
        return shopping_cart;
    }
    public ArrayList<Search_history> getSearch_history() {
        return search_history;
    }
}
