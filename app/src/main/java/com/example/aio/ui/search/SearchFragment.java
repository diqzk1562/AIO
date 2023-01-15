package com.example.aio.ui.search;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aio.MainActivity;
import com.example.aio.PreviousListViewAdapter;
import com.example.aio.Search_ListActivity;
import com.example.aio.Search_history;
import com.example.aio.databinding.FragmentSearchBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

// 검색화면을 담당하는 SearchFragment. MainActiviy에서 동작한다.
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private String user;
    private ListView listview;
    private PreviousListViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 메인 액티비티의 context와 변수를 사용하기위해 MainActivity 객체 가져옴.
        MainActivity mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
        // 메인 액티비티의 searchFragment에 현재 플래그먼트 설정.
        mainActivity.searchFragment = this;

        user = mainActivity.user.getID();

        listview = binding.previousSearch;   // 화면의 listview객체
        adapter = new PreviousListViewAdapter();

        // DB에서 검색 기록 가져오기
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> deleteList = new ArrayList<String>();
                ArrayList<Search_history> list = new ArrayList<>();
                adapter.items = new ArrayList<String>();    // 어댑터 초기화
                // 검색 기록에서 데이터 가지고 옴.
                for(DataSnapshot data: task.getResult().child("account").child(user).child("Searchhistory").getChildren()){
                    String searchWord = data.getKey();
                    String search[] = searchWord.split("@");    // 데이터에서 날짜와 검색한 문자열 분리
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date to = null;
                    try {
                        to = sdf.parse(search[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // DB에서 얻은 데이터로 Search_history인스턴스 생성하여 리스트에 추가.
                    list.add(new Search_history(search[1], to));
                }
                // DB에서 날짜로 오름차순으로 정렬되어있기 때문에
                // 최근 순서로 보여주기위해 reverse사용
                Collections.reverse(list);
                for(Search_history s: list){
                    // 만약 데이터가 20개가 넘는다면 이후는 deleteList에 저장
                    if(adapter.items.size()>19){
                        deleteList.add(s.getSearches());
                    }
                    else{
                        adapter.addItem(s.getSearches());
                    }
                }
                // deleteList에 저장된 데이터들은 DB에서 삭제.(이를 통해 DB에서 20개씩만 저장하도록 한다.)
                for(DataSnapshot data: task.getResult().child("account").child(user).child("Searchhistory").getChildren()){
                    for(String sname : deleteList){
                        String searchWord[] = data.getKey().split("@");
                        // DB에서 검색기록 일치하는것 삭제
                        if(sname.equals(searchWord[1])){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference Ref = database.getReference();
                            Ref.child("account").child(user).child("Searchhistory").child(data.getKey()).removeValue();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
        });
        // 검색 기록의 항목을 클릭하면 해당 검색문자열로 검색.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String word = adapter.getItem(i);//아이템의 문자열 가져옴
                Intent in = new Intent(getActivity(), Search_ListActivity.class);
                in.putExtra("user", mainActivity.user);
                in.putExtra("search_word",word);// 다음 엑티비티로 전달.
                startActivity(in);// 검색 리스트 액티비티 실행.
            }
        });

        // 검색 에딧텍스트
        EditText search_word = binding.editText;

        // 검색화면 검색버튼
        Button bt = binding.search;
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String word = search_word.getText().toString().trim();// 에딧텍스트의 글 가져와 문자열로
                if(search_word.length() == 0 || word.equals("") || word.equals(null)){
                    Toast.makeText(mainActivity, "검색어를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    search_word.setText("");
                    return;
                }
                else{
                    search_word.setText("");
                    Intent in = new Intent(getActivity(), Search_ListActivity.class);
                    in.putExtra("user", mainActivity.user);
                    in.putExtra("search_word",word);
                    startActivity(in);// 검색 리스트 액티비티 실행.
                }
            }
        });

        return root;
    }
    // 검색을 통해 새로운 검색기록이 생길경우 다시 검색기록 리스트를 출력하기 위한 메소드
    public void ref(){
        adapter = new PreviousListViewAdapter();
        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> deleteList = new ArrayList<String>();
                ArrayList<Search_history> list = new ArrayList<>();
                adapter.items = new ArrayList<String>();
                for(DataSnapshot ds: task.getResult().child("account").child(user).child("Searchhistory").getChildren()){
                    String searchWord = ds.getKey();
                    String search[] = searchWord.split("@");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date to = null;
                    try {
                        to = sdf.parse(search[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    list.add(new Search_history(search[1], to));
                }
                Collections.reverse(list);
                for(Search_history s: list){
                    if(adapter.items.size()>19){
                        deleteList.add(s.getSearches());
                    }
                    else{
                        adapter.addItem(s.getSearches());
                    }
                }
                for(DataSnapshot t: task.getResult().child("account").child(user).child("Searchhistory").getChildren()){
                    for(String sname : deleteList){
                        String searchWord[] = t.getKey().split("@");
                        if(sname.equals(searchWord[1])){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference Ref = database.getReference();
                            Ref.child("account").child(user).child("Searchhistory").child(t.getKey()).removeValue();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}