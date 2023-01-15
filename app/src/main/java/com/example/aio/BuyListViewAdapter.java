package com.example.aio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// 구매항목 리스트 어댑터
public class BuyListViewAdapter extends BaseAdapter {
    public ArrayList<Product> items;
    public ArrayList<String> dates;
    public Context mContext;

    public BuyListViewAdapter(){
        items = new ArrayList<Product>();
    }
    public BuyListViewAdapter(Context context){
        items = new ArrayList<Product>();
        dates = new ArrayList<String>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Product item,String date) {
        items.add(item);
        dates.add(date);
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
        final Product listItem = items.get(position); // 해당 위치의 리스트 아이템
        final String listDate = dates.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.buy_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        // 화면 리스트아이템의 각 객체들
        TextView tv_name = (TextView) convertView.findViewById(R.id.buy_pname);
        TextView tv_num = (TextView) convertView.findViewById(R.id.buy_pri);
        TextView tv_date = (TextView) convertView.findViewById(R.id.buy_date);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.buy_img);
        // 리스트아이템의 번호, 날짜, 사진 설정.
        String name[] = listItem.getProduct_name().split("@");
        tv_name.setText(name[0]);
        tv_num.setText(Integer.toString(listItem.getProduct_price()));
        tv_date.setText(listDate.substring(0,listDate.length()-8));

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
                // Handle any errors
            }
        });
        // 환불 버튼
        Button refund = (Button) convertView.findViewById(R.id.buy_refund);
        refund.setOnClickListener(new View.OnClickListener(){
            Boolean like = true;
            @Override
            public void onClick(View view) {// 클릭되면
                MainActivity mainActivity = (MainActivity) ((MainActivity) MainActivity.context_main)._MainActivity;
                UserID user = mainActivity.user;

                FirebaseDatabase.getInstance().getReference().child("refund").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Boolean b = true;
                        // 이미 환불 신청한 기록이 있는 제품의 경우
                        for(DataSnapshot ds : task.getResult().getChildren()){
                            if(ds.getKey().equals(user.getID()+"@"+listItem.getProduct_name())){
                                b = false;
                                Toast.makeText(view.getContext(), "이미 신청한 항목입니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                        if(b){
                            Intent in = new Intent(view.getContext(), RefundActivity.class);
                            String[] name=listItem.getProduct_name().split("@");
                            in.putExtra("user", user);
                            in.putExtra("ProductName", name[0]+"@"+name[1]);
                            context.startActivity(in);
                        }
                    }
                });

            }
        });

        return convertView;  //뷰 객체 반환
    }
}
