package com.example.aio;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

// 회원 정보 조회 액티비티
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        TextView nameV = (TextView) findViewById(R.id.profile_uname);
        TextView idV = (TextView) findViewById(R.id.profile_uid);
        TextView phoneV = (TextView) findViewById(R.id.profile_unum);
        TextView add1V = (TextView) findViewById(R.id.profile_uaddr1);
        TextView add2V = (TextView) findViewById(R.id.profile_uaddr2);

        // DB에서 회원의 정보를 가지고와 출력
        FirebaseDatabase.getInstance().getReference("account").child(user.getID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String name = task.getResult().child("name").getValue().toString();
                    String point = task.getResult().child("point").getValue().toString();
                    String phone = task.getResult().child("phoneNum").getValue().toString();
                    String stAdd = task.getResult().child("stAddress").getValue().toString();
                    String dAdd = task.getResult().child("dAddress").getValue().toString();
                    User_information ui = new User_information(user, Integer.parseInt(point), name, stAdd, dAdd, phone);
                    nameV.setText(ui.getName());
                    idV.setText(ui.getUid());
                    phoneV.setText(ui.getPhone_num());
                    Address add = ui.getAddress();
                    add1V.setText(add.getStreet_name());
                    add2V.setText("("+add.getDetail_address()+")");
                }
            }
        });
    }
}
