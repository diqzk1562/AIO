package com.example.aio;

import java.util.ArrayList;

public abstract class Product_list {
    protected ArrayList<Product> product_list;

    public Product_list(){
        product_list = new ArrayList<Product>();
    };
    public abstract void add_product(Product product);
    public abstract void delete_product(Product product);
    public abstract ArrayList<Product> getProduct_list();
}
