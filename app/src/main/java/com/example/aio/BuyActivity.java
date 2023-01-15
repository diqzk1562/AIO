package com.example.aio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

// 구매 화면 액티비티
public class BuyActivity extends AppCompatActivity {
    private Button btn_buy;
    private Button btn_cancle;
    private Bought_product bought_product;
    private SimpleDateFormat sdf;
    private TextView stAddress;
    private TextView dAddress;
    private TextView phoneNum;
    private TextView receiver;
    private TextView buyCost;
    private TextView userPoint;
    private Shopping_cart shopping_cart;
    int stock=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        String nowPoint = getIntent().getStringExtra("userPoint");
        stAddress=findViewById(R.id.buy_addr1);
        dAddress=findViewById(R.id.buy_addr2);
        phoneNum=findViewById(R.id.buy_num);
        receiver=findViewById(R.id.buy_name);
        buyCost = findViewById(R.id.buy_cost);
        userPoint=findViewById(R.id.buy_point);

        userPoint.setText("현재 남은 잔액(포인트): "+nowPoint);

        btn_buy=findViewById(R.id.buy_button);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // DB에서 사용자의 정보를 가져와 주소, 전화번호, 수신자 이름을 출력.
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                shopping_cart = new Shopping_cart(user);
                Product p;
                String stadd = task.getResult().child("account").child(user.getID()).child("stAddress").getValue().toString();
                String dadd = task.getResult().child("account").child(user.getID()).child("dAddress").getValue().toString();
                String phone = task.getResult().child("account").child(user.getID()).child("phoneNum").getValue().toString();
                String name = task.getResult().child("account").child(user.getID()).child("name").getValue().toString();

                stAddress.setText(stadd);
                dAddress.setText(dadd);
                phoneNum.setText(phone);
                receiver.setText(name);

                // DB에서 사용자의 장바구니 항목에서 제품들을 가져와 Shopping_cart의 인스턴스에 추가.
                for(DataSnapshot data: task.getResult().child("account").child(user.getID()).child("shoppingCart").getChildren()){
                    String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                    String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                    p = new Product(data.getValue().toString(),Integer.parseInt(price),image);
                    shopping_cart.add_product(p);
                }
                // 장바구니의 전체 가격 출력
                buyCost.setText(Integer.toString(shopping_cart.getOverall_price()));
            }
        });
        // 구매버튼
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자의 포인트와 장바구니 상목들 전체의 가격을 비교하여 포인트가 부족시 포인트 부족 메시지.
                String stAddr = stAddress.getText().toString().trim();
                if(stAddress.length() == 0 || stAddr.equals("") || stAddr.equals(null)){
                    Toast.makeText(BuyActivity.this, "도로명 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                String dAddr = dAddress.getText().toString().trim();
                if(dAddress.length() == 0 || dAddr.equals("") || dAddr.equals(null)){
                    Toast.makeText(BuyActivity.this, "상세 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                String phone = phoneNum.getText().toString().trim();
                if(phoneNum.length() == 0 || phone.equals("") || phone.equals(null)){
                    Toast.makeText(BuyActivity.this, "전화번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                String rname = receiver.getText().toString().trim();
                if(receiver.length() == 0 || rname.equals("") || rname.equals(null)){
                    Toast.makeText(BuyActivity.this, "수진자를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(Integer.parseInt(nowPoint)<Integer.parseInt(buyCost.getText().toString())){
                    Toast.makeText(BuyActivity.this, "포인트가 부족합니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                bought_product = new Bought_product(user);

                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        shopping_cart = new Shopping_cart(user);
                        Product p;
                        // 장바구니 항목의 제품들 정보를 shopping_cart에 추가.
                        for(DataSnapshot data: task.getResult().child("account").child(user.getID()).child("shoppingCart").getChildren()){

                            String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                            String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                            stock = Integer.parseInt(task.getResult().child("product").child(data.getValue().toString()).child("stock").getValue().toString());

                            // 제품의 재고가 없는 경우
                            if(stock == 0){
                                Toast.makeText(BuyActivity.this, "장바구니에 품절제품이 존재합니다.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            p = new Product(data.getValue().toString(),Integer.parseInt(price),image);
                            shopping_cart.add_product(p);
                        }
                        // 장바구니가 비어있는 경우
                        if(shopping_cart.getProduct_list().isEmpty()){
                            Toast.makeText(BuyActivity.this, "결제하실 제품이 없습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // bought_product에 추가.
                        for(Product k : shopping_cart.getProduct_list()){
                            bought_product.add_product(k);
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();

                        // DB에 구매하는 제품들 재고, 판매량 변경
                        for(Product k: bought_product.getProduct_list()){
                            Date date = new Date();
                            String sendTime = sdf.format(date);

                            int sellcount=Integer.parseInt(task.getResult().child("product").child(k.getProduct_name()).child("sell_count")
                                    .getValue().toString());
                            stock=Integer.parseInt(task.getResult().child("product").child(k.getProduct_name()).child("stock")
                                    .getValue().toString());
                            sellcount+=1;
                            stock-=1;

                            myRef.child("account").child(user.getID()).child("boughtList").child(sendTime+"@"+k.getProduct_name()).setValue(k.getProduct_name());
                            myRef.child("product").child(k.getProduct_name()).child("sell_count").setValue(sellcount);
                            myRef.child("product").child(k.getProduct_name()).child("stock").setValue(stock);

                            Hashtable<String, String> profile = new Hashtable<String, String>();
                            profile.put("stAddress",stAddr);
                            profile.put("dAddress",dAddr);
                            profile.put("phoneNum",phone);
                            profile.put("receiver",rname);
                            profile.put("productName",k.getProduct_name());
                            // DB에 구매 신청
                            myRef.child("PurchaseRequest").child(sendTime+"@"+k.getProduct_name()).setValue(profile);

                        }

                        shopping_cart.shopping_cart_purchase(user);      // 구매메소드 실행(이때 장바구니도 비워짐)
                        bought_product.getProduct_list().clear();
                        bought_product.getDate().clear();
                        // 구매완료 출력
                        Toast.makeText(BuyActivity.this, "구매가 완료되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                // 구매가 완료되면 장바구니 액티비티와 현재 액티비티를 종료시켜 메인액티비티의 마이페이지가 나오도록 한다.
                CartActivity cartActivity = (CartActivity) ((CartActivity) CartActivity.context_cart)._CartActivity;
                cartActivity.finish();
                finish();
            }
        });
        btn_cancle=findViewById(R.id.buy_cancle);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyActivity.super.onBackPressed();
            }
        });
    }
}
