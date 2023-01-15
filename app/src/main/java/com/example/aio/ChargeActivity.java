package com.example.aio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aio.ui.my_page.MyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

// 포인트 충전 액티비티
public class ChargeActivity extends AppCompatActivity {
    public UserID user;
    public int now_point = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        user = (UserID) getIntent().getSerializableExtra("user");
        String nPoint = getIntent().getStringExtra("nPoint");
        nPoint = nPoint.substring(0,nPoint.length()-1);
        now_point = Integer.parseInt(nPoint);

        EditText val = (EditText)findViewById(R.id.val);
        EditText point = (EditText)findViewById(R.id.want_point);

        Spinner bankSpinner = (Spinner)findViewById(R.id.bank);
        ArrayAdapter bankAdapter = ArrayAdapter.createFromResource(this,
                R.array.bank, android.R.layout.simple_spinner_item);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(bankAdapter);

        // 충전 버튼
        Button charge = (Button)findViewById(R.id.charge);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chargPoint = point.getText().toString().trim();
                if(point.length() == 0 || chargPoint.equals("") || chargPoint.equals(null)){
                    Toast.makeText(ChargeActivity.this, "금액을 입력해 주세요.", Toast.LENGTH_LONG).show();
                    point.setText("");
                    return;
                }
                // DB에서 사용자의 정보를 가져와 User_function 클래스 인스턴스 생성하여 충전 메소드 사용.
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
                            uf.charging_point(Integer.parseInt(chargPoint));
                            MainActivity mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
                            MyPageFragment mypage = mainActivity.mypage;
                            // 마이페이지 포인트 출력 변경 메소드
                            mypage.ref();
                            ChargeActivity.this.finish();
                        }
                    }
                });
            }
        });

        // 취소 버튼
        Button cancel = (Button)findViewById(R.id.charge_c);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
