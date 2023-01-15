package com.example.aio;


public class Product_detail {
    private String product_detail_image;
    private String product_detail;

    public Product_detail(String product_dimage, String product_detail){
        this.product_detail_image = product_dimage;
        this.product_detail = product_detail;
    }
    public String getProduct_detail_image() {
        return product_detail_image;
    }
    public String getProduct_detail() {
        return product_detail;
    }
}
