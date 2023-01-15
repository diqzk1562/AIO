package com.example.aio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 고객질의 화면 액티비티
public class FeedbackActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText feedbackText;
    private Button sendButton;
    public static Context context_feedback;
    public Activity _FeedbackActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context_feedback = this;
        _FeedbackActivity = FeedbackActivity.this;

        UserID user = (UserID) getIntent().getSerializableExtra("user");
        recyclerView = (RecyclerView) findViewById(R.id.feedback_recyceler);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("query");

        feedbackText = (EditText) findViewById(R.id.feedbackText);
        sendButton = (Button) findViewById(R.id.feedbackButton);
        sendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String msg = feedbackText.getText().toString().trim();
                if(feedbackText.length() != 0 && !msg.equals("") && !msg.equals(null)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    feedbackText.setText("");//다시 빈칸으로
                    Feedback feed = new Feedback(user, msg);
                    // 질의 파이어베이스에 저장
                    String day = sdf.format(feed.getDate());
                    myRef.child(day+"@"+feed.getWriter()).setValue(feed.getfeedback());
                }
                else{
                    feedbackText.setText("");
                }
            }
        });

        FeedbackAdapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<Feedback> myDataset;

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FeedbackActivity.this);

        myDataset = new ArrayList<Feedback>();
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FeedbackAdapter(myDataset, user.getID());
        recyclerView.setAdapter(mAdapter);
        myRef.addChildEventListener(new ChildEventListener() {
            // 질의가 추가되면 리스트뷰의 어댑터 갱신.
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey().toString();
                String value = snapshot.getValue().toString();
                String name[] = key.split("@");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date to = null;
                try {
                    to = sdf.parse(name[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Feedback msg = new Feedback(new UserID(name[1]), value, to);
                ((FeedbackAdapter)mAdapter).setArrayData(msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // 질의를 길게 누르는 경우
        mAdapter.setOnItemLongClickListener(new FeedbackAdapter.OnItemLongClickEventListener(){
            @Override
            public void onItemLongClick(View a_view, final int a_position) {
                // 사용자 본인의 질의인 경우
                if(myDataset.get(a_position).getWriter().equals(user.getID())){
                    // 삭제를 확인하는 FeedbackPopActivity실행
                    Intent in = new Intent(FeedbackActivity.this, FeedbackPopActivity.class);
                    in.putExtra("user", user);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String day = sdf.format(myDataset.get(a_position).getDate());
                    in.putExtra("feed", day+"@"+myDataset.get(a_position).getWriter());
                    startActivity(in);
                }
            }
        });
    }
}
