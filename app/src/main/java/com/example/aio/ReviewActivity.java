package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    public static Context context_review;
    public Activity _ReviewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        context_review = this;
        _ReviewActivity = ReviewActivity.this;

        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        String productName = getIntent().getStringExtra("product_name");

        TextView my_review = (TextView) findViewById(R.id.my_review);
        Button booll = (Button) findViewById(R.id.review_bool);

        Button set = (Button)findViewById(R.id.set_review);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAct = my_review.getText().toString();

                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    int count=0;
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for(DataSnapshot data : task.getResult().child("account").child(user.getID()).child("boughtList").getChildren()){
                            String name = data.getValue().toString();
                            if(name.equals(productName)){
                                count++;
                                break;
                            }
                        }
                        if(count==0){     // 제품 안샀을때
                            Toast.makeText(ReviewActivity.this, "제품을 구매해주세요.", Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            Intent in;
                            if(!setAct.equals("")){
                                in = new Intent(ReviewActivity.this, SetReviewActivity.class);
                            }
                            else{
                                in = new Intent(ReviewActivity.this, UploadReviewActivity.class);
                            }
                            in.putExtra("user", user);
                            in.putExtra("product_name", productName);
                            startActivity(in);
                        }
                    }
                });
            }
        });

        ListView listview;
        listview = (ListView) findViewById(R.id.review_list);   // 화면의 listview객체
        ReviewListViewAdapter adapter;
        adapter = new ReviewListViewAdapter(this);
        listview.setAdapter(adapter);
        // DB에서 리뷰 데이터 가져오기
        FirebaseDatabase.getInstance().getReference().child("product").child(productName).child("review").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ReviewList reviewList = new ReviewList(user);
                for(DataSnapshot data : task.getResult().getChildren()){
                    String writer = data.getKey();
                    String text = data.child("writings").getValue().toString();
                    String good = data.child("isGood").getValue().toString();
                    Boolean bool;
                    if(good.equals("true"))
                        bool = true;
                    else
                        bool = false;
                    if(data.getKey().equals(user.getID())){
                        reviewList.setMy_review(text, bool);
                        // 평가에 따라 표시 달라지도록
                        if(bool) {
                            Drawable d = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24);
                            booll.setBackground(d);
                            booll.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.orange));
                        }
                        else{
                            Drawable d = getBaseContext().getResources().getDrawable(R.drawable.ic_baseline_thumb_down_24);
                            booll.setBackground(d);
                            booll.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.black));
                        }

                    }
                    else{
                        Review review = new Review(new UserID(writer), text, bool);
                        reviewList.addReview(review);
                    }
                }
                ArrayList<Review> list = reviewList.getReview_list();
                for(Review r : list){
                    adapter.addItem(r);
                }
                if(reviewList.getMy_review()!=null){
                    my_review.setText(reviewList.getMy_review().getContents());
                    set.setText("modify\nreview");
                }
                else{
                    my_review.setText("");
                    set.setText("upload\nreview");
                    booll.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.white));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
