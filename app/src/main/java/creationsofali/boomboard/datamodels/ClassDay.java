package creationsofali.boomboard.datamodels;

import java.util.List;

/**
 * Created by ali on 5/8/17.
 */

public class ClassDay {

    private List<ClassSession> sessionList;
    private boolean isRetrieved;

    public List<ClassSession> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<ClassSession> sessionList) {
        this.sessionList = sessionList;
    }

    public boolean isRetrieved() {
        return isRetrieved;
    }

    public void setRetrieved(boolean retrieved) {
        isRetrieved = retrieved;
    }
}
