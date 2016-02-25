/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harsha on 2/21/2016.
 */
public class Event {
    String id;
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

    int pickuptimehour;
    int pickuptimeminute;

    double eventlocationlng;
    double eventlocationlat;

    double pickuplocationlng;
    double pickuplocationlat;

    String eventlocation;
    String pickuplocation;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPickuplocation() {
        return pickuplocation;
    }

    public void setPickuplocation(String pickuplocation) {
        this.pickuplocation = pickuplocation;
    }

    public double getPickuplocationlat() {
        return pickuplocationlat;
    }

    public void setPickuplocationlat(double pickuplocationlat) {
        this.pickuplocationlat = pickuplocationlat;
    }

    public double getPickuplocationlng() {
        return pickuplocationlng;
    }

    public void setPickuplocationlng(double pickuplocationlng) {
        this.pickuplocationlng = pickuplocationlng;
    }

    public int getPickuptimehour() {
        return pickuptimehour;
    }

    public void setPickuptimehour(int pickuptimehour) {
        this.pickuptimehour = pickuptimehour;
    }

    public int getPickuptimeminute() {
        return pickuptimeminute;
    }

    public void setPickuptimeminute(int pickuptimeminute) {
        this.pickuptimeminute = pickuptimeminute;
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
