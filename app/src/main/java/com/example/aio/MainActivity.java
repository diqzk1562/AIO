package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.aio.ui.my_page.MyPageFragment;
import com.example.aio.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.aio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {//바텀네비게이션의 주가되는 메인엑티비티

    public static Context context_main;
    public Activity _MainActivity;
    public UserID user;
    public MyPageFragment mypage;
    public SearchFragment searchFragment;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;
        _MainActivity = MainActivity.this;


        // 사용자 받기
        user = (UserID) getIntent().getSerializableExtra("user");
        // 바텀 네비게이션 구현하는 부분. 메인액티비티는 계속 실행되고있고 여기에서 프래그먼트만 바뀌면서 다른화면 보여주는것.

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);// activity_main.xml의 바텀 네비게이션 하단 바.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_category, R.id.navigation_search, R.id.navigation_home, R.id.navigation_like, R.id.navigation_my_page)
                .build();//navigation의 mobile_navigation.xml에 넣어둔 프래그먼트 id들
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);// activity_main.xml의 프래그먼트.(이 화면이 바뀐다.)
        NavigationUI.setupWithNavController(binding.navView, navController);// 바텀 네비게이션 하단바랑 프래그먼트함로 컨트롤러세팅
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