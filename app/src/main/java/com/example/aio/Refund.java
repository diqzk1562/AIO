package com.example.aio;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class Refund extends Writings{
    private String product;
    public Refund(UserID userid, String contents, String product){   // contents는 사유, product는 제품이름
        super(userid.getID(), contents);
        this.product = product;
    }
    public void request_refund(String num){
        // DB에 환불 데이터 저장
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                Hashtable<String, String> profile = new Hashtable<String, String>();
                profile.put("사유",contents);
                profile.put("신청자전화번호",num);
                profile.put("처리여부","false");

                myRef.child("refund").child(writer+"@"+product).setValue(profile);
            }
        });
    }
}
