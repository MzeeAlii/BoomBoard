package creationsofali.boomboard.helpers;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by ali on 3/18/17.
 */

@SuppressLint("ParcelCreator")
public class DrawerTypefaceSpan extends TypefaceSpan {

    private Typeface newTypeface;

    public DrawerTypefaceSpan(String family, Typeface newTypeface) {
        super(family);
        this.newTypeface = newTypeface;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setTypeface(newTypeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        paint.setTypeface(newTypeface);
    }
}
