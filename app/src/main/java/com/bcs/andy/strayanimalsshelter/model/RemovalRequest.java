package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RemovalRequest implements Parcelable {

    private String requestingUser;
    private String message;

    public RemovalRequest() {
    }

    public RemovalRequest(String requestingUser, String message) {
        this.requestingUser = requestingUser;
        this.message = message;
    }

    public String getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(String requestingUser) {
        this.requestingUser = requestingUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requestingUser);
        dest.writeString(this.message);
    }

    protected RemovalRequest(Parcel in) {
        this.requestingUser = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<RemovalRequest> CREATOR = new Parcelable.Creator<RemovalRequest>() {
        @Override
        public RemovalRequest createFromParcel(Parcel source) {
            return new RemovalRequest(source);
        }

        @Override
        public RemovalRequest[] newArray(int size) {
            return new RemovalRequest[size];
        }
    };

    @Override
    public String toString() {
        return "RemovalRequest{" +
                "requestingUser='" + requestingUser + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
