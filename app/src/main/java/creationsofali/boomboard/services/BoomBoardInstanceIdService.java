package creationsofali.boomboard.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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
        Log.d(TAG, "onTokenRefresh:token =  " + refreshedToken);
    }
}
