package creationsofali.boomboard.appfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by ali on 12/29/16.
 */

public class HindBoldTextView extends AppCompatTextView {
    String LOG_TAG = "UbuntuFont";

    public HindBoldTextView(Context context) {
        super(context);
        init();
    }

    public HindBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HindBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        try {
            setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Hind-SemiBold.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
