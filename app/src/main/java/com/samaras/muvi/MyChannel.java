package com.samaras.muvi;

import android.util.Log;

import java.util.*;

import ro.pub.acs.hyccups.opportunistic.Channel;


public class MyChannel extends Channel {
    @Override
    public String getName() { return null; }

    @Override
    public List getInterests() { return null; }

    @Override
    public void onPeerConnected(String deviceId, String userId) {
        Log.v("MyChannel", "onPeerConnected: " + deviceId + "," + userId);
    }

    @Override
    public void onPeerDisconnected(String deviceId, String userId) {
        Log.v("MyChannel", "onPeerDisconnected: " + deviceId + "," + userId);
    }

    @Override
    public void onMessageReceived(String sourceUserId, String destinationUserId, String message, long timestamp) { }

    @Override
    public void onDisseminationReceived(String sourceUserId, String message, long timestamp, String[] tags) { }
}
