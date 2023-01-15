package com.example.aio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

// 리뷰 등록화면 액티비티
public class UploadReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_review);

        ReviewActivity reviewActivity = (ReviewActivity) ((ReviewActivity) ReviewActivity.context_review)._ReviewActivity;

        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        String productName = getIntent().getStringExtra("product_name");

        EditText editText = (EditText) findViewById(R.id.upload_review_content);
        RadioButton good = (RadioButton) findViewById(R.id.radio_good2);
        RadioButton bad = (RadioButton) findViewById(R.id.radio_bad2);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_good2);

        Button upload = (Button) findViewById(R.id.upload_review_s);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리뷰 화면 액티비티 종료(리뷰 등록시 변경된 리뷰를 출력하기 위해 재실행 하기위함)
                reviewActivity.finish();
                String text = editText.getText().toString();
                if(editText.length() == 0 || text.equals("") || text.equals(null)){
                    Toast.makeText(UploadReviewActivity.this, "리뷰를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    return;
                }
                Boolean bool;
                if(good.isChecked()){
                    bool = true;
                }
                else
                    bool = false;
                if(text.equals("")||text==null){
                    Toast.makeText(UploadReviewActivity.this,"리뷰를 입력 해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                Review review = new Review(user, text, bool);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                Hashtable<String, String> r = new Hashtable<String, String>();
                r.put("isGood", review.getPros_cons().toString());
                r.put("writings", review.getContents());
                myRef.child("product").child(productName).child("review").child(review.getWriter()).setValue(r);
                Toast.makeText(UploadReviewActivity.this,"리뷰를 업로드했습니다.",Toast.LENGTH_LONG).show();
                // 리뷰 화면 액티비티 재실행
                Intent in = new Intent(UploadReviewActivity.this, ReviewActivity.class);
                in.putExtra("user", user);
                in.putExtra("product_name", productName);
                startActivity(in);

                UploadReviewActivity.this.finish();
                return;
            }
        });
        // 취소 버튼
        Button cancel = (Button) findViewById(R.id.upload_review_c);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }
}
