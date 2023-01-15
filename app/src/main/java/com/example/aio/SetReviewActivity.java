package com.example.aio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

// 리뷰 수정 및 삭제화면 액티비티
public class SetReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_review);

        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        String productName = getIntent().getStringExtra("product_name");

        ReviewActivity reviewActivity = (ReviewActivity) ((ReviewActivity) ReviewActivity.context_review)._ReviewActivity;

        EditText editText = (EditText) findViewById(R.id.set_review_content);
        RadioButton good = (RadioButton) findViewById(R.id.radio_good);
        RadioButton bad = (RadioButton) findViewById(R.id.radio_bad);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_good);
        // DB에서 리뷰 가져와서 채우기
        FirebaseDatabase.getInstance().getReference("product").child(productName).child("review").child(user.getID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String goods = task.getResult().child("isGood").getValue().toString();
                    String text = task.getResult().child("writings").getValue().toString();
                    if(goods != null)
                        if(goods.equals("true")){
                            good.setChecked(true);
                        }
                        else
                            bad.setChecked(true);
                    if(text != null)
                        editText.setText(text);
                }
            }
        });
        // 수정 버튼
        Button upload = (Button) findViewById(R.id.set_review_s);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리뷰 화면 액티비티 종료(리뷰 수정시 변경된 리뷰를 출력하기 위해 재실행 하기위함)
                reviewActivity.finish();

                String text = editText.getText().toString();
                if(editText.length() == 0 || text.equals("") || text.equals(null)){
                    Toast.makeText(SetReviewActivity.this, "리뷰를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    return;
                }
                Boolean bool;
                if(good.isChecked()){
                    bool = true;
                }
                else
                    bool = false;
                // 리뷰가 빈 경우
                if(text.equals("")||text==null){
                    Toast.makeText(SetReviewActivity.this,"리뷰를 입력 해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                Review review = new Review(user, text, bool);
                // DB에 리뷰 수정
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                Hashtable<String, String> r = new Hashtable<String, String>();
                r.put("isGood", review.getPros_cons().toString());
                System.out.println(review.getPros_cons().toString());
                r.put("writings", review.getContents());
                myRef.child("product").child(productName).child("review").child(review.getWriter()).setValue(r);
                Toast.makeText(SetReviewActivity.this,"리뷰를 수정했습니다.",Toast.LENGTH_LONG).show();
                // 리뷰 화면 액티비티 재실행
                Intent in = new Intent(SetReviewActivity.this, ReviewActivity.class);
                in.putExtra("user", user);
                in.putExtra("product_name", productName);
                startActivity(in);

                SetReviewActivity.this.finish();
                return;
            }
        });
        // 취소 버튼
        Button cancel = (Button) findViewById(R.id.set_review_c);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
        // 삭제 버튼
        Button delete = (Button) findViewById(R.id.set_review_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리뷰 화면 액티비티 종료(리뷰 수정시 변경된 리뷰를 출력하기 위해 재실행 하기위함)
                reviewActivity.finish();
                // DB에서 리뷰 삭제
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("product").child(productName).child("review").child(user.getID()).removeValue();
                Toast.makeText(SetReviewActivity.this,"리뷰를 삭제했습니다.",Toast.LENGTH_LONG).show();
                // 리뷰 화면 액티비티 재실행
                Intent in = new Intent(SetReviewActivity.this, ReviewActivity.class);
                in.putExtra("user", user);
                in.putExtra("product_name", productName);
                startActivity(in);
                SetReviewActivity.this.finish();
                return;
            }
        });

    }
}
