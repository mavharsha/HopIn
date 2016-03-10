/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Models;


public class Request {

    String eventid;
    String eventname;
    int seatsrequested;
    String createduser;
    String requesteduser;
    Boolean seen;
    Boolean status;

    public String getCreateduser() {
        return createduser;
    }

    public void setCreateduser(String createduser) {
        this.createduser = createduser;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getRequesteduser() {
        return requesteduser;
    }

    public void setRequesteduser(String requesteduser) {
        this.requesteduser = requesteduser;
    }

    public int getSeatsrequested() {
        return seatsrequested;
    }

    public void setSeatsrequested(int seatsrequested) {
        this.seatsrequested = seatsrequested;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
