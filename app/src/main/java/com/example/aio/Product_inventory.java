package com.example.aio;

import java.util.ArrayList;

public class Product_inventory extends Product_list{
    public Product_inventory(){
        super();
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


    public void sort(int how, String word){
        Sort sort = new Sort();

        if(how == 0){
            sort.price_sort(word);
        }
        else if(how == 1){
            sort.popular_sort(word);
        }
        else
            sort.new_sort(word);
    }

}
