package com.samaras.muvi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samaras.muvi.R;
import com.samaras.muvi.Backend.SpUtil;

public class FirstActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Context context;
    private static final String TAG = "EmailPassword";
    private ProgressDialog pDialog;
    private final Handler handler = new Handler();
    private Class<? extends AppCompatActivity> activityClass = null;
    private Button tryAgainBtn;
    private TextView checkConnectionText;
    private ImageView MuViIcon;
    private TextView MuViText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        final String prefEmail = SpUtil.getPreferenceString(context, "email");
        final String prefPassword = SpUtil.getPreferenceString(context, "password");
        MuViIcon = (ImageView) findViewById(R.id.MuViIcon);
        MuViText = (TextView) findViewById(R.id.MuViText);

        checkConnectionText = (TextView) findViewById(R.id.checkConnection);
        pDialog = new ProgressDialog(FirstActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        tryAgainBtn = (Button) findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    tryAgainBtn.setVisibility(View.INVISIBLE);
                    checkConnectionText.setVisibility(View.INVISIBLE);

                    pDialog.show();
                    startApp(prefEmail, prefPassword);
                } else {
                    tryAgainBtn.setVisibility(View.VISIBLE);
                    checkConnectionText.setVisibility(View.VISIBLE);
                }
            }
        });

        if(isConnected()) {
            startApp(prefEmail, prefPassword);
        } else {
            MuViText.setVisibility(View.INVISIBLE);
            MuViIcon.setVisibility(View.INVISIBLE);
            tryAgainBtn.setVisibility(View.VISIBLE);
            checkConnectionText.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void startApp( String email, String password) {
        if (email != "" && password != "") {
            Log.d(TAG, "signIn:" + email);

            mAuth.signInWithEmailAndPassword(email, password)
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