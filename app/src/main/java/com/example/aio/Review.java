package com.example.aio;

public class Review extends Writings{
    private Boolean pros_cons;

    public Review(UserID userId, String contents, Boolean pros_cons){
        super(userId.getID(), contents);
        this.pros_cons = pros_cons;
    }

    public String getWriter(){
        return this.writer;
    }
    public String getContents(){
        return this.contents;
    }
    public Boolean getPros_cons(){
        return this.pros_cons;
    }
}
