package com.example.aio;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

// 회원탈퇴 확인 팝업 액티비티
public class WithdrawalPopActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback_pop);

        UserID user = (UserID) getIntent().getSerializableExtra("user");

        TextView text1 = (TextView) findViewById(R.id.pop_text1);
        text1.setText("회원 탈퇴");

        TextView text2 = (TextView) findViewById(R.id.pop_text2);
        text2.setText("AIO에서 탈퇴 하시겠습니까?");

        Button delete = (Button)findViewById(R.id.pop_yes);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("account").child(user.getID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (!task.isSuccessful()) {

                        } else {
                            String name = task.getResult().child("name").getValue().toString();
                            String point = task.getResult().child("point").getValue().toString();
                            String phone = task.getResult().child("phoneNum").getValue().toString();
                            String stAdd = task.getResult().child("stAddress").getValue().toString();
                            String dAdd = task.getResult().child("dAddress").getValue().toString();
                            User_function uf = new User_function(user, Integer.parseInt(point), name, stAdd, dAdd, phone);
                            uf.resigning_membership(user);

                            Logout logout1 = new Logout();
                            logout1.release_auto_login();
                            logout1.logout();
                            finish();
                        }
                    }
                });
                finish();
            }
        });
        Button cancel = (Button)findViewById(R.id.pop_no);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void mOnClose(View V){
        finish();
    }
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}
