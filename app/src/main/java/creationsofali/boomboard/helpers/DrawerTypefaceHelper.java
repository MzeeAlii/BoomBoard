package creationsofali.boomboard.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by ali on 5/30/17.
 */

public class DrawerTypefaceHelper {

    private Context context;

    public DrawerTypefaceHelper(Context context) {
        this.context = context;

    }

    public void executeTask(Menu drawerMenu) {
        new DrawerTypefaceSpanTask().execute(drawerMenu);
    }

    private class DrawerTypefaceSpanTask extends AsyncTask<Menu, Void, Void> {
        @Override
        protected Void doInBackground(Menu... menus) {
            Menu drawerMenu = menus[0];
            for (int i = 0; i < drawerMenu.size(); i++) {
                // for each item in the drawer
                setCustomDrawerFonts(drawerMenu.getItem(i));
            }
            return null;
        }
    }

    // custom drawer items fonts
    private void setCustomDrawerFonts(MenuItem item) {
        String fontFamily = "";
        Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Hind-Regular.ttf");

        SpannableString newItemTitle = new SpannableString(item.getTitle());
        newItemTitle.setSpan(
                new DrawerTypefaceSpan(fontFamily, customTypeface),
                0,
                newItemTitle.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        item.setTitle(newItemTitle);
    }


    @SuppressLint("ParcelCreator")
    private class DrawerTypefaceSpan extends TypefaceSpan {

        private Typeface newTypeface;

        DrawerTypefaceSpan(String family, Typeface newTypeface) {
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

}
