package com.example.aio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// 환불 페이지 액티비티
public class RefundActivity extends AppCompatActivity {

    TextView reason;
    TextView phoneNum;
    TextView refundP;
    Button btn_refund;
    Button btn_cancel;
    Refund refund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        UserID user=new UserID(((MainActivity) MainActivity.context_main).user.getID());
        String Pname = getIntent().getStringExtra("ProductName");

        reason = findViewById(R.id.refund_reason);
        phoneNum = findViewById(R.id.refund_num);
        refundP = findViewById(R.id.refund_p);
        String name[] = Pname.split("@");
        refundP.setText(name[0]);
        btn_refund = findViewById(R.id.refund_button);
        btn_cancel = findViewById(R.id.refund_cancle);


        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reason.getText().toString().trim().isEmpty() || phoneNum.getText().toString().trim().isEmpty()){
                    Toast.makeText(RefundActivity.this, "빈칸을 모두 입력해주세요", Toast.LENGTH_LONG).show();
                }else{
                    refund=new Refund(user,reason.getText().toString(),Pname);
                    refund.request_refund(phoneNum.getText().toString());
                    Toast.makeText(RefundActivity.this, "환불신청이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
