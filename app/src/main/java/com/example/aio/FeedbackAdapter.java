package com.example.aio;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

// 고객 질의 화면의 리스트뷰 어댑터
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private ArrayList<Feedback> mDataset;
    String name;
    private OnItemLongClickEventListener mItemLongClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView msgText;
        public TextView timeText;
        public View rootView;
        public LinearLayout msgLinear;
        public MyViewHolder(View v, final OnItemLongClickEventListener a_itemLongClickListener){
            super(v);
            nameText = v.findViewById(R.id.nameText);
            msgText = v.findViewById(R.id.chatM);
            timeText = v.findViewById(R.id.timeText);
            msgLinear = v.findViewById(R.id.msgLinear);
            rootView = v;
            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View a_view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        a_itemLongClickListener.onItemLongClick(a_view, position);
                    }

                    return false;
                }
            });
        }

    }
    public FeedbackAdapter(ArrayList<Feedback> mDataset, String name) {
        this.mDataset = mDataset;
        this.name = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_consult_view, parent, false);

        MyViewHolder viewholder = new MyViewHolder(linearLayout, mItemLongClickListener);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Feedback feed = mDataset.get(position);
        holder.nameText.setText(feed.getWriter());
        holder.msgText.setText(feed.getfeedback());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = sdf.format(feed.getDate());
        holder.timeText.setText(day);
        // 자신의 질의인 경우
        if(feed.getWriter().equals(name)){
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.timeText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgLinear.setGravity(Gravity.RIGHT);
            holder.msgText.setBackgroundResource(R.drawable.my_msg);
        }
        else if(feed.getWriter().contains("0-답변")){ // AIO에서 답변한 경우
            String aio = feed.getWriter();
            aio = aio.substring(0,aio.length()-4) + "님에 대한 답변";
            holder.nameText.setText(aio);
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.timeText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgLinear.setGravity(Gravity.RIGHT);
            holder.msgText.setBackgroundResource(R.drawable.aio_msg);
        }
        else { // 다른 사용자의 질의인 경우
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.timeText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgLinear.setGravity(Gravity.LEFT);
            holder.msgText.setBackgroundResource(R.drawable.not_my_msg);
        }
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(View a_view, int a_position);
    }
    public void setOnItemLongClickListener(OnItemLongClickEventListener a_listener) {
        mItemLongClickListener = a_listener;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 데이터를 입력
    public void setArrayData(Feedback MData) {
        mDataset.add(MData);
        notifyItemInserted(mDataset.size()-1);
    }
}
