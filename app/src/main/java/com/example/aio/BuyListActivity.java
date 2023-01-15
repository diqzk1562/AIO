package com.example.aio;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

// 구매 리스트 액티비티
public class BuyListActivity extends AppCompatActivity {

    private Bought_product bought_product;
    private Product p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list);
        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");

        ListView listview;
        listview = (ListView) findViewById(R.id.buy_list_view);   // 화면의 listview객체
        BuyListViewAdapter adapter;
        adapter = new BuyListViewAdapter(this);

        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                bought_product = new Bought_product(user);
                // DB에서 구매 목록 데이터를 가져와 bought_product에 추가.
                for(DataSnapshot data: task.getResult().child("account").child(user.getID()).child("boughtList").getChildren()){
                    String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                    String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                    p = new Product(data.getValue().toString(),Integer.parseInt(price),image);

                    String date[] = data.getKey().toString().split("@");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date to = null;
                    try {
                        to = sdf.parse(date[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bought_product.add_product(p);
                    bought_product.add_date(to);
                }
                ArrayList<Product> product_list = bought_product.getProduct_list();
                ArrayList<Date> date_list = bought_product.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(int i = product_list.size()-1; i >= 0; i--){
                    String day = sdf.format(date_list.get(i));
                    adapter.addItem(product_list.get(i), day);
                }

                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);

            }
        });
    }
}
