package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import creationsofali.boomboard.R;
import creationsofali.boomboard.datamodels.Constant;

/**
 * Created by ali on 4/25/17.
 */

public class SharedPreferenceReader extends AsyncTask<Void, Void, String> {
    private Context context;
    private static final String TAG = "SharedPreferenceReader";

    public SharedPreferenceReader(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        String student = sharedPreferences.getString("student", null);

        Log.d(TAG, "doInBackground:studentProfile: " + student);

        return student;
    }

    @Override
    protected void onPostExecute(String student) {
        super.onPostExecute(student);
    }
}
