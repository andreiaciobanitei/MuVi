package com.samaras.muvi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OpportunisticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");

        if(action.equalsIgnoreCase("connected")) {
            String userId = intent.getStringExtra("userId");
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

            Notification n  = new Notification.Builder(context)
                    .setContentTitle("MuVi")
                    .setContentText("User connected to access point with id: " + userId)
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.popcorn, "Call", pIntent)
                    .addAction(R.drawable.popcorn, "More", pIntent)
                    .addAction(R.drawable.popcorn, "And more", pIntent).build();

            n.defaults |= Notification.DEFAULT_SOUND;

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
            // Vibrate the mobile phone
//            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//            vibrator.vibrate(2000);
        }
        else if(action.equalsIgnoreCase("disconnected")) {
            String userId = intent.getStringExtra("userId");
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

            Notification n  = new Notification.Builder(context)
                    .setContentTitle("MuVi")
                    .setContentText("User disconnected from access point with id: " + userId)
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.popcorn, "Call", pIntent)
                    .addAction(R.drawable.popcorn, "More", pIntent)
                    .addAction(R.drawable.popcorn, "And more", pIntent).build();

            n.defaults |= Notification.DEFAULT_SOUND;

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
            // Vibrate the mobile phone
//            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//            vibrator.vibrate(2000);
        }
    }
}
