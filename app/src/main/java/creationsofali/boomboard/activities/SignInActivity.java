package creationsofali.boomboard.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import creationsofali.boomboard.R;
import creationsofali.boomboard.datamodels.RequestCode;
import creationsofali.boomboard.helpers.NetworkHelper;
import dmax.dialog.SpotsDialog;

public class SignInActivity extends AppCompatActivity {

    Button buttonSignIn;
    SpotsDialog signInWaitDialog;
    SpotsDialog buildSignInDialog;

    GoogleApiClient googleApiClient;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setTypeface(Typeface.createFromAsset(
                getResources().getAssets(), "fonts/hind-regular.ttf"));

        // show progress dialog while signing in
        signInWaitDialog = new SpotsDialog(SignInActivity.this, R.style.SignInDialogStyle);
        signInWaitDialog.setCancelable(false);

        buildSignInDialog = new SpotsDialog(SignInActivity.this, R.style.BuildSignInDialogStyle);
        buildSignInDialog.setCancelable(false);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.RC_SIGN_IN) {
            // if (buildSignInDialog.isShowing())
            buildSignInDialog.dismiss();

            // sign in with google sign in api
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()) {
                GoogleSignInAccount account = signInResult.getSignInAccount();
                signInWaitDialog.show();
                firebaseAuthWithGoogleSignInApi(account);
                Log.d(TAG, "onActivityResult:signInResult::isSuccess");
            } else {
                // sign in failed
                showSnackbar(getString(R.string.sorry_error_try_again));
            }
        }
    }

    public void showSignInDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_dialog_sign_in, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignInActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.cardButtonUsingGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                // sign in with google
                buildSignInDialog.show();
                startActivityForResult(getGoogleAuthIntent(), RequestCode.RC_SIGN_IN);
            }
        });

        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private Intent getGoogleAuthIntent() {
        // signInIntent
        return Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClientInstance());
    }

    private GoogleApiClient getGoogleApiClientInstance() {
        if (googleApiClient == null) {
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    // .requestProfile()
                    //  .requestId()
                    .requestIdToken(getString(R.string.web_client_id))
                    .build();

            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    //.enableAutoManage()
                    .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                    .build();
            Log.d(TAG, "getGoogleApiClientInstance: create new client instance");
        }

        return googleApiClient;
    }

    private void firebaseAuthWithGoogleSignInApi(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                // dismiss progress dialog
                if (signInWaitDialog.isShowing())
                    signInWaitDialog.dismiss();

                // go to main activity
                startActivity(new Intent(SignInActivity.this, UpdateProfileActivity.class));
                // kill activity to clear top
                finish();

                Log.d(TAG, "firebaseAuthWithGoogleSignInApi:onSuccess");
                // onChangeAuthSharedPreference(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "firebaseAuthWithGoogleSignInApi:onFailure::" + e.getMessage());
                // dismiss progress dialog
                if (signInWaitDialog.isShowing())
                    signInWaitDialog.dismiss();

                // check if the error is caused by network problems
                if (!NetworkHelper.isOnline(SignInActivity.this)) {
                    showSnackbar("Device is offline, check internet settings and try again.");
                } else
                    showSnackbar("Failed! " + e.getMessage());
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(
                findViewById(R.id.coordinatorSignIn),
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setDuration(8000).show();
    }


}
