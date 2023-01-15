package com.example.aio;

import androidx.annotation.NonNull;

import com.example.aio.ui.my_page.MyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Shopping_cart extends Product_list {
    private int overall_price;

    public Shopping_cart(UserID userid){
        super();

        product_list=new ArrayList<>();
        this.overall_price = 0;
    }
    @Override
    public void add_product(Product product) {
        this.product_list.add(product);
        this.overall_price+=product.getProduct_price();         // 총 구매가격 계산
    }
    @Override
    public void delete_product(Product product) {
        this.product_list.remove(product);
        this.overall_price-=product.getProduct_price();         // 총 구매가격에서 제외
    }
    @Override
    public ArrayList<Product> getProduct_list() {
        return product_list;
    }
    public int getOverall_price(){
        return this.overall_price;
    }
    public void shopping_cart_purchase(UserID userid){        // 구매실행메소드

        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int userPrice=Integer.parseInt(task.getResult().child("account").child(userid.getID()).child("point").getValue().toString());
                if(userPrice < overall_price){
                    // 잔액 부족 경우(구매 액티비티에서 한번 거르지만 혹시모르니 존재)
                }else{
                    // 구매
                    userPrice-=overall_price;
                    all_delete(userid);       //구매했으니까 장바구니 비워야겠지?
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    myRef.child("account").child(userid.getID()).child("point").setValue(Integer.toString(userPrice));
                    MainActivity mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
                    MyPageFragment mypage = mainActivity.mypage;
                    mypage.ref();
                }
            }
        });

    }
    public void all_delete(UserID userid){
        //db에서 해당 유저의 장바구니 다지우고
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("account").child(userid.getID()).child("shoppingCart")
                .setValue(null);            //기존에 있던값 다사라지네??

        this.product_list.removeAll(product_list);//이거 되는지 모르겠다.

        this.overall_price = 0;
    }

}
