package com.example.aio;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;


public class Sort {

    // 생성자
    public Sort(){

    };
    // 가격 낮은순 정렬 메소드
    public void price_sort(String word){
        Search_ListActivity search_listActivity =
                (Search_ListActivity) ((Search_ListActivity) Search_ListActivity.context_searchList)._Search_ListActivity;

        search_listActivity.adapter = new SearchListViewAdapter();
        Product_inventory product_inventory = new Product_inventory();

        Query query = FirebaseDatabase.getInstance().getReference("product").orderByChild("price");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().contains(word)){
                        String sales = data.child("sell_count").getValue().toString();
                        search_listActivity.adapter.addSale(sales);
                        product_inventory.add_product(new Product(data.getKey(), Integer.parseInt(data.child("price").getValue().toString()), data.child("picture").getValue().toString()));
                    }
                }
                search_listActivity.adapter.setList(product_inventory.getProduct_list());
                search_listActivity.listview.setAdapter(search_listActivity.adapter);
                search_listActivity.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // 판매량 많은 순 정렬 메소드
    public void popular_sort(String word){
        Search_ListActivity search_listActivity =
                (Search_ListActivity) ((Search_ListActivity) Search_ListActivity.context_searchList)._Search_ListActivity;

        search_listActivity.adapter = new SearchListViewAdapter();
        Product_inventory product_inventory = new Product_inventory();

        Query query = FirebaseDatabase.getInstance().getReference("product").orderByChild("sell_count");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().contains(word)) {
                        String sales = data.child("sell_count").getValue().toString();
                        search_listActivity.adapter.addSale(sales);
                        product_inventory.add_product(new Product(data.getKey(), Integer.parseInt(data.child("price").getValue().toString()), data.child("picture").getValue().toString()));
                    }
                }
                Collections.reverse(product_inventory.getProduct_list());
                Collections.reverse(search_listActivity.adapter.sale);
                search_listActivity.adapter.setList(product_inventory.getProduct_list());
                search_listActivity.listview.setAdapter(search_listActivity.adapter);
                search_listActivity.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // 신상품순 정렬 메소드
    public void new_sort(String word){
        Search_ListActivity search_listActivity =
                (Search_ListActivity) ((Search_ListActivity) Search_ListActivity.context_searchList)._Search_ListActivity;

        search_listActivity.adapter = new SearchListViewAdapter();
        Product_inventory product_inventory = new Product_inventory();
        Query query = FirebaseDatabase.getInstance().getReference("product").orderByChild("regdate");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().contains(word)) {
                        String sales = data.child("sell_count").getValue().toString();
                        search_listActivity.adapter.addSale(sales);
                        product_inventory.add_product(new Product(data.getKey(), Integer.parseInt(data.child("price").getValue().toString()), data.child("picture").getValue().toString()));
                    }
                }
                Collections.reverse(product_inventory.getProduct_list());
                Collections.reverse(search_listActivity.adapter.sale);
                search_listActivity.adapter.setList(product_inventory.getProduct_list());
                search_listActivity.listview.setAdapter(search_listActivity.adapter);
                search_listActivity.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
