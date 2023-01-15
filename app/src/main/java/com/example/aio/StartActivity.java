package com.example.aio;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    // 시작시 아이콘 띄우는 액티비티(별 기능 없음. 그냥 첨에 아이콘 띄우고 좀있다 자동으로 로그인 화면으로 이동하게함)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {// 딜레이 이후에 아래 run메소드 실행됨.
            @Override
            public void run() {
                finish();// 현재 액티비티 종료
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);// 로그인 액티비티 실행.
            }
        }, 2000);

    }
}
