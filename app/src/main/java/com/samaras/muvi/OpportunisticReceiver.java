package com.samaras.muvi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.samaras.muvi.Activities.ChatActivity;
import com.samaras.muvi.Activities.ProfileActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OpportunisticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            if(action.equalsIgnoreCase("connected")) {
                String userId = intent.getStringExtra("userId");
                Intent resultIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                PendingIntent pIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent resultIntent2 = new Intent(context, ChatActivity.class);

                TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
                stackBuilder2.addNextIntentWithParentStack(resultIntent2);
                PendingIntent pIntent2 =
                        stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification n  = new Notification.Builder(context)
                        .setContentTitle("MuVi")
                        .setContentText(userId + " connected")
                        .setSmallIcon(R.drawable.icon)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .addAction(R.drawable.popcorn, "Call", pIntent)
                        .addAction(R.drawable.popcorn, "Chat", pIntent2).build();

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
                Intent resultIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
//                Intent resultIntent = new Intent(context, ProfileActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent pIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

                Notification n  = new Notification.Builder(context)
                        .setContentTitle("MuVi")
                        .setContentText(userId + " disconnected")
                        .setSmallIcon(R.drawable.icon)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .addAction(R.drawable.popcorn, "Call", pIntent)
                        .addAction(R.drawable.popcorn, "Chat", pIntent).build();

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
}
