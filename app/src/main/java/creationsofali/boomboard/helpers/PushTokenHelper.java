package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import creationsofali.boomboard.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ali on 9/24/17.
 */

public class PushTokenHelper {

    private static final String TAG = PushTokenHelper.class.getSimpleName();

    public static String getTokenFromPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name), MODE_PRIVATE);
        // refresh push token
        return sharedPreferences.getString("pushToken", null);
    }

    public static void refreshPushToken(DatabaseReference profileReference, Context context) {
        String token = getTokenFromPreferences(context);
        profileReference.child("pushToken").setValue(token);
        Log.d(TAG, "Token refreshed from SharedPreferences " + token);
    }
}
