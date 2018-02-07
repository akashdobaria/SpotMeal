package com.spotmeal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by akash on 11/13/17.
 */

public class Review implements Parcelable {
    private String givenBy;
    private String givenBy_Name;
    private Long rating;
    private String comment;
    private String dateOfReview;
    private String givenTo;

    public String getGivenTo_Name() {
        return givenTo_Name;
    }

    public void setGivenTo_Name(String givenTo_Name) {
        this.givenTo_Name = givenTo_Name;
    }

    private String givenTo_Name;

    public String getGivenBy_Name() {
        return givenBy_Name;
    }

    public void setGivenBy_Name(String givenBy_Name) {
        this.givenBy_Name = givenBy_Name;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGivenTo() {
        return givenTo;
    }

    public void setGivenTo(String givenTo) {
        this.givenTo = givenTo;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.givenBy);
        dest.writeString(this.givenBy_Name);
        dest.writeLong(this.rating);
        dest.writeString(this.comment);
        dest.writeString(this.dateOfReview);
        dest.writeString(this.givenTo);
        dest.writeString(this.givenTo_Name);
    }

    public Review(){

    }
    public Review(String givenBy,String givenTo, String givenToName, String name, Long stars, String review, String dateOfReview){
        this.givenBy = givenBy;
        this.givenBy_Name = name;
        this.rating = stars;
        this.comment = review;
        this.dateOfReview = dateOfReview;
        this.givenTo = givenTo;
        this.givenTo_Name = givenToName;
    }

    public Review(Parcel parcel){
        this.givenBy = parcel.readString();
        this.givenBy_Name = parcel.readString();
        this.rating = parcel.readLong();
        this.comment = parcel.readString();
        this.dateOfReview = parcel.readString();
        this.givenTo = parcel.readString();
        this.givenTo_Name = parcel.readString();
    }

    public String getGivenBy() { return  this.givenBy;}
    public void setGivenBy(String givenBy) { this.givenBy = givenBy;}

    public String getDateOfReview() { return dateOfReview;}
    public void setDateOfReview(String dateOfReview) { this.dateOfReview = dateOfReview;}

}