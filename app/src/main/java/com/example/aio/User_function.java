package com.example.aio;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class User_function {
    private User_information user_information;

    public User_function(UserID userID, int point, String name, String street_name, String detail_address, String phone_num){//생성자 인자 바뀜
        user_information = new User_information(userID, point, name, street_name, detail_address, phone_num);
    }
    public void charging_point(int point){
        // DB에 해당 유저의 포인트 증가시키기 user_information.getName()으로 이름가져와 db접근
        int now_point = user_information.getPoint() + point;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("account").child(user_information.getUid()).child("point").setValue(Integer.toString(now_point));
        // DB에는 위에걸로 바뀐거고
        // 앱는 포인트 충전 액티비티에서 마이페이지 출력 변경.
    }
    // 회원정보 수정 메소드
    public void user_information_modification(Address address, String phone_num, String pw){
        LoginActivity loginActivity = (LoginActivity) ((LoginActivity) LoginActivity.context_Login)._LoginActivity;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // 회원정보 수정 화면에서 입력하지 않은 값들은 수정하지 않는다.
        if(!address.getStreet_name().equals(""))
            myRef.child("account").child(user_information.getUid()).child("stAddress").setValue(address.getStreet_name());
        if(!address.getDetail_address().equals(""))
            myRef.child("account").child(user_information.getUid()).child("dAddress").setValue(address.getDetail_address());
        if(!phone_num.equals(""))
            myRef.child("account").child(user_information.getUid()).child("phoneNum").setValue(phone_num);
        if(!pw.equals("")) {
            myRef.child("account").child(user_information.getUid()).child("pw").setValue(pw);
            // 비밀번호 바뀌면 자동로그인 되어있으면 자동로그인에 비밀번호도 바꾸기.
            SharedPreferences auto = loginActivity.auto;
            String log_id = auto.getString("id", null);
            String log_pw = auto.getString("pw", null);
            if(log_id != null && log_pw != null){
                SharedPreferences.Editor e = auto.edit();
                e.clear();
                e.commit();
                e.putString("id", user_information.getUid());
                e.putString("pw", pw);
                e.commit();
            }
        }
    }
    // 회원탈퇴 메소드
    public void resigning_membership(UserID userid){
        // db에서 해당 유저의 정보 삭제.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("account").child(user_information.getUid()).removeValue();
    }
}
