package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by ali on 5/20/17.
 */

public class PackageInfoHelper {

    private static final String TAG = "PackageInfoHelper";

    public static String getVersionName(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            Log.d(TAG, "versionName:" + packageInfo.versionName);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
}
