package com.example.aio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

// 최근 본 상품 화면 액티비티
public class LastViewActivity extends AppCompatActivity {
    private Product p;
    private Check_viewed_product check_viewed_product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_list);
        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");

        ListView listview;
        listview = (ListView) findViewById(R.id.last_list_listview);   // 화면의 listview객체
        SearchListViewAdapter adapter;
        adapter = new SearchListViewAdapter();

        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
        // DB에서 최근본상품들을 가져와 check_viewed_product에 추가.
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> deleteList = new ArrayList<String>();
                check_viewed_product=new Check_viewed_product(user);
                adapter.sale = new ArrayList<String>();
                adapter.items = new ArrayList<Product>();
                for(DataSnapshot data : task.getResult().child("account").child(user.getID()).child("ViewedProduct").getChildren()){
                    String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                    String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                    String sales = task.getResult().child("product").child(data.getValue().toString()).child("sell_count").getValue().toString();
                    p = new Product(data.getValue().toString(),Integer.parseInt(price),image);
                    check_viewed_product.add_product(p);
                    // 제품의 판매량 어댑터에 추가.
                    adapter.addSale(sales);
                }
                ArrayList<Product> list = check_viewed_product.getProduct_list();
                // DB에 날짜 오름차순으로 정렬되어 있으므로 reverse로 최근 순으로 변경
                Collections.reverse(list);
                Collections.reverse(adapter.sale);
                // 20개 까지만 출력. 나머지는 deleteList에 추가.
                while (adapter.sale.size()>20){
                    adapter.sale.remove(20);
                }
                for(Product p: list){
                    if(adapter.items.size()>19){
                        deleteList.add(p.getProduct_name());
                    }
                    else{
                        adapter.addItem(p);
                    }
                }
                // 20개가 넘어 deleteList에 추가된 것은 DB에서 삭제.(이를 통해 DB에 20개씩 저장)
                for(DataSnapshot data: task.getResult().child("account").child(user.getID()).child("ViewedProduct").getChildren()){
                    for(String pname : deleteList){
                        if(data.getKey().contains(pname)){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference Ref = database.getReference();
                            Ref.child("account").child(user.getID()).child("ViewedProduct").child(data.getKey()).removeValue();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Product item = (Product) adapter.getItem(position);
                Intent in = new Intent(getApplicationContext(), ProductPageActivity.class);
                in.putExtra("user", ((MainActivity) MainActivity.context_main).user);   // 다음 엑티비티로 전달.
                in.putExtra("item", item.getProduct_name());
                startActivity(in);
            }
        });

    }
}
