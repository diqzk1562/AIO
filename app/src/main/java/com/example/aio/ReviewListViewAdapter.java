package com.example.aio;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

// 리뷰 리스트 어댑터
public class ReviewListViewAdapter extends BaseAdapter {
    ArrayList<Review> items = new ArrayList<Review>();
    Context mContext;

    public ReviewListViewAdapter(){
        items = new ArrayList<Review>();
    }
    public ReviewListViewAdapter(Context context){
        items = new ArrayList<Review>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Review item) {
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
        final Review listItem = items.get(position);//해당 위치의 리스트 아이템

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        // 화면 리스트아이템의 각 객체들
        TextView tv_name = (TextView) convertView.findViewById(R.id.reviewer_name);
        TextView tv_num = (TextView) convertView.findViewById(R.id.review_contents);
        Button iv_icon = (Button) convertView.findViewById(R.id.review_bools);
        // 리스트아이템의 작성자, 리뷰 내용 설정
        tv_name.setText(listItem.getWriter());
        tv_num.setText(listItem.getContents());


        if(listItem.getPros_cons() == true){
            Drawable d = mContext.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24);
            iv_icon.setBackground(d);
            iv_icon.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.orange));
        }
        else{
            Drawable d = mContext.getResources().getDrawable(R.drawable.ic_baseline_thumb_down_24);
            iv_icon.setBackground(d);
            iv_icon.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.black));
        }

        return convertView;  //뷰 객체 반환
    }
}
