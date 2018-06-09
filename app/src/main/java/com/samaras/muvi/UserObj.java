package com.samaras.muvi;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import ro.pub.acs.hyccups.opportunistic.Channel;

/**
 * Created by Andrei Aciobanitei on 11.05.2017.
 */
public class UserObj {

    private String e_mail;

    public UserObj () {}

    public UserObj (String e_mail) {
        this.e_mail = e_mail;
    }
    public String getE_mail() {
        return e_mail;
    }

    public static class MyChannel extends Channel {
        @Override
        public String getName() { return null; }

        @Override
        public List getInterests() { return null; }

        @Override
        public void onPeerConnected(String deviceId, String userId) {
            Toast.makeText(getApplicationContext(), "Connected: " + deviceId,
                    Toast.LENGTH_SHORT).show();
            Log.v("OpportunisticCommunication", "onPeerConnected: " + deviceId + "," + userId);
        }

        @Override
        public void onPeerDisconnected(String deviceId, String userId) {
            Log.v("OpportunisticCommunication", "onPeerDisconnected: " + deviceId + "," + userId);
        }

        @Override
        public void onMessageReceived(String sourceUserId, String destinationUserId, String message, long timestamp) { }

        @Override
        public void onDisseminationReceived(String sourceUserId, String message, long timestamp, String[] tags) { }
    }
}