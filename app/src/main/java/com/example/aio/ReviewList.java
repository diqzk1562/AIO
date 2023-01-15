package com.example.aio;


import java.util.ArrayList;

public class ReviewList {
    private ArrayList<Review> review_list;
    private Review my_review;
    private UserID userid;

    public ReviewList(UserID userid){
        this.userid = userid;
        review_list = new ArrayList<Review>();

    }
    public void addReview(Review review){
        this.review_list.add(review);
    }

    public ArrayList<Review> getReview_list() {
        return review_list;
    }

    public Review getMy_review() {
        return my_review;
    }

    public void setMy_review(String contents, Boolean pros_cons) {
        this.my_review = new Review(userid, contents, pros_cons);
    }
}
