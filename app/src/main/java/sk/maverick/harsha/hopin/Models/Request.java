/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Models;


public class Request {

    String _id;
    String eventid;
    String eventname;
    int seatsrequested;
    String requesteduseravatar;
    String createduser;
    String requesteduser;
    Boolean seen;
    Boolean status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public String getRequesteduseravatar() {
        return requesteduseravatar;
    }

    public void setRequesteduseravatar(String requesteduseravatar) {
        this.requesteduseravatar = requesteduseravatar;
    }

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
