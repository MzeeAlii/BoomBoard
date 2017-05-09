package creationsofali.boomboard.appfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ali on 12/29/16.
 */

public class MyCustomAppFont {
    private Context context;
    private final static String LOG_TAG = "MyCustomAppFont";

    public MyCustomAppFont() {

    }

    public MyCustomAppFont(Context context, View view) {
        this.context = context;
        overrideFont(view);
    }


    private void overrideFont(View view) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFont(child);    // recursion
                }

            } else if (view instanceof TextView) {
                // skip other custom fonts .. don't override
                if (view instanceof HindTextView)
                    return; // don't override
                else
                    ((TextView) view).setTypeface(Typeface.createFromAsset(context.getAssets(),
                            "fonts/hind-regular.ttf"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
