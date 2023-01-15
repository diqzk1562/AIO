package com.example.aio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Login {
    private String ID;
    private String PW;

    public Login(String id, String pw){
        this.ID = id;
        this.PW = pw;
    }
    public void login(){
        // DB에서 id, pw확인해 로그인.
        LoginActivity loginActivity = (LoginActivity) ((LoginActivity) LoginActivity.context_Login)._LoginActivity;

        FirebaseDatabase.getInstance().getReference("account").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {

                    if (task.getResult().child(ID).exists()) {
                        if (task.getResult().child(ID).child("pw").getValue().toString().equals(PW)) {
                            Intent in = new Intent(loginActivity, MainActivity.class);
                            UserID user = new UserID(ID);
                            in.putExtra("user", user);
                            loginActivity.startActivity(in);
                            return;

                        } else {
                            Toast.makeText(loginActivity, "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
                            e.clear();
                            e.commit();
                            return;
                        }
                    } else {
                        Toast.makeText(loginActivity, "해당 아이디로된 계정이 없습니다.", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor e = ((LoginActivity)LoginActivity.context_Login).auto.edit();
                        e.clear();
                        e.commit();
                        return;
                    }
                }
            }
        });
    }
    public void auto_login(){
        // 자동로그인
        SharedPreferences auto = ((LoginActivity) LoginActivity.context_Login).auto;
        SharedPreferences.Editor autoConnectEdit = auto.edit();
        autoConnectEdit.putString("id", ID);
        autoConnectEdit.putString("pw", PW);
        autoConnectEdit.commit();
    }
}
