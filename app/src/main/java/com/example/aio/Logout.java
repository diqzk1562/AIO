package com.example.aio;

import android.content.SharedPreferences;

public class Logout {
    public Logout(){};
    public void release_auto_login(){
        // 자동 로그인 해제
        SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
        e.clear();
        e.commit();
    }
    public void logout(){
        // 로그아웃 메소드
        MainActivity main = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
        main.finish();
    }
}
