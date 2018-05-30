package com.samaras.muvi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Context context;
    private static final String TAG = "EmailPassword";
    private ProgressDialog pDialog;
    private final Handler handler = new Handler();
    private Class<? extends AppCompatActivity> activityClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        String prefEmail = SpUtil.getPreferenceString(context, "email");
        String prefPassword = SpUtil.getPreferenceString(context, "password");
        if (prefEmail != "" && prefPassword != "") {
            Log.d(TAG, "signIn:" + prefEmail);

            mAuth.signInWithEmailAndPassword(prefEmail, prefPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            activityClass = MainActivity.class;
        } else {
            activityClass = LoginActivity.class;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newActivity = new Intent(context, activityClass);
                context.startActivity(newActivity);
                finish();
            }
        }, 1000);
    }
}