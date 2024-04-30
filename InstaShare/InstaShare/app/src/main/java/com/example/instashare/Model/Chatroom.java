package com.example.instashare.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Chatroom implements Parcelable {
    String idchatroom;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderId;
    String lastMessage;
    String uid1;
    String uid2;
    String state;

    protected Chatroom(Parcel in) {
        idchatroom = in.readString();
        lastMessageTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        lastMessageSenderId = in.readString();
        lastMessage = in.readString();
        uid1 = in.readString();
        uid2 = in.readString();
        state = in.readString();
    }

    public static final Creator<Chatroom> CREATOR = new Creator<Chatroom>() {
        @Override
        public Chatroom createFromParcel(Parcel in) {
            return new Chatroom(in);
        }

        @Override
        public Chatroom[] newArray(int size) {
            return new Chatroom[size];
        }
    };

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }
    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public Chatroom(String idchatroom, Timestamp lastMessageTimestamp, String lastMessageSenderId, String lastMessage, String uid1, String uid2, String state) {
        this.idchatroom = idchatroom;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastMessage = lastMessage;
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.state = state;
    }

    public Chatroom() {
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdchatroom() {
        return idchatroom;
    }

    public void setIdchatroom(String idchatroom) {
        this.idchatroom = idchatroom;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idchatroom);
        dest.writeString(lastMessage);
        dest.writeString(lastMessageSenderId);
        dest.writeValue(lastMessageTimestamp);
        dest.writeString(uid1);
        dest.writeString(uid2);
        dest.writeString(state);
    }
}
