package com.spotmeal.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by parneet on 11/13/17.
 */

public class User {
    private String uid;
    private String name;
    private String email;
    private String contact;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String state;
    private String dateOfBirth;
    private String gender;
    private String aboutMe;
    private HashMap<String ,Event> events_hosting;
    private HashMap<String ,Event> events_attending;
    private HashMap<String ,Review> ratings_given;
    private HashMap<String ,Review> ratings_recieved;

    public User(){

    }


    public User(String uid, String name, String email, String contact, String addressLine1, String addressLine2, String city, String country, String state, String dateOfBirth, String gender, String aboutMe) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city= city;
        this.country = country;
        this.state = state;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.aboutMe = aboutMe;
        this.events_hosting = new HashMap<String ,Event>();
        this.events_attending = new HashMap<String ,Event>();
        this.ratings_given = new HashMap<String,Review>();
        this.ratings_recieved = new HashMap<String,Review>();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String ,Event> getEvents_hosting() {
        return events_hosting;
    }

    public void setEvents_hosting(HashMap<String ,Event> events_hosting) {
        this.events_hosting = events_hosting;
    }
    public HashMap<String ,Event> getEvents_attending() {
        return events_attending;
    }

    public void setEvents_attending(HashMap<String ,Event> events_attending) {
        this.events_attending = events_attending;
    }
    public HashMap<String ,Review> getRatings_given() {
        return ratings_given;
    }
    public void setRatings_given(HashMap<String ,Review> ratings_given) {
        this.ratings_given = ratings_given;
    }
    public HashMap<String ,Review> getRatings_recieved() {
        return ratings_recieved;
    }
    public void setRatings_recieved(HashMap<String ,Review> ratings_recieved) {
        this.ratings_recieved = ratings_recieved;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}

