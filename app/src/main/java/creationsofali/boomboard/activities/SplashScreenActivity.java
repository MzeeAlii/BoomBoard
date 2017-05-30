package creationsofali.boomboard.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import creationsofali.boomboard.R;

/**
 * Created by ali on 5/15/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final String studentProfile = sharedPreferences.getString("student", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (studentProfile != null) {
                    // profile available
                    startActivity(new Intent(
                            SplashScreenActivity.this, MainActivity.class)
                            .putExtra("student", studentProfile));
                } else {
                    // set up profile
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class)
                            .putExtra("isFromSplash", true));
                }
                // finish activity
                finish();
            }
        }, 2000);

    }
}
