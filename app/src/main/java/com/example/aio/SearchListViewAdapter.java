package com.example.aio;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// 검색 결과 리스트 어댑터
public class SearchListViewAdapter extends BaseAdapter {
    ArrayList<Product> items = new ArrayList<Product>();
    ArrayList<String> sale = new ArrayList<String>();

    static class ViewHolder{
        ImageView icon;
        TextView name;
        TextView price;
        TextView sale;
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

    public void setList(ArrayList<Product> list) {
        this.items = list;
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
        final ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_list_item, viewGroup, false);
            holder.icon = (ImageView) convertView.findViewById(R.id.sli_img);
            holder.name = (TextView) convertView.findViewById(R.id.sli_name);
            holder.price = (TextView) convertView.findViewById(R.id.sli_pri);
            holder.sale = (TextView) convertView.findViewById(R.id.sli_sal);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        // 리스트아이템의 이름, 가격, 사진, 판매량 설정.
        holder.sale.setText("판매량: " + sales);
        String name[] = listItem.getProduct_name().split("@");
        holder.name.setText(name[0]);
        holder.price.setText(Integer.toString(listItem.getProduct_price()));

        String s = listItem.getProduct_image();
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Product_img");
        StorageReference item = listRef.child(s);
        final long ONE_MEGABYTE = 1024 * 1024;
        item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {// 이미지 파일을 Byte형태로
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );// Byte를 bitmap으로 변경
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 256, 256, true);
                holder.icon.setImageBitmap(resized);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
        return convertView;  // 뷰 객체 반환
    }
}
