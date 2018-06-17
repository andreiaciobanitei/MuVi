package com.samaras.muvi;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.util.*;

import ro.pub.acs.hyccups.opportunistic.Channel;
import ro.pub.acs.hyccups.opportunistic.SocialInfo;


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
        switch (userId) {
            case "b62ce838-3b5c-4922-ae5d-ff6ec9a337c8":
                userId = "Andrei Allview";
                break;
            case "93c35a91-3191-4a22-90d9-0ad5dae7e8bb":
                userId = "Andrei Aciobanitei";
                break;
            default:
                userId = "User necunoscut";
        }
        Intent intent = new Intent(this, OpportunisticReceiver.class);
        intent.putExtra("action", "connected");
        intent.putExtra("userId", userId);
        sendBroadcast(intent);
        Log.v("OpportunisticComm", "onPeerConnected: " + deviceId + "," + userId);
    }

    @Override
    public void onPeerDisconnected(String deviceId, String userId) {
        switch (userId) {
            case "b62ce838-3b5c-4922-ae5d-ff6ec9a337c8":
                userId = "Andrei Allview";
                break;
            case "93c35a91-3191-4a22-90d9-0ad5dae7e8bb":
                userId = "Andrei Aciobanitei";
                break;
            default:
                userId = "User necunoscut";
        }
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
