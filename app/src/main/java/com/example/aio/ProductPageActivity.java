package com.example.aio;

import static java.lang.Math.max;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

// 제품 상세 페이지 액티비티
public class ProductPageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;// 리사이클러뷰 객체
    private ArrayList<String> mList;// 리사이클러뷰에 들어가는 이벤트 어레이리스트
    private RecyclerViewAdapter_Pimg mRecyclerViewAdapter;// 리사이클러뷰 어뎁터
    private TextView product_price;
    private TextView product_name;
    private TextView product_account;
    private TextView product_seller;
    private Button like;
    private Button in_cart;
    private ImageView detail_image;

    private String item;
    private Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent getter = getIntent();
        String name = getter.getStringExtra("item");
        product_name = findViewById(R.id.productId);
        product_price = findViewById(R.id.productPri);
        product_account = findViewById(R.id.detailaccount);
        product_seller=findViewById(R.id.productSeller);
        like=findViewById(R.id.select_like);
        in_cart=findViewById(R.id.in_cart);
        detail_image = findViewById(R.id.detail_image);

        mRecyclerView = (RecyclerView) findViewById(R.id.product_img_ListView);
        mList = new ArrayList<>();
        UserID user = (UserID) getIntent().getSerializableExtra("user");


        FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String price = task.getResult().child("product").child(getter.getStringExtra("item")).child("price").getValue().toString();
                    String img = task.getResult().child("product").child(name).child("picture").getValue().toString();
                    String detailaccount = task.getResult().child("product").child(name).child("detailAccount").getValue().toString();
                    String seller = task.getResult().child("product").child(name).child("seller").getValue().toString();
                    String detailImage = task.getResult().child("product").child(name).child("detailPicture").getValue().toString();
                    Product product = new Product(name, Integer.parseInt(price), img, detailImage, detailaccount);

                    for(DataSnapshot t: task.getResult().child("account").child(user.getID()).child("ViewedProduct").getChildren()){     //해당 제품이 최근본제품에 등록되었는지 확인
                        if(t.getKey().contains(name)){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference Ref = database.getReference();
                            Ref.child("account").child(user.getID()).child("ViewedProduct").child(t.getKey()).removeValue();
                            break;
                        }
                    }

                    for(DataSnapshot t: task.getResult().child("account").child(user.getID()).child("Favorites").getChildren()){     //해당 제품이 즐겨찾기에 등록되었는지 확인
                        if(t.getKey().toString().equals(name)){        //만약 고객이 해당 제품을 즐겨찾기에 등록했다면 동작
                            like.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                            break;
                        }
                    }

                    StorageReference myRef= FirebaseStorage.getInstance().getReference().child("Product_img");
                    StorageReference itemRef=myRef.child(product.getProduct_detail().getProduct_detail_image());

                    final long ONE_MEGABYTE=1024*1024;
                    itemRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            detail_image.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("실패용");
                        }
                    });


                    p = new Product(name, Integer.parseInt(price),img);
                    String pname[] = p.getProduct_name().split("@");
                    product_name.setText(pname[0]);
                    product_price.setText(Integer.toString(p.getProduct_price()));
                    product_account.setText(detailaccount);
                    product_seller.setText("판매자:"+seller);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();    //최근본 상품에 추가
                    DatabaseReference Ref = database.getReference();

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    Ref.child("account").child(user.getID()).child("ViewedProduct").child(sendTime+"@"+p.getProduct_name()).setValue(p.getProduct_name());

                    item = img;

                    mList.add(item);
                    mList.add(item);
                    mRecyclerViewAdapter = new RecyclerViewAdapter_Pimg(mList);//만들어진 어레이리스트로 어뎁터 인스턴스 생성.
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);//리사이클러뷰에 생성한 어뎁터 설정.
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));//가로로 넘기게 설정.

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int moveX = 0; // 스크롤 시 움직이는 픽셀수를 가지는 변수
            boolean scrolling = false;// 스크롤 중인지 확인하는 변수. true면 스크롤중.

            @Override
            public void onScrollStateChanged(RecyclerView eventListView, int newState) {// 스크롤 상태인지 상태를 가지는 메소드
                super.onScrollStateChanged(eventListView, newState);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {// 스크롤 중이 아닌경우
                    scrolling = false;// 스크롤 중 아니니까 false
                    int completePos = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();// 현재 리사이클러뷰에서 가장 처음으로 보이는 완벽한 아이템번호(전체가 다 보인다는뜻)
                    int firstPos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();// 현재 리사이클러뷰에서 가장 처음보이는 아이템번호
                    int lastPos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();// 현재 리사이클러뷰에서 가장 나중에보이는 아이템번호
                    int selectedPos = max(completePos, firstPos);// 완벽하게 보이는거랑 젤 처음 보이는거랑 비교
                    if (selectedPos != -1) {// 완벽하게 보이는거가 없는 경우
                        View viewItem;
                        if (moveX > mRecyclerView.getMeasuredWidth() / 2) {// 스크롤의 이동량이 리사이클러뷰의 가로 절반보다 큰 경우->뒤에거가 더 많이보임
                            viewItem = ((LinearLayoutManager) layoutManager).findViewByPosition(lastPos);// 보여야 하는 뷰는 뒤에거
                        } else {
                            viewItem = ((LinearLayoutManager) layoutManager).findViewByPosition(firstPos);// 아니면 앞에거
                        }
                        int itemMargin = (mRecyclerView.getMeasuredWidth() - viewItem.getMeasuredWidth()) / 2;// 화면에서의 마진고려(일단 딱맞게 해서 없어도 될거같긴함.)
                        mRecyclerView.smoothScrollBy((int) viewItem.getX() - itemMargin, (int) viewItem.getY());// 보여야하는 뷰(앞에거 or 뒤에거)로 이동
                    }
                    moveX = 0;// 스크롤 중 아니니까 값 초기화
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {// 스크롤 중인경우
                    scrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {// 스크롤하면 스크롤 되는만큼 가로세로 이동량 가지는 메소드(스크롤하고있는중에도 반복해서 실행된다. 일정시간(짧음)마다 실행되는듯)
                super.onScrolled(recyclerView, dx, dy);
                if (scrolling)// 스크롤 중이라면
                    moveX += dx;// 가로 이동량 더함.(이 메소드 스크롤 중에 손안때고 계속 스크롤 중인데도 실행되므로 계속 더해서 최종 이동량을 moveX에 가지게 한다.)
                // 반대로 스크롤 하면 dx가 -값을가지니까 그냥 계속 더하면 됨.
            }
        });
        Button reviewB = (Button) findViewById(R.id.productReview);
        reviewB.setOnClickListener(new View.OnClickListener() {        // 리뷰화면 이동
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductPageActivity.this, ReviewActivity.class);

                in.putExtra("user", user);
                in.putExtra("product_name", p.getProduct_name());
                startActivity(in);
            }
        });
        in_cart.setOnClickListener(new View.OnClickListener() {       // 장바구니담기
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int i=0;
                        for(DataSnapshot data : task.getResult().child("account").child(user.getID()).child("shoppingCart").getChildren()){
                            String pname = data.getValue().toString();
                            if(pname.equals(p.getProduct_name())){
                                i=1;
                                break;
                            }
                        }
                        if(i==0){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();

                            myRef.child("account").child(user.getID()).child("shoppingCart").child(p.getProduct_name()).setValue(p.getProduct_name());
                            Toast.makeText(ProductPageActivity.this, "장바구니에 담았습니다.", Toast.LENGTH_LONG).show();
                        }else if(i==1){
                            Toast.makeText(ProductPageActivity.this, "이미 장바구니에 담긴 제품입니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        like.setOnClickListener(new View.OnClickListener() {       // 즐겨찾기 등록 or 해제
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int i=0;
                        for(DataSnapshot t: task.getResult().child("account").child(user.getID()).child("Favorites").getChildren()){
                            if(t.getKey().toString().equals(p.getProduct_name())){        // 해당 제품이 즐겨찾기라면 해제하기
                                like.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference().child("account").child(user.getID()).child("Favorites")
                                        .child(p.getProduct_name()).setValue(null);
                                i=1;
                                break;
                            }
                        }
                        if(i==0){          // 해당제품이 즐겨찾기에 없다면 등록하기
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();

                            myRef.child("account").child(user.getID()).child("Favorites").child(p.getProduct_name()).setValue(p.getProduct_name());

                            like.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        }

                    }
                });
            }
        });

    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {// 뒤로키 누르면 액티비티 끝
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
