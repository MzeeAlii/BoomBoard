package creationsofali.boomboard.appfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by ali on 12/29/16.
 */

public class HindTextView extends AppCompatTextView {
    String LOG_TAG = "UbuntuFont";

    public HindTextView(Context context) {
        super(context);
        init();
    }

    public HindTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HindTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        try {
            setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/hind-regular.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
