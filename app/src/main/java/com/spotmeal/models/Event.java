package com.spotmeal.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.spotmeal.CreateEvent;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by parneet on 11/13/17.
 */

public class Event implements Parcelable{
    private static final String TAG = Event.class.getSimpleName();

    private String uid;
    private String title;
    private String cuisine;
    private String spiceLevel;
    private ArrayList<String> starters;
    private ArrayList<String> mainCourse;
    private ArrayList<String> deserts;
    private ArrayList<String> drinks;
    private Long minGuests;
    private Long maxGuests;
    private Long costPerPerson;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String latitude;
    private String longitude;
    private Long seatsReserved;

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    private User host;
//    private ArrayList<Reservation> reservations;
    private HashMap<String, Reservation> reservations;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public HashMap<String, Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(HashMap<String, Reservation> reservations) {
        this.reservations = reservations;
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
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.cuisine);
        dest.writeString(this.spiceLevel);
//        dest.writeList(this.starters);
//        dest.writeList(this.mainCourse);
//        dest.writeList(this.deserts);
//        dest.writeList(this.drinks);
        dest.writeLong(this.minGuests);
        dest.writeLong(this.maxGuests);
        dest.writeLong(this.costPerPerson);
        dest.writeString(this.eventDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
//        dest.writeList(this.reservations);
        dest.writeString(this.key);
        Log.i(TAG,"writing key to parcel"+this.key);

    }


    public Event(){

    }

    public Event(String uid, String title, String cuisine, String spiceLevel, ArrayList<String > starters,
                 ArrayList<String > mainCourse, ArrayList<String > deserts, ArrayList<String > drinks,
                 Long minGuests, Long maxGuests, Long costPerPerson, String eventDate, String startTime,
                 String endTime, String latitude, String longitude, Long seatsReserved, User host) {
        this.uid = uid;
        this.title = title;
        this.cuisine = cuisine;
        this.spiceLevel = spiceLevel;
        this.starters = starters;
        this.mainCourse = mainCourse;
        this.deserts = deserts;
        this.drinks = drinks;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.costPerPerson = costPerPerson;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.seatsReserved = seatsReserved;
//        this.reservations = new ArrayList<Reservation>();
        this.reservations = new HashMap<String, Reservation>();
        this.host = host;
    }

    public Event(Parcel parcel){
        this.uid = parcel.readString();
        this.title = parcel.readString();
        this.cuisine = parcel.readString();
        this.spiceLevel = parcel.readString();
//        this.starters = parcel.readArrayList();
//        this.mainCourse = parcel.readArrayList();
//        this.deserts = parcel.readArrayList();
//        this.drinks = parcel.readArrayList();
        this.minGuests = parcel.readLong();
        this.maxGuests = parcel.readLong();
        this.costPerPerson = parcel.readLong();
        this.eventDate = parcel.readString();
        this.startTime = parcel.readString();
        this.endTime = parcel.readString();
        this.latitude = parcel.readString();
        this.longitude = parcel.readString();
        this.seatsReserved = parcel.readLong();
        this.key = parcel.readString();
        Log.i(TAG,"getting key from parcel"+this.key);

    }

    public HashMap<String, Object> toMap(){
        HashMap<String,Object> map= new HashMap<>();
        map.put("uid", this.uid);
        map.put("title", this.title);
        map.put("cuisine", this.cuisine);
        map.put("spiceLevel", this.spiceLevel);
        map.put("starters", this.starters);
        map.put("mainCourse", this.mainCourse);
        map.put("deserts", this.deserts);
        map.put("drinks", this.drinks);
        map.put("minGuests", this.minGuests);
        map.put("maxGuests", this.maxGuests);
        map.put("costPerPerson", this.costPerPerson);
        map.put("eventDate", this.eventDate);
        map.put("startTime", this.startTime);
        map.put("endTime", this.endTime);
        map.put("latitude", this.latitude);
        map.put("longitude", this.longitude);
        map.put("seatsReserved", this.seatsReserved);
        map.put("reservations", this.reservations);
        map.put("key", this.key);
        map.put("host", this.host);


        return map;
    }

    public Event toEventObject(HashMap<String,Object> map){
        Event event= new Event();
        event.setUid((String)map.get("uid"));
        event.setTitle((String)map.get("title"));
        event.setCuisine((String)map.get("cuisine"));
        event.setSpiceLevel((String)map.get("spiceLevel"));
        event.setStarters((ArrayList<String>)map.get("starters" ));
        event.setMainCourse((ArrayList<String>)map.get("mainCourse"));
        event.setDeserts((ArrayList<String>)map.get("deserts") );
        event.setDrinks((ArrayList<String>)map.get("drinks" ));
        event.setMinGuests((Long)map.get("minGuests") );
        event.setMaxGuests((Long)map.get("maxGuests" ));
        event.setCostPerPerson((Long)map.get("costPerPerson" ));
        event.setEventDate((String)map.get("eventDate"));
        event.setStartTime((String)map.get("startTime" ));
        event.setEndTime((String)map.get("endTime"));
        event.setLatitude((String)map.get("latitude"));
        event.setLongitude((String)map.get("longitude"));
        event.setSeatsReserved((Long)map.get("seatsReserved"));
        event.setReservations((HashMap<String, Reservation>)map.get("reservations"));
        event.setKey((String)map.get("key"));
//        event.setHost((User)map.get("host"));
        return event;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public ArrayList<String > getStarters() {
        return starters;
    }

    public void setStarters(ArrayList<String > starters) {
        this.starters = starters;
    }

    public ArrayList<String > getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(ArrayList<String > mainCourse) {
        this.mainCourse = mainCourse;
    }

    public ArrayList<String > getDeserts() {
        return deserts;
    }

    public void setDeserts(ArrayList<String > deserts) {
        this.deserts = deserts;
    }

    public ArrayList<String > getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<String > drinks) {
        this.drinks = drinks;
    }

    public Long getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(Long costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Long getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(Long minGuests) {
        this.minGuests = minGuests;
    }

    public Long getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Long maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(Long seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

}
