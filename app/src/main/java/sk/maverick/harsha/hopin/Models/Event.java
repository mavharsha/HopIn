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

    String eventName;
    String eventType;
    String seatsAvailabale;
    int eventDateDay;
    int eventDateMonth;
    int eventDateYear;
    int eventTimeHour;
    int eventTimeMinute;
    int pickupTimeHour;
    int pickupTimeMinute;
    String eventLocation;
    String pickUpLocation;
    Map<String, String> eventlist = new HashMap<>();

    public int getEventDateDay() {
        return eventDateDay;
    }


    public void setEventDateDay(int eventDateDay) {
        this.eventDateDay = eventDateDay;
    }

    public int getEventDateMonth() {
        return eventDateMonth;
    }

    public void setEventDateMonth(int eventDateMonth) {
        this.eventDateMonth = eventDateMonth;
    }

    public int getEventDateYear() {
        return eventDateYear;
    }

    public void setEventDateYear(int eventDateYear) {
        this.eventDateYear = eventDateYear;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventTimeHour() {
        return eventTimeHour;
    }

    public void setEventTimeHour(int eventTimeHour) {
        this.eventTimeHour = eventTimeHour;
    }

    public int getEventTimeMinute() {
        return eventTimeMinute;
    }

    public void setEventTimeMinute(int eventTimeMinute) {
        this.eventTimeMinute = eventTimeMinute;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public int getPickupTimeHour() {
        return pickupTimeHour;
    }

    public void setPickupTimeHour(int pickupTimeHour) {
        this.pickupTimeHour = pickupTimeHour;
    }

    public int getPickupTimeMinute() {
        return pickupTimeMinute;
    }

    public void setPickupTimeMinute(int pickupTimeMinute) {
        this.pickupTimeMinute = pickupTimeMinute;
    }

    public String getSeatsAvailabale() {
        return seatsAvailabale;
    }

    public void setSeatsAvailabale(String seatsAvailabale) {
        this.seatsAvailabale = seatsAvailabale;
    }

    public Map<String, String> getEventlist() {
        return eventlist;
    }

    public void setEventlist(Map<String, String> eventlist) {
        this.eventlist = eventlist;
    }

    public void setEventAttribute(String key, String value){

        eventlist.put(key, value);
    }


    @Override
    public String toString() {
        return "Event{" +
                "eventDateDay=" + eventDateDay +
                ", eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", seatsAvailabale='" + seatsAvailabale + '\'' +
                ", eventDateMonth=" + eventDateMonth +
                ", eventDateYear=" + eventDateYear +
                ", eventTimeHour=" + eventTimeHour +
                ", eventTimeMinute=" + eventTimeMinute +
                ", pickupTimeHour=" + pickupTimeHour +
                ", pickupTimeMinute=" + pickupTimeMinute +
                ", eventLocation='" + eventLocation + '\'' +
                ", pickUpLocation='" + pickUpLocation + '\'' +
                '}';
    }
}
