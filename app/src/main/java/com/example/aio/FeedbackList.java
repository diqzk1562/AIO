package com.example.aio;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackList {
    private ArrayList<Feedback> feedback_list;
    private Feedback my_feed;
    private UserID u;

    public FeedbackList(UserID userid, String feedback_contents){
        this.u = userid;
        feedback_list = new ArrayList<Feedback>();
        my_feed = new Feedback(userid, feedback_contents);
    }

    public void upload_feed(Feedback feed){
        // DB에 feed저장.
        FeedbackActivity feedbackActivity = (FeedbackActivity) ((FeedbackActivity) FeedbackActivity.context_feedback)._FeedbackActivity;
        FirebaseDatabase.getInstance().getReference().child("query").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = u.getID();
                for(DataSnapshot data : snapshot.getChildren()){
                    String name = data.child("writer").getValue().toString();
                    if(name.equals(user)){
                        Toast.makeText(feedbackActivity, "이미 작성한 질의가 존재합니다..", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("query");
                myRef.push().setValue(feed);
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void delete_feed(UserID userid){
        // DB에서 해당 유저의 feedback 삭제.
        FirebaseDatabase.getInstance().getReference().child("query").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = userid.getID();
                for(DataSnapshot data : snapshot.getChildren()){
                    String name = data.child("writer").getValue().toString();
                    if(name.equals(user)){
                        String key = data.getKey();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();

                        myRef.child("query").child(key).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public ArrayList<Feedback> getFeedback_list(){
        return this.feedback_list;
    }
    public void setMyFeedback(Feedback my_feed){
        this.my_feed = my_feed;
    }
}
