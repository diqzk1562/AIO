package com.example.aio;

import java.io.Serializable;

public class UserID implements Serializable {
    private String ID;
    UserID(String ID){
        this.ID = ID;
    }
    public String getID(){
        return this.ID;
    }
}
