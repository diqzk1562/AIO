package com.example.aio;

import java.util.Date;

public class Search_history {
    private String searches;
    private Date date;

    public Search_history(String searches, Date date){
        this.searches = searches;
        this.date = date;

    }
    public String getSearches() {
        return this.searches;
    }
    public Date getDate(){
        return this.date;
    }
}
