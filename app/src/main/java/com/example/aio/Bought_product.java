package com.example.aio;

import java.util.ArrayList;
import java.util.Date;

// 구매한 상품 클래스
public class Bought_product extends Date_info{

    public Bought_product(UserID userid){
        super();

        product_list = new ArrayList<Product>();
        date_list=new ArrayList<Date>();
    }

    @Override
    public void add_product(Product product) {
        this.product_list.add(product);
    }
    public void add_date(Date date){
        this.date_list.add(date);
    }
    @Override
    public void delete_product(Product product) {
        this.product_list.remove(product);
    }
    public void delete_date(Date date){
        this.date_list.remove(date);
    }
    @Override
    public ArrayList<Product> getProduct_list() {
        return product_list;
    }
    public ArrayList<Date> getDate(){
        return date_list;
    }
}
