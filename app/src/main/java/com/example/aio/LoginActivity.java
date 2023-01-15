package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    // 로그인 화면 액티비티

    public static Context context_Login;
    public Activity _LoginActivity;
    public SharedPreferences auto;
    CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context_Login = this;
        _LoginActivity = LoginActivity.this;

        auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        String log_id = auto.getString("id", null);
        String log_pw = auto.getString("pw", null);

        EditText id = (EditText)findViewById(R.id.log_id);
        EditText pw = (EditText)findViewById(R.id.log_password);
        autoLogin = (CheckBox)findViewById(R.id.log_checkbox);

        // 로그인 버튼
        Button login = (Button)findViewById(R.id.log_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString();
                String passwd = pw.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Insert ID", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Insert password", Toast.LENGTH_LONG).show();
                    return;
                }

                Login login = new Login(ID, passwd);

                if(autoLogin.isChecked()) {
                    SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
                    e.clear();
                    e.commit();
                    login.auto_login();
                }

                login.login();
            }
        });

        // 회원가입 버튼
        Button membership = (Button)findViewById(R.id.log_mem);
        membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, MembershipActivity.class);
                LoginActivity.this.startActivity(in);
            }
        });

        // 자동로그인 체크박스 인스턴스 만들어서 autoLogin에 넣어야함.
        if(log_id != null && log_pw != null){
            id.setText(log_id);
            pw.setText(log_pw);
            autoLogin.setChecked(true);
            login.performClick();
        }
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

}
