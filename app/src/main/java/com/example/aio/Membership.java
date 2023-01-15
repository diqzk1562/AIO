package com.example.aio;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class Membership {
    private String name;
    private String ID;
    private String PW;
    private Address address;
    private String phone_num;

    public Membership(String name, String id, String pw, String street_name, String detail_address, String phone_num){
        this.name = name;
        this.ID = id;
        this.PW = pw;
        this.address = new Address(street_name, detail_address);
        this.phone_num = phone_num;
    }
    public void register(){
        // DB에 회원가입 정보 저장.
        // 생성한 계정을 파이어베이스에 저장하여 회원가입을 하는 메소드.

        MembershipActivity membershipActivity = (MembershipActivity) ((MembershipActivity) MembershipActivity.context_Membership)._MembershipActivity;
        LoginActivity loginActivity = (LoginActivity) ((LoginActivity) LoginActivity.context_Login)._LoginActivity;
        if(ID.contains("0-답변")){
            Toast.makeText(membershipActivity,"0-답변이 포함된 아이디는 사용할 수 업습니다.",Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference("account").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                        if(postSnapshot.getKey().toString().equals(ID)) {
                            Toast.makeText(membershipActivity,"이미 존재하는 아이디입니다.",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    Hashtable<String, String> profile = new Hashtable<String, String>();
                    profile.put("pw", PW);
                    profile.put("name", name);
                    profile.put("point", "0");
                    profile.put("phoneNum", phone_num);
                    profile.put("stAddress", address.getStreet_name());
                    profile.put("dAddress", address.getDetail_address());

                    myRef.child("account").child(ID).setValue(profile);
                    membershipActivity.finish();
                    Toast.makeText(loginActivity,"회원가입에 성공했습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
