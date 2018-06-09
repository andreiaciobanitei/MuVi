package com.samaras.muvi;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.util.*;

import ro.pub.acs.hyccups.opportunistic.Channel;


public class OpportunisticCommunication extends Channel {
    @Override
    public String getName() {
        Log.v("OpportunisticComm", "debug");
        return "OpportunisticCommunication";
    }

    @Override
    public List getInterests() { return null; }

    @Override
    public void onPeerConnected(String deviceId, String userId) {
        Intent intent = new Intent(this, OpportunisticReceiver.class);
        intent.putExtra("action", "connected");
        intent.putExtra("userId", userId);
        sendBroadcast(intent);
        Log.v("OpportunisticComm", "onPeerConnected: " + deviceId + "," + userId);
    }

    @Override
    public void onPeerDisconnected(String deviceId, String userId) {
        Intent intent = new Intent(this, OpportunisticReceiver.class);
        intent.putExtra("action", "disconnected");
        intent.putExtra("userId", userId);
        sendBroadcast(intent);
        Log.v("OpportunisticComm", "onPeerDisconnected: " + deviceId + "," + userId);
    }

    @Override
    public void onMessageReceived(String sourceUserId, String destinationUserId, String message, long timestamp) { }

    @Override
    public void onDisseminationReceived(String sourceUserId, String message, long timestamp, String[] tags) { }
}
