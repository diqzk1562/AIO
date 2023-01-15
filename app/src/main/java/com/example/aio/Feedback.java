package com.example.aio;

import java.util.Date;

public class Feedback extends Writings{
    private Date date;

    // 생성자
    public Feedback(UserID userid, String feedback_contents){
        super(userid.getID(), feedback_contents);
        date = new Date();
    }
    // 생성자
    public Feedback(UserID userid, String feedback_contents, Date date){
        super(userid.getID(), feedback_contents);
        this.date = date;
    }
    public String getWriter(){
        return this.writer;
    }
    public String getfeedback(){
        return this.contents;
    }
    public Date getDate(){
        return this.date;
    }
}
