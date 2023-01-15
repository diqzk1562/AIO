package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// 장바구니 액티비티
public class CartActivity extends AppCompatActivity {
    private Product p;
    private Shopping_cart shopping_cart;
    public static Context context_cart;
    public Activity _CartActivity;
    String userPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context_cart = this;
        _CartActivity = CartActivity.this;

        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");

        ListView listview;
        listview = (ListView) findViewById(R.id.cart_list_view);   // 화면의 listview객체
        CartListViewAdapter adapter;
        adapter = new CartListViewAdapter(this);

        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userPoint=task.getResult().child("account").child(user.getID()).child("point").getValue().toString();
                shopping_cart=new Shopping_cart(user);
                // DB에서 사용자의 장바구니 데이터 가지고와 추가.
                for(DataSnapshot data: task.getResult().child("account").child(user.getID()).child("shoppingCart").getChildren()){
                    String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                    String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                    p = new Product(data.getValue().toString(),Integer.parseInt(price),image);
                    shopping_cart.add_product(p);
                }
                ArrayList<Product> list = shopping_cart.getProduct_list();    // 쇼핑카트객체에서 가져오기
                for(Product p: list){
                    adapter.addItem(p);
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
                in.putExtra("user", ((MainActivity) MainActivity.context_main).user);// 다음 엑티비티로 전달.
                in.putExtra("item", item.getProduct_name());
                startActivity(in);
            }
        });


        Button buy = (Button)findViewById(R.id.buy_b);
        buy.setOnClickListener(new View.OnClickListener() {          // 결제버튼 눌렀을때
            @Override
            public void onClick(View view) {
                // 장바구니가 비어있을 경우
                if(adapter.isEmpty()){
                    Toast.makeText(CartActivity.this, "장바구니가 비었습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                // 그 외에는 구매 액티비티 실행
                Intent in = new Intent(CartActivity.this, BuyActivity.class);
                in.putExtra("user", user);
                in.putExtra("userPoint", userPoint);
                startActivity(in);
            }
        });

    }
}
