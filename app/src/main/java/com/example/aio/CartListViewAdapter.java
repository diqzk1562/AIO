package com.example.aio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

// 장바구니 리스트 어댑터
public class CartListViewAdapter extends BaseAdapter {
    ArrayList<Product> items;
    Context mContext;

    public CartListViewAdapter(){
        items = new ArrayList<Product>();
    }
    public CartListViewAdapter(Context context){
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

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        // 화면 리스트아이템의 각 객체들
        TextView tv_name = (TextView) convertView.findViewById(R.id.cart_name);
        TextView tv_num = (TextView) convertView.findViewById(R.id.cart_pri);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.cart_img);
        // 리스트아이템의 번호, 날짜, 사진 설정.
        String name[] = listItem.getProduct_name().split("@");
        tv_name.setText(name[0]);
        tv_num.setText(Integer.toString(listItem.getProduct_price()));

        String s = listItem.getProduct_image();
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Product_img");
        StorageReference item = listRef.child(s);
        final long ONE_MEGABYTE = 1024 * 1024;
        item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {// 이미지 파일을 Byte형태로
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );// Byte를 bitmap으로 변경
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
                iv_icon.setImageBitmap(resized);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        Button set_like = (Button) convertView.findViewById(R.id.cart_delete);
        set_like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 클릭되면
                // DB에서 삭제 구현.
                FirebaseDatabase.getInstance().getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int i=0;
                        for(DataSnapshot t: task.getResult().child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("shoppingCart").getChildren()){

                            if(t.getKey().toString().equals(listItem.getProduct_name())){   //해당 제품이 장바구니에 등록되있다면 해제하기

                                set_like.setText("재등록");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference().child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("shoppingCart")
                                        .child(listItem.getProduct_name()).setValue(null);  //삭제
                                i=1;
                                break;
                            }

                        }
                        if(i==0){// 없으면 다시 등록
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();

                            myRef.child("account").child(((MainActivity) MainActivity.context_main).user.getID()).child("shoppingCart")
                                    .child(listItem.getProduct_name()).setValue(listItem.getProduct_name());

                            set_like.setText("삭제");
                        }

                    }
                });
            }
        });

        return convertView;  // 뷰 객체 반환
    }
}
