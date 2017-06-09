package creationsofali.boomboard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.MainActivity;
import creationsofali.boomboard.datamodels.RequestCode;

/**
 * Created by ali on 6/2/17.
 */

public class BoomBoardMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived:message = " + remoteMessage.getNotification().getBody());

        // show notification
        createNotification(remoteMessage);
    }

    private void createNotification(RemoteMessage message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultIntent = PendingIntent.getActivities(
                this,
                RequestCode.RC_FCM_NOTIFICATION,
                new Intent[]{intent},
                PendingIntent.FLAG_ONE_SHOT);

        // notification sound
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_paper_pin_line)
                .setTicker("New notification on Boom Board")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.image_boom_board))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setSound(notificationSoundUri)
                .setContentIntent(resultIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_amber));

        NotificationManager nm = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(RequestCode.RC_FCM_NOTIFICATION, notificationBuilder.build());
    }
}
