package com.example.aio;

import java.util.ArrayList;

public class Favorites extends Product_list{

    public Favorites(UserID userid){
        super();
        product_list=new ArrayList<>();
    }
    @Override
    public void add_product(Product product) {
        this.product_list.add(product);
    }
    @Override
    public void delete_product(Product product) {
        this.product_list.remove(product);
    }
    @Override
    public ArrayList<Product> getProduct_list() {
        return product_list;
    }
}
