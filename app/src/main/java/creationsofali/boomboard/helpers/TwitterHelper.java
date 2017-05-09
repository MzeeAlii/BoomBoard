package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by ali on 5/4/17.
 */

public class TwitterHelper {

    public static void launchTwitter(String username, Context context) {
        Intent twitterIntent = null;
        try {
            // launch app if available
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + username));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // no app, open in browser
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + username));
        } finally {
            // go to twitter account page
            if (twitterIntent != null) {
                twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(twitterIntent);
            }
        }
    }
}
