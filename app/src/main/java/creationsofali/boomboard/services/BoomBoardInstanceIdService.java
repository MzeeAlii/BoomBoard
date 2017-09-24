package creationsofali.boomboard.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import creationsofali.boomboard.R;

/**
 * Created by ali on 6/2/17.
 */

public class BoomBoardInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "InstanceIdService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh:token = " + refreshedToken);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // user signed in
            DatabaseReference profileReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("students")
                    .child(user.getUid())
                    .child("profile");
            // refresh push token
            profileReference.child("token").setValue(refreshedToken);
            Log.d(TAG, "token saved to profile " + refreshedToken);
        } else {
            // user signed out
            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.app_name), MODE_PRIVATE);

            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("pushToken", refreshedToken);
            spEditor.apply();
            Log.d(TAG, "token saved to shared preferences " + refreshedToken);
        }
    }
}
