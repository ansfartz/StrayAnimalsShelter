package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AdoptionRequest implements Parcelable {

    private String requestId;
    private String senderUserId;
    private String senderUsername;
    private String senderUserEmail;

    public AdoptionRequest() {
    }

    public AdoptionRequest(String requestId, String senderUserId, String senderUsername, String senderUserEmail) {
        this.requestId = requestId;
        this.senderUserId = senderUserId;
        this.senderUsername = senderUsername;
        this.senderUserEmail = senderUserEmail;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderUserEmail() {
        return senderUserEmail;
    }

    public void setSenderUserEmail(String senderUserEmail) {
        this.senderUserEmail = senderUserEmail;
    }

    @Override
    public String toString() {
        return "AdoptionRequest{" +
                "requestId='" + requestId + '\'' +
                ", senderUserId='" + senderUserId + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", senderUserEmail='" + senderUserEmail + '\'' +
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
        dest.writeString(this.senderUsername);
        dest.writeString(this.senderUserEmail);
    }

    protected AdoptionRequest(Parcel in) {
        this.requestId = in.readString();
        this.senderUserId = in.readString();
        this.senderUsername = in.readString();
        this.senderUserEmail = in.readString();
    }

    public static final Parcelable.Creator<AdoptionRequest> CREATOR = new Parcelable.Creator<AdoptionRequest>() {
        @Override
        public AdoptionRequest createFromParcel(Parcel source) {
            return new AdoptionRequest(source);
        }

        @Override
        public AdoptionRequest[] newArray(int size) {
            return new AdoptionRequest[size];
        }
    };
}
