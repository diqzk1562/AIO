package com.example.aio.ui.like;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aio.Favorites;
import com.example.aio.LikeListViewAdapter;
import com.example.aio.MainActivity;
import com.example.aio.Product;
import com.example.aio.UserID;
import com.example.aio.databinding.FragmentLikeBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

// 즐겨찾기 화면을 담당하는 LikeFragment. MainActivity에서 동작한다.
public class LikeFragment extends Fragment {

    private FragmentLikeBinding binding;
    private Product p;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LikeViewModel likeViewModel =
                new ViewModelProvider(this).get(LikeViewModel.class);

        binding = FragmentLikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLike;
        likeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 즐겨찾기 리스트를 위한 리스트뷰
        ListView listview;
        listview = binding.likeListView;   // 화면의 listview객체
        LikeListViewAdapter adapter;    // 즐겨찾기 리스트 어댑터
        adapter = new LikeListViewAdapter(getContext());

        // DB에서 해당 유저의 즐겨찾기 목록을 가져온다.
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserID user=((MainActivity) MainActivity.context_main).user;
                Favorites favorites=new Favorites(((MainActivity) MainActivity.context_main).user);

                // 즐겨찾기 목록에 존재하는 제품들의 정보를 DB에서 가지고와 Product클래스의 인스턴스를 생성하여 Favorites 인스턴스의 리스트에 추가.
                for(DataSnapshot data : task.getResult().child("account").child(user.getID()).child("Favorites").getChildren()){
                    String price = task.getResult().child("product").child(data.getValue().toString()).child("price").getValue().toString();
                    String image = task.getResult().child("product").child(data.getValue().toString()).child("picture").getValue().toString();
                    String sales = task.getResult().child("product").child(data.getValue().toString()).child("sell_count").getValue().toString();
                    p = new Product(data.getValue().toString(),Integer.parseInt(price),image);

                    favorites.add_product(p);
                    // 어댑터에 판매량 항목 추가
                    adapter.addSale(sales);
                }
                // Favorites 인스턴스에서 즐겨찾기 등록 제품 리스트를 가져와 어댑터에 추가.
                for(Product p: favorites.getProduct_list()){
                    adapter.addItem(p);
                }
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
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
