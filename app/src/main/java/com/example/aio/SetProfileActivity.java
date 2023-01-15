package com.example.aio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aio.ui.my_page.MyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        // 사용자 받기
        UserID user = (UserID) getIntent().getSerializableExtra("user");
        EditText PW = (EditText) findViewById(R.id.set_password);
        EditText PWck = (EditText) findViewById(R.id.set_pwC);
        EditText num = (EditText) findViewById(R.id.set_num);
        EditText stadd = (EditText) findViewById(R.id.set_addr);
        EditText dadd = (EditText) findViewById(R.id.set_addr2);

        // DB에서 기존의 회원 정보를 가져와 출력
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
                    num.setText(ui.getPhone_num());
                    Address add = ui.getAddress();
                    stadd.setText(add.getStreet_name());
                    dadd.setText(add.getDetail_address());
                }
            }
        });
        // 정보 변경 버튼
        Button setButton = (Button) findViewById(R.id.set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력이 null인 경우 ""로 변경
                String passwd = PW.getText().toString().trim();
                if(PW.length() == 0 || passwd.equals("") || passwd.equals(null))
                    passwd = "";
                String passwdck = PWck.getText().toString().trim();
                if(PWck.length() == 0 || passwdck.equals("") || passwdck.equals(null))
                    passwdck = "";
                String phoneNum = num.getText().toString().trim();
                if(num.length() == 0 || phoneNum.equals("") || phoneNum.equals(null))
                    phoneNum = "";
                String address = stadd.getText().toString().trim();
                if(stadd.length() == 0 || address.equals("") || address.equals(null))
                    address = "";
                String address2 = dadd.getText().toString().trim();
                if(dadd.length() == 0 || address2.equals("") || address2.equals(null))
                    address2 = "";

                // 변경할 비밀번호와 확인이 다른 경우
                if(!passwd.equals(passwdck)){
                    Toast.makeText(SetProfileActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                    return;
                }

                String finalAddress = address;
                String finalAddress1 = address2;
                String finalPhoneNum = phoneNum;
                String finalPasswd = passwd;
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
                            User_function uf = new User_function(user, Integer.parseInt(point), name, stAdd, dAdd, phone);
                            // 회원정보 수정 메소드
                            uf.user_information_modification(new Address(finalAddress, finalAddress1), finalPhoneNum, finalPasswd);
                        }
                    }
                });

                SetProfileActivity.this.finish();
            }
        });
        Button cancel = (Button) findViewById(R.id.set_cancle);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

    }
}
