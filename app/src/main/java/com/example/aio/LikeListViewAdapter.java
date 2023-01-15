package com.example.aio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.drawable.Drawable;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

// 즐겨찾기 리스트뷰 어댑터
public class LikeListViewAdapter extends BaseAdapter {
    ArrayList<Product> items;
    Context mContext;
    ArrayList<String> sale = new ArrayList<String>();

    public LikeListViewAdapter(){
        items = new ArrayList<Product>();
    }
    public LikeListViewAdapter(Context context){
        items = new ArrayList<Product>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Product item) {
        items.add(item);
    }
    public void addSale(String num){
        sale.add(num);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final Product listItem = items.get(position);// 해당 위치의 리스트 아이템
        final String sales = sale.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.like_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        // 화면 리스트아이템의 각 객체들
        LinearLayout like = (LinearLayout) convertView.findViewById(R.id.like_item);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, ProductPageActivity.class);
                in.putExtra("item", listItem.getProduct_name());
                in.putExtra("user", ((MainActivity) MainActivity.context_main).user);//다음 엑티비티로 전달.
                mContext.startActivity(in);
            }
        });
        TextView tv_name = (TextView) convertView.findViewById(R.id.sli_name);
        TextView tv_num = (TextView) convertView.findViewById(R.id.sli_pri);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.sli_img);
        TextView tv_sale = (TextView) convertView.findViewById(R.id.sli_sal);
        tv_sale.setText("판매량: " + sales);

        String name[] = listItem.getProduct_name().split("@");
        tv_name.setText(name[0]);
        tv_num.setText(Integer.toString(listItem.getProduct_price()));

        String s = listItem.getProduct_image();
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Product_img");
        StorageReference item = listRef.child(s);
        final long ONE_MEGABYTE = 1024 * 1024;
        item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {//이미지 파일을 Byte형태로
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );//Byte를 bitmap으로 변경
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 256, 256, true);
                iv_icon.setImageBitmap(resized);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        Button set_like = (Button) convertView.findViewById(R.id.select_like);
        set_like.setOnClickListener(new View.OnClickListener(){
            Boolean like = true;
            @Override
            public void onClick(View view) {//클릭되면
                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int i=0;
                        for(DataSnapshot t: task.getResult().child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("Favorites").getChildren()){
                            if(t.getKey().toString().equals(listItem.getProduct_name())){        // 해당 제품이 즐겨찾기라면 해제하기
                                Drawable d = mContext.getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24);
                                set_like.setBackground(d);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference().child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("Favorites")
                                        .child(listItem.getProduct_name()).setValue(null);
                                i=1;
                                break;
                            }
                        }
                        if(i==0){          // 해당제품이 즐겨찾기에 없다면 등록하기
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();

                            myRef.child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("Favorites")
                                    .child(listItem.getProduct_name()).setValue(listItem.getProduct_name());

                            Drawable d = mContext.getResources().getDrawable(R.drawable.ic_baseline_favorite_24);
                            set_like.setBackground(d);
                        }

                    }
                });
            }
        });

        return convertView;  //뷰 객체 반환
    }
}
