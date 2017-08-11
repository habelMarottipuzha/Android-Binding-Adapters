package in.habel.models;

import java.io.Serializable;

import in.habel.interfaces.chatInterface;

/**
 * Created by habel on 23/4/17.
 */

public class DemoChatModel implements chatInterface, Serializable {
    private boolean isOut;
    private String message;
    private String user;
    private long addedOn;
    public DemoChatModel(String message, long addedOn) {
        this.message = message;
        this.addedOn = addedOn;
    }

    public DemoChatModel(String message, String user, long addedOn) {
        this.message = message;
        this.user = user;
        this.addedOn = addedOn;
    }

    public void setIsOut(boolean isOut) {
        this.isOut = isOut;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(long addedOn) {
        this.addedOn = addedOn;
    }

    @Override
    public boolean isOutgoing() {
        return isOut;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DemoChatModel that = (DemoChatModel) o;

        if (isOut != that.isOut) return false;
        if (addedOn != that.addedOn) return false;
        if (!message.equals(that.message)) return false;
        return user != null ? user.equals(that.user) : that.user == null;

    }

    @Override
    public int hashCode() {
        int result = (isOut ? 1 : 0);
        result = 31 * result + message.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (int) (addedOn ^ (addedOn >>> 32));
        return result;
    }
}
