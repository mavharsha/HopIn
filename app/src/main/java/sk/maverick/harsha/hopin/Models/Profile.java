/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Models;

/**
 * Created by Harsha on 2/24/2016.
 */
public class Profile {

    String username;
    String firstname;
    String lastname;
    String phonenumber;
    String avatar;
    String emergencycntname1;
    String emergencycntname2;
    String emergencycntnumber1;
    String emergencycntnumber2;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmergencycntname1() {
        return emergencycntname1;
    }

    public void setEmergencycntname1(String emergencycntname1) {
        this.emergencycntname1 = emergencycntname1;
    }

    public String getEmergencycntname2() {
        return emergencycntname2;
    }

    public void setEmergencycntname2(String emergencycntname2) {
        this.emergencycntname2 = emergencycntname2;
    }

    public String getEmergencycntnumber1() {
        return emergencycntnumber1;
    }

    public void setEmergencycntnumber1(String emergencycntnumber1) {
        this.emergencycntnumber1 = emergencycntnumber1;
    }

    public String getEmergencycntnumber2() {
        return emergencycntnumber2;
    }

    public void setEmergencycntnumber2(String emergencycntnumber2) {
        this.emergencycntnumber2 = emergencycntnumber2;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
