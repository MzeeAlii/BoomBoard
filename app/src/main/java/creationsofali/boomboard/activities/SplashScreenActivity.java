package creationsofali.boomboard.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import creationsofali.boomboard.R;

/**
 * Created by ali on 5/15/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final String studentProfile = sharedPreferences.getString("student", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (studentProfile != null && firebaseAuth.getCurrentUser() != null) {
                    // profile available, user logged in
                    startActivity(new Intent(
                            SplashScreenActivity.this, MainActivity.class)
                            .putExtra("student", studentProfile));

                } else if (studentProfile == null) {
                    // it's the first time user using the app, set up profile
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class)
                            .putExtra("isFirstTime", true));

                } else if (firebaseAuth.getCurrentUser() == null) {
                    // just sign in, no profile set up needed
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                }
                // finish activity
                finish();
            }
        }, 2000);

    }
}
