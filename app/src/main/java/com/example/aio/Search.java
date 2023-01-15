package com.example.aio;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Search {
    private String searches;

    public Search(String searches){
        this.searches = searches;
    }
    public Product_inventory search(){
        Search_ListActivity s = (Search_ListActivity) ((Search_ListActivity) Search_ListActivity.context_searchList)._Search_ListActivity;
        Product_inventory product_inventory = new Product_inventory();

        FirebaseDatabase.getInstance().getReference().child("product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getKey().toString().contains(searches)){
                        String name = data.getKey().toString();
                        String price = data.child("price").getValue().toString();
                        String img = data.child("picture").getValue().toString();
                        String sales = data.child("sell_count").getValue().toString();
                        s.adapter.addSale(sales);
                        product_inventory.add_product(new Product(name, Integer.parseInt(price), img));
                    }
                }
                s.adapter.setList(product_inventory.getProduct_list());
                s.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return product_inventory;
    }
}
