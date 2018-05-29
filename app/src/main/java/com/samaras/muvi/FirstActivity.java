package com.samaras.muvi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Context context ;
    private static final String TAG = "EmailPassword";
    private static Class<? extends AppCompatActivity> activityClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        String prefEmail = SpUtil.getPreferenceString(context, "email");
        String prefPassword = SpUtil.getPreferenceString(context, "password");
        if( prefEmail != "" && prefPassword != "") {
            Log.d(TAG, "signIn:" + prefEmail);

            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(prefEmail, prefPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(getApplicationContext(), "Authentication succesful.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent switchIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(switchIntent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // [END_EXCLUDE]
                        }
                    });
            activityClass = MainActivity.class;
        } else {
            activityClass = LoginActivity.class;
        }

        Intent newActivity = new Intent(context, activityClass);
        context.startActivity(newActivity);
    }
}
