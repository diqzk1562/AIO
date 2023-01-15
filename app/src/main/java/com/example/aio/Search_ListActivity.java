package com.example.aio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aio.ui.search.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

// 검색 결과화면 액티비티
public class Search_ListActivity extends AppCompatActivity {
    int sortNum = 0;
    public static Context context_searchList;
    public Activity _Search_ListActivity;
    public SearchListViewAdapter adapter;
    public ListView listview;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("account");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        // 사용자 받기
        // 검색버튼 누를때 검색하려한 문자열 받기
        Intent intent = getIntent();
        UserID user = (UserID) intent.getSerializableExtra("user");
        String category = intent.getStringExtra("category");
        String word = intent.getStringExtra("search_word");

        // 카테고리로 검색한 경우 카테고리를 검색어로
        if(category != null){
            word = category;
        }

        context_searchList = this;
        _Search_ListActivity = Search_ListActivity.this;

        Search search = new Search(word);

        TextView textView = (TextView) findViewById(R.id.search_list_result);
        textView.setText(word+"의 검색결과");

        // 검색 기록 추가
        String finalWord = word;
        databaseReference.child(user.getID()).child("Searchhistory").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot data: task.getResult().getChildren()){     // 해당 제품이 최근검색에 등록되었는지 확인
                    String searchWord[] = data.getKey().split("@");
                    // 등록되어 있으면 제거
                    if(finalWord.equals(searchWord[1])){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference Ref = database.getReference();
                        Ref.child("account").child(user.getID()).child("Searchhistory").child(data.getKey()).removeValue();
                        break;
                    }
                }
                // DB에 검색기록 등록
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sendTime = sdf.format(date);
                databaseReference.child(user.getID()).child("Searchhistory").child(sendTime+"@"+finalWord).setValue("");

                MainActivity mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
                SearchFragment searchFragment = mainActivity.searchFragment;
                // 검색 화면에 검색 기록 갱신
                if(searchFragment!=null)
                    searchFragment.ref();
            }
        });


        listview = (ListView) findViewById(R.id.search_list_listview);   // 화면의 listview객체
        adapter = new SearchListViewAdapter();
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
        if(category != null){
            Category category1 = new Category(category);
            category1.category_search();
        }
        else{
            search.search();

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Product item = (Product) adapter.getItem(position);
                Intent in = new Intent(getApplicationContext(), ProductPageActivity.class);
                in.putExtra("user", user);
                in.putExtra("item", item.getProduct_name());
                startActivity(in);
            }
        });
        Button sortB = (Button)findViewById(R.id.search_list_sort);
        sortB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items2 = { "가격 순", "판매 순", "신상품 순" };
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Search_ListActivity.this);

                // 제목셋팅
                alertDialogBuilder.setTitle("sort");
                alertDialogBuilder.setSingleChoiceItems(items2, sortNum,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 정렬누르면 어떻게 할지
                                sortNum = id;
                                dialog.dismiss();
                                Sort sort = new Sort();
                                switch(id) {
                                    case 0: sort.price_sort(finalWord);
                                        break;
                                    case 1: sort.popular_sort(finalWord);
                                        break;
                                    case 2: sort.new_sort(finalWord);
                                        break;
                                }
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

