package com.example.aio;

// 회사(고객센터) 정보 클래스
public class Company_information {

    private String company_name;
    private Address company_address;
    private String company_num;

    public Company_information(String company_name, Address company_address, String company_num){
        this.company_name = company_name;
        this. company_address = company_address;
        this.company_num = company_num;

    }

    public String getCompany_name() {
        return company_name;
    }

    public Address getCompany_address(){
        return company_address;
    }

    public String getCompany_num() {
        return company_num;
    }
}