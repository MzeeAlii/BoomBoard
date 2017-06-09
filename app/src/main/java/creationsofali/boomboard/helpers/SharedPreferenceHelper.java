package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.datamodels.Student;

/**
 * Created by ali on 6/1/17.
 */

public class SharedPreferenceHelper {

    public static Student getStudentProfile(Context context) {
        Gson gson = new Gson();

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        String gsonStudent = sharedPreferences.getString("student", null);


        if (gsonStudent != null)
            return gson.fromJson(gsonStudent, Student.class);
        else
            return null;
    }
}
