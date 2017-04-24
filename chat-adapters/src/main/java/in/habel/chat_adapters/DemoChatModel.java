package in.habel.chat_adapters;

import in.habel.chat_adapters.interfaces.chatInterface;

/**
 * Created by habel on 23/4/17.
 */

public class DemoChatModel implements chatInterface {
    private static
    boolean isOut;
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
        isOut = !isOut; //toggle chat positions
        return isOut;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
