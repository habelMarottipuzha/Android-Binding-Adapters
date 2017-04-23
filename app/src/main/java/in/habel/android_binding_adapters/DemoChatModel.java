package in.habel.android_binding_adapters;

import in.habel.chat_adapters.baseadapter.interfaces.chatInterface;

/**
 * Created by habel on 23/4/17.
 */

public class DemoChatModel implements chatInterface {
    private String message;
    private long addedOn;
    private boolean isOut;

    public DemoChatModel(String message, long addedOn) {
        this.message = message;
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
}
