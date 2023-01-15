package com.example.aio;

// 주소를 가지는 클래스
public class Address {
    private String street_name; // 도로명 주소
    private String detail_address;  // 상세 주소

    public Address(String street_name, String detail_address){
        this.street_name = street_name;
        this.detail_address = detail_address;
    }
    public String getStreet_name(){
        return this.street_name;
    }
    public String getDetail_address(){
        return this.detail_address;
    }
}
