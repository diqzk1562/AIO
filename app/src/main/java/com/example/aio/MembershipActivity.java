package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


public class MembershipActivity extends AppCompatActivity {
    // 회원가입 화면 액티비티
    public static Context context_Membership;
    public Activity _MembershipActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_Membership = this;
        _MembershipActivity = MembershipActivity.this;
        setContentView(R.layout.activity_membership);
        EditText id = (EditText)findViewById(R.id.mem_id);
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if(str.equals("")||str.equals(null)){
                    Toast.makeText(MembershipActivity.this,"아이디를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase.getInstance().getReference("account").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                                if(postSnapshot.getKey().toString().equals(str)) {
                                    Toast toast = Toast.makeText(MembershipActivity.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT);
                                    toast.show();
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        });
        EditText pw = (EditText)findViewById(R.id.mem_password);
        EditText pwck = (EditText)findViewById(R.id.mem_pwck);
        EditText name = (EditText)findViewById(R.id.mem_name);
        EditText num = (EditText)findViewById(R.id.mem_num);
        EditText addr = (EditText)findViewById(R.id.mem_address);
        EditText addr2 = (EditText)findViewById(R.id.mem_address2);

        // 회원가입 버튼
        Button mem = (Button)findViewById(R.id.mem_button);
        mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString().trim();
                ID = ID.replace(" ","");
                String passwd = pw.getText().toString().trim();
                String passwdck = pwck.getText().toString().trim();
                String user_name = name.getText().toString().trim();
                String phoneNum = num.getText().toString().trim();
                String address = addr.getText().toString().trim();
                String address2 = addr2.getText().toString().trim();

                if(id.length() == 0 || ID.equals("")||ID.equals(null)){
                    Toast.makeText(MembershipActivity.this,"아이디를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw.length() == 0 || passwd.equals("")||passwd.equals(null)){
                    Toast.makeText(MembershipActivity.this,"비밀번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passwd.equals(passwdck)){
                    Toast.makeText(MembershipActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.length() == 0 || user_name.equals("")||user_name.equals(null)){
                    Toast.makeText(MembershipActivity.this,"이름을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num.length() == 0 || phoneNum.equals("")||phoneNum.equals(null)){
                    Toast.makeText(MembershipActivity.this,"전화번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addr.length() == 0 || address.equals("")||address.equals(null)){
                    Toast.makeText(MembershipActivity.this,"도로명 주소를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addr2.length() == 0 || address2.equals("")||address2.equals(null)){
                    Toast.makeText(MembershipActivity.this,"상세 주소를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Membership mem = new Membership(user_name, ID, passwd, address, address2, phoneNum);
                mem.register();
            }
        });

        // 취소 버튼
        Button cancle = (Button)findViewById(R.id.mem_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {// 뒤로키 누르면 액티비티 끝냄->로그인 화면으로 돌아감.
            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

