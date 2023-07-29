package com.example.sharecare.models;

public class ActivityShareDTO {

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;
    private boolean isSharedWithMe;

    public boolean isSharedWithMe() {
        return isSharedWithMe;
    }

    public void setSharedWithMe(boolean sharedWithMe) {
        isSharedWithMe = sharedWithMe;
    }

    public boolean isiAmOwner() {
        return iAmOwner;
    }

    public void setiAmOwner(boolean iAmOwner) {
        this.iAmOwner = iAmOwner;
    }

    private boolean iAmOwner;

    public ActivityShareDTO(Activity activity) {
        this.activity=activity;
    }
}
