package sk.maverick.harsha.hopin.Models;

/**
 * Created by Harsha on 4/14/2016.
 */
public class Location {
    static double latitude = 38.5623067;
    static double longitude = -121.4231014;

    public static double getLongitude() {
        return Location.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static double getLatitude() {
        return Location.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
