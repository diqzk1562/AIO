package com.example.aio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// 고객질의 삭제 팝업
public class FeedbackPopActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback_pop);

        FeedbackActivity feedbackActivity = (FeedbackActivity) ((FeedbackActivity) FeedbackActivity.context_feedback)._FeedbackActivity;

        UserID user = (UserID) getIntent().getSerializableExtra("user");
        String s = getIntent().getStringExtra("feed");

        TextView text1 = (TextView) findViewById(R.id.pop_text1);
        text1.setText("Delete");

        TextView text2 = (TextView) findViewById(R.id.pop_text2);
        text2.setText("삭제 하시겠습니까?");

        Button delete = (Button)findViewById(R.id.pop_yes);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("query").child(s).removeValue();
                feedbackActivity.finish();
                Intent in = new Intent(FeedbackPopActivity.this, FeedbackActivity.class);
                in.putExtra("user", user);
                startActivity(in);
                finish();
            }
        });
    }
    public void mOnClose(View V){
        finish();
    }
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}
