package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.datamodels.Student;

/**
 * Created by ali on 4/25/17.
 */

public class SharedPreferenceEditor extends AsyncTask<Student, Void, Void> {
    private SharedPreferences.Editor preferencesEditor;
    private SharedPreferences sharedPreferences;
    private Context context;
    private static final String TAG = "SharedPreferenceEditor";

    public SharedPreferenceEditor(Context context) {
        this.context = context;
        preferencesEditor = getEditorInstance();
    }

    @Override
    protected Void doInBackground(Student... params) {
        Student student = params[0];
        String gsonStudent = new Gson().toJson(student);
        preferencesEditor.putString("student", gsonStudent);
        preferencesEditor.commit();

        Log.d(TAG, "doInBackground:student = " + gsonStudent);

        return null;
    }

    private SharedPreferences.Editor getEditorInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.app_name), Context.MODE_PRIVATE);
            Log.d(TAG, "getEditorInstance:created");
        }

        return sharedPreferences.edit();
    }
}
