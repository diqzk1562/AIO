package com.example.aio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// 리사이클러뷰 어댑터
public class RecyclerViewAdapter_Pimg extends RecyclerView.Adapter<RecyclerViewAdapter_Pimg.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_item = (ImageView) itemView.findViewById(R.id.product_img_item);// 이벤트 이미지 아이템 이미지뷰
        }
    }

    private ArrayList<String> mList = null;

    public RecyclerViewAdapter_Pimg(ArrayList<String> mList) {
        // 인자로 받은 어레이리스트를 리사이클러뷰에 넣을 리스트로
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.product_img_item, parent, false);
        RecyclerViewAdapter_Pimg.ViewHolder vh = new RecyclerViewAdapter_Pimg.ViewHolder(view);
        return vh;// 아이템 뷰를 위해 뷰홀더 리턴
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_Pimg.ViewHolder holder, int position) {
        String item = mList.get(position);

        StorageReference myRef= FirebaseStorage.getInstance().getReference().child("Product_img");
        StorageReference itemRef=myRef.child(item);

        final long ONE_MEGABYTE=1024*1024;
        itemRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.imgView_item.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("실패용");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
