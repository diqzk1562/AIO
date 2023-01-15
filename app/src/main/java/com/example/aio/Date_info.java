package com.example.aio;

import java.util.ArrayList;
import java.util.Date;

public class Date_info extends Product_list{
    protected ArrayList<Date> date_list;

    public Date_info(){
        super();
        date_list = new ArrayList<Date>();
    };
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
