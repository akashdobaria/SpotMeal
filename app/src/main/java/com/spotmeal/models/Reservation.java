package com.spotmeal.models;

/**
 * Created by parneet on 12/1/17.
 */

public class Reservation {
    private String guestName;
    private Long noOfSeats;
    private String customizations;
    private String contact;

    public Reservation(){

    }
    public Reservation(String guestName,Long noOfSeats,String customizations ,String contact){
        this.guestName = guestName;
        this.noOfSeats = noOfSeats;
        this.customizations = customizations;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Long getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Long noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getCustomizations() {
        return customizations;
    }

    public void setCustomizations(String customizations) {
        this.customizations = customizations;
    }


}
