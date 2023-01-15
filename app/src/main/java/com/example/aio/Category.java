package com.example.aio;

// 카테고리 클래스
public class Category {
    private String category_name;

    public Category(String category_name){
        this.category_name = category_name;
    }
    public Product_inventory category_search(){
        Search search = new Search(category_name);
        return search.search();
    }
}
