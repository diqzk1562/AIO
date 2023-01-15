package com.example.aio;

public abstract class Writings {
    protected String writer;
    protected String contents;
    public Writings(String writer, String contents){
        this.writer = writer;
        this.contents = contents;
    }
}
