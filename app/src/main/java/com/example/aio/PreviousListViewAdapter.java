package com.example.aio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// 최근 검색 리스트 어댑터
public class PreviousListViewAdapter extends BaseAdapter {

    public ArrayList<String> items;

    public PreviousListViewAdapter(){
        items = new ArrayList<String>();
    }
    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(String item) {
        items.add(item);
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final String listItem = items.get(position);//해당 위치의 리스트 아이템

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.previous_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.previous_word);
        // 리스트아이템 설정.
        tv_name.setText(listItem);

        return convertView;  //뷰 객체 반환
    }
}
