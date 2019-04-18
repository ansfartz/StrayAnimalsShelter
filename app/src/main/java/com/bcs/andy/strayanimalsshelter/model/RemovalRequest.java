package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RemovalRequest implements Parcelable {

    private String requestId;
    private String senderUserId;
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

    public RemovalRequest(String requestId, String senderUserId, String senderUserName, String senderUserEmail, String message) {
        this.requestId = requestId;
        this.senderUserId = senderUserId;
        this.senderUserName = senderUserName;
        this.senderUserEmail = senderUserEmail;
        this.message = message;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
                "requestId='" + requestId + '\'' +
                ", senderUserId='" + senderUserId + '\'' +
                ", senderUserName='" + senderUserName + '\'' +
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
        dest.writeString(this.requestId);
        dest.writeString(this.senderUserId);
        dest.writeString(this.senderUserName);
        dest.writeString(this.senderUserEmail);
        dest.writeString(this.message);
    }

    protected RemovalRequest(Parcel in) {
        this.requestId = in.readString();
        this.senderUserId = in.readString();
        this.senderUserName = in.readString();
        this.senderUserEmail = in.readString();
        this.message = in.readString();
    }

    public static final Creator<RemovalRequest> CREATOR = new Creator<RemovalRequest>() {
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
