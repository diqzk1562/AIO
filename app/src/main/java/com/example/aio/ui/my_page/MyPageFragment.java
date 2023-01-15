package com.example.aio.ui.my_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aio.BuyListActivity;
import com.example.aio.CartActivity;
import com.example.aio.ChargeActivity;
import com.example.aio.CustomerServiceActivity;
import com.example.aio.FeedbackActivity;
import com.example.aio.LastViewActivity;
import com.example.aio.Logout;
import com.example.aio.MainActivity;
import com.example.aio.ProfileActivity;
import com.example.aio.SetProfileActivity;
import com.example.aio.WithdrawalPopActivity;
import com.example.aio.databinding.FragmentMyPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

// 마이페이지 화면을 담당하는 MyPageFragment. MainActivity에서 동작한다.
public class MyPageFragment extends Fragment {

    private FragmentMyPageBinding binding;
    private MainActivity mainActivity;
    private String userId;
    private TextView point;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyPageViewModel myPageViewModel =
                new ViewModelProvider(this).get(MyPageViewModel.class);

        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 메인 액티비티의 context와 변수를 사용하기위해 MainActivity 객체 가져옴.
        mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
        // 메인 액티비티의 mypage변수에 이 플래그먼트를 설정.
        mainActivity.mypage = this;

        userId = mainActivity.user.getID();
        // 마이페이지의 유저아이디 텍스트뷰에 유저아이디를 등록
        TextView uid = binding.userId;
        uid.setText("user : " + userId);

        // 마이페이지의 포인트 텍스트뷰에 유저의 point를 DB에서 가져와 등록
        point = binding.point;
        FirebaseDatabase.getInstance().getReference("account").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String nowPoint = task.getResult().child("point").getValue().toString();
                    point.setText(nowPoint + "P");
                }
            }
        });

        // 포인트 충전 버튼
        Button charge = binding.pointChargeButton;
        charge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), ChargeActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("nPoint", point.getText().toString());
                startActivity(in);// 충전 액티비티 실행.
            }
        });

        // 고객질의 등록 버튼
        Button feedback = binding.cC;
        feedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), FeedbackActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 장바구니 버튼
        Button cart = binding.cart;
        cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), CartActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 회원정보 조회 버튼
        Button checkUser = binding.checkUser;
        checkUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), ProfileActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 회원정보 수정 버튼
        Button changeUser = binding.changeUser;
        changeUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), SetProfileActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 최근 본 상품 버튼
        Button lastWatch = binding.lastWatch;
        lastWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), LastViewActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 구매이력 조회 버튼
        Button buyList = binding.buyList;
        buyList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), BuyListActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 회원탈퇴 버튼
        Button un = binding.un;
        un.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), WithdrawalPopActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 고객센터 버튼
        Button serviceCenter = binding.serviceCenter;
        serviceCenter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), CustomerServiceActivity.class);
                in.putExtra("user", mainActivity.user);// 다음 엑티비티로 전달.
                startActivity(in);
            }
        });

        // 로그아웃 버튼
        Button logout = binding.logout;
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Logout 인스턴스 생성
                Logout logout1 = new Logout();
                logout1.release_auto_login();   // 자동로그인 해제 메소드
                logout1.logout();   // 로그아웃 메소드
            }
        });


        return root;
    }

    // 포인트 충전 또는 상품 구매로 인해 point에 변동이 생기는 경우 플래그먼트의 포인트 출력값을 변경하기위한 메소드
    public void ref(){
        FirebaseDatabase.getInstance().getReference("account").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // 포인트 데이터를 가져와 출력
                    String nowPoint = task.getResult().child("point").getValue().toString();
                    point.setText(nowPoint + "P");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}