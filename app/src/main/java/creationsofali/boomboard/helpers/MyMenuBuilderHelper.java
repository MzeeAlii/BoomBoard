package creationsofali.boomboard.helpers;

import android.util.Log;
import android.view.Menu;
import android.view.Window;

import java.lang.reflect.Method;

/**
 * Created by ali on 5/26/17.
 */

public class MyMenuBuilderHelper {

    private static final String TAG = "MyMenuBuilderHelper";

    public static void setOptionalIconsVisible(int featureId, Menu menu) {
        //  Log.d(TAG, menu.getClass().getSimpleName());

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
//            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method setOptionalIconsVisible = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                setOptionalIconsVisible.setAccessible(true);
                setOptionalIconsVisible.invoke(menu, true);

            } catch (Exception e) {
                Log.d(TAG, "e = " + e.toString() + ", m = " + e.getMessage());

            }
//            }
        }
    }

    public static void setOptionalIconsVisible(Menu menu) {
        //  Log.d(TAG, menu.getClass().getSimpleName());

        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method setOptionalIconsVisible = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    setOptionalIconsVisible.setAccessible(true);
                    setOptionalIconsVisible.invoke(menu, true);

                } catch (Exception e) {
                    Log.d(TAG, "e = " + e.toString() + ", m = " + e.getMessage());

                }
            }
        }
    }
}
