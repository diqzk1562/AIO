package com.example.aio.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aio.MainActivity;
import com.example.aio.Search_ListActivity;
import com.example.aio.databinding.FragmentCategoryBinding;

// 카테고리 화면을 담당하는 CategoryFragment. MainActivity에서 동작한다.
public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding; // MainActivity의 바텀네비게이션에 사용하는 바인딩
    MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoryViewModel categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 메인 액티비티의 context와 변수를 사용하기위해 MainActivity 객체 가져옴.
        mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;

        // 상단에 Category 글자 출력
        final TextView textView = binding.textCategory;
        categoryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 카테고리들 버튼 클릭되면 카테고리 이름 넘겨주면서 Search_ListActiviy 실행

        // 의류
        Button cloth = binding.categoryCloth;
        cloth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "clothing";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 식품
        Button food = binding.categoryFood;
        food.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "food";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 전자기기
        Button electronics = binding.categoryElectronics;
        electronics.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "electronics";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 액세서리
        Button accessory = binding.categoryAccessory;
        accessory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "accessory";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 생활용품
        Button houseGoods = binding.categoryHouseGoods;
        houseGoods.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "houseGoods";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 주방용품
        Button kitchenGoods = binding.categoryKitchenGoods;
        kitchenGoods.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "kitchen";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 도서
        Button book = binding.categoryBook;
        book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "book";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 완구
        Button toy = binding.categoryToy;
        toy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "toy";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 스포츠 용품
        Button sports = binding.categorySports;
        sports.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "sports";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 오피스 용품
        Button office = binding.categoryOffice;
        office.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "office";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 반려동물 용품
        Button pet = binding.categoryPet;
        pet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "pet";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });
        // 자동차 용품
        Button car = binding.categoryCar;
        car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = "car";
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                // 다음 엑티비티로 전달.
                in.putExtra("user", mainActivity.user);
                in.putExtra("category",word);
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}