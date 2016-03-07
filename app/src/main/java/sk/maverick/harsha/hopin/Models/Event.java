/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Models;

import java.util.List;


public class Event {
    String _id;
    String username;

    String eventname;
    String eventtype;
    String seatsavailable;
    String preferences;

    int dateday;
    int datemonth;
    int dateyear;

    int eventtimehour;
    int eventtimeminute;

    double eventlocationlng;
    double eventlocationlat;
    String eventlocation;

    List<Pickup> pickup;

    public List<Pickup> getPickup() {
        return pickup;
    }

    public void setPickup(List<Pickup> pickup) {
        this.pickup = pickup;
    }

    public int getDateday() {
        return dateday;
    }

    public void setDateday(int dateday) {
        this.dateday = dateday;
    }

    public int getDatemonth() {
        return datemonth;
    }

    public void setDatemonth(int datemonth) {
        this.datemonth = datemonth;
    }

    public int getDateyear() {
        return dateyear;
    }

    public void setDateyear(int dateyear) {
        this.dateyear = dateyear;
    }

    public String getSeatsavailable() {
        return seatsavailable;
    }

    public void setSeatsavailable(String seatsavailable) {
        this.seatsavailable = seatsavailable;
    }
    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }

    public double getEventlocationlat() {
        return eventlocationlat;
    }

    public void setEventlocationlat(double eventlocationlat) {
        this.eventlocationlat = eventlocationlat;
    }

    public double getEventlocationlng() {
        return eventlocationlng;
    }

    public void setEventlocationlng(double eventlocationlng) {
        this.eventlocationlng = eventlocationlng;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public int getEventtimehour() {
        return eventtimehour;
    }

    public void setEventtimehour(int eventtimehour) {
        this.eventtimehour = eventtimehour;
    }

    public int getEventtimeminute() {
        return eventtimeminute;
    }

    public void setEventtimeminute(int eventtimeminute) {
        this.eventtimeminute = eventtimeminute;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
