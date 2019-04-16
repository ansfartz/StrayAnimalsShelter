package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RemovalRequest implements Parcelable {

    private String senderUserName;
    private String senderUserEmail;
    private String message;

    public RemovalRequest() {
    }

    public RemovalRequest(String senderUserName, String senderUserEmail, String message) {
        this.senderUserName = senderUserName;
        this.senderUserEmail = senderUserEmail;
        this.message = message;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getSenderUserEmail() {
        return senderUserEmail;
    }

    public void setSenderUserEmail(String senderUserEmail) {
        this.senderUserEmail = senderUserEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RemovalRequest{" +
                "senderUserName='" + senderUserName + '\'' +
                ", senderUserEmail='" + senderUserEmail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.senderUserName);
        dest.writeString(this.senderUserEmail);
        dest.writeString(this.message);
    }

    protected RemovalRequest(Parcel in) {
        this.senderUserName = in.readString();
        this.senderUserEmail = in.readString();
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
}
