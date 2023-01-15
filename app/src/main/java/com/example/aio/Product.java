package com.example.aio;


public class Product {
    private String product_name;
    private int product_price;
    private Product_detail product_detail;
    private String product_image;

    public Product(String product_name, int product_price, String product_image) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image=product_image;
    }

    public Product(String product_name, int product_price, String product_image,String product_dimage, String product_di) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_detail = new Product_detail(product_dimage, product_di);
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return this.product_name;
    }
    public int getProduct_price() {
        return this.product_price;
    }
    public Product_detail getProduct_detail(){
        return this.product_detail;
    }
    public String getProduct_image() {
        return this.product_image;
    }
}
