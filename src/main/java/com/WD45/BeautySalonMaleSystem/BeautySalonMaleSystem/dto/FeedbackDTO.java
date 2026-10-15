package com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto;

public class FeedbackDTO {
    private String comment;
    private int rating;

    public FeedbackDTO() {}

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
