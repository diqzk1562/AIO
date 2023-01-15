package com.example.aio.ui.home;

import static java.lang.Math.max;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aio.Event;
import com.example.aio.MainActivity;
import com.example.aio.ProductPageActivity;
import com.example.aio.RecyclerViewAdapter;
import com.example.aio.Product;
import com.example.aio.SearchListViewAdapter;
import com.example.aio.Search_ListActivity;
import com.example.aio.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 홈 화면을 담당하는 HomeFragment. MainActivity에서 동작한다.
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;// 바텀네비게이션에 쓰는 바인딩
    private RecyclerView mRecyclerView;// 리사이클러뷰 객체
    private ArrayList<Event> mList;// 리사이클러뷰에 들어가는 이벤트 어레이리스트
    private RecyclerViewAdapter mRecyclerViewAdapter;// 리사이클러뷰 어뎁터
    MainActivity mainActivity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 메인 액티비티의 context와 변수를 사용하기위해 MainActivity 객체 가져옴.
        mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;

        // 검색 에딧텍스트
        EditText search_word = binding.searchWord;
        // 검색 버튼
        Button search_button = binding.searchButton;
        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                String word = search_word.getText().toString().trim();// 에딧텍스트의 글 가져와 문자열로
                if(search_word.length() == 0 || word.equals("") || word.equals(null)){
                    Toast.makeText(mainActivity, "검색어를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    search_word.setText("");
                    return;
                }
                else{
                    search_word.setText("");// 에딧텍스트 글자 초기화
                    Intent in = new Intent(getActivity(), Search_ListActivity.class);
                    // 다음 엑티비티로 전달.
                    in.putExtra("user", mainActivity.user);
                    in.putExtra("search_word",word);
                    startActivity(in);// 검색 리스트 액티비티 실행.
                }
            }
        });

        mRecyclerView = binding.eventListView;// 해당 프래그먼트의 리사이클러뷰
        mList = new ArrayList<>();// 이벤트 어레이 리스트 생성.

        // DB에서 이벤트 이미지를 가져와 리스트에 추가.
        FirebaseStorage.getInstance().getReference().child("Event_img").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final long ONE_MEGABYTE = 1024 * 1024;
                for(StorageReference sr : listResult.getItems()) {
                    sr.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mList.add(new Event(bitmap));

                            mRecyclerViewAdapter = new RecyclerViewAdapter(mList);// 만들어진 어레이리스트로 어뎁터 인스턴스 생성.
                            mRecyclerView.setAdapter(mRecyclerViewAdapter);// 리사이클러뷰에 생성한 어뎁터 설정.
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));//가로로 넘기게 설정.
                        }
                    });
                }
            }
        });

        binding.eventListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int moveX = 0; // 스크롤 시 움직이는 픽셀 수를 가지는 변수
            boolean scrolling=false;// 스크롤 중인지 확인하는 변수. true면 스크롤중.
            @Override
            public void onScrollStateChanged(RecyclerView eventListView, int newState) {// 스크롤 상태인지 상태를 가지는 메소드
                super.onScrollStateChanged(eventListView, newState);
                RecyclerView.LayoutManager layoutManager = binding.eventListView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE){// 스크롤 중이 아닌경우
                    scrolling = false;// 스크롤 중 아니니까 false
                    int completePos = ((LinearLayoutManager)layoutManager).findFirstCompletelyVisibleItemPosition();// 현재 리사이클러뷰에서 가장 처음으로 보이는 완벽한 아이템번호(전체가 다 보인다는뜻)
                    int firstPos = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();// 현재 리사이클러뷰에서 가장 처음보이는 아이템번호
                    int lastPos = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();// 현재 리사이클러뷰에서 가장 나중에보이는 아이템번호
                    int selectedPos = max(completePos,firstPos);// 완벽하게 보이는거랑 젤 처음 보이는거랑 비교
                    if(selectedPos!=-1){// 완벽하게 보이는거가 없는 경우
                        View viewItem;
                        if(moveX>binding.eventListView.getMeasuredWidth()/2) {// 스크롤의 이동량이 리사이클러뷰의 가로 절반보다 큰 경우->뒤에거가 더 많이보임
                            viewItem = ((LinearLayoutManager) layoutManager).findViewByPosition(lastPos);// 보여야 하는 뷰는 뒤에거
                        }
                        else{
                            viewItem = ((LinearLayoutManager) layoutManager).findViewByPosition(firstPos);// 아니면 앞에거
                        }
                        int itemMargin = (binding.eventListView.getMeasuredWidth()-viewItem.getMeasuredWidth())/2;// 화면에서의 마진고려(일단 딱맞게 해서 없어도 될거같긴함.)
                        binding.eventListView.smoothScrollBy((int) viewItem.getX() - itemMargin, (int) viewItem.getY());// 보여야하는 뷰(앞에거 or 뒤에거)로 이동
                    }
                    moveX=0;// 스크롤 중 아니니까 값 초기화
                }
                else if(newState==RecyclerView.SCROLL_STATE_DRAGGING){// 스크롤 중인경우
                    scrolling = true;
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {// 스크롤하면 스크롤 되는만큼 가로세로 이동량 가지는 메소드(스크롤하고있는중에도 반복해서 실행된다. 일정시간(짧음)마다 실행되는듯)
                super.onScrolled(recyclerView, dx, dy);
                if(scrolling)// 스크롤 중이라면
                    moveX+=dx;// 가로 이동량 더함.(이 메소드 스크롤 중에 손안때고 계속 스크롤 중인데도 실행되므로 계속 더해서 최종 이동량을 moveX에 가지게 한다.)
                    // 반대로 스크롤 하면 dx가 -값을가지니까 그냥 계속 더하면 된다.
            }
        });

        // 랜덤 상품 소개
        ListView listview;
        listview = binding.randomListListview;   // 화면의 listview객체
        SearchListViewAdapter adapter;  // 리스트 뷰에 연결할 어댑터
        adapter = new SearchListViewAdapter();

        // DB에서 제품들을 가져와 랜덤으로 어댑터에 추가.(파이어베이스에 랜덤으로 몇개만 가져오는 메소드가 없으므로 다가지고와 랜덤으로 선택)
        FirebaseDatabase.getInstance().getReference().child("product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<Integer, Product> map = new HashMap<Integer, Product>();
                Map<Integer, String> smap = new HashMap<Integer, String>();
                Integer count = 0;
                // 모든 제품을 map에 저장.
                for(DataSnapshot data : snapshot.getChildren()){
                    String name = data.getKey().toString();
                    String price = data.child("price").getValue().toString();
                    String img = data.child("picture").getValue().toString();
                    String sales = data.child("sell_count").getValue().toString();
                    map.put(count, new Product(name, Integer.parseInt(price), img));
                    smap.put(count++, sales);
                }
                count = 0;
                List keys = new ArrayList(map.keySet());
                Collections.shuffle(keys);
                // 셔플을 통해 랜덤으로 앞에서부터 5개만 가지고온다.
                for (Object o : keys) {
                    Product p = map.get(o);
                    String s = smap.get(o);
                    adapter.addItem(p);
                    adapter.addSale(s);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    count++;
                    if(count==5)
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // 랜덤 제품의 리스트에서 해당 제품이 클릭된 경우
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Product item = (Product) adapter.getItem(position);
                Intent in = new Intent(getActivity(), ProductPageActivity.class);
                // 클릭된 제품의 이름을 다음 액티비티로 넘겨줌
                in.putExtra("user", mainActivity.user);
                in.putExtra("item", item.getProduct_name());
                startActivity(in);
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