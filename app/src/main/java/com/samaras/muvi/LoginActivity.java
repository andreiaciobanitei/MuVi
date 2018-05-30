package com.samaras.muvi;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    DatabaseReference rf = FirebaseDatabase.getInstance().getReference("users");
    private Context context ;
    private ProgressBar mProgressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        final String  prefEmail = email;
        final String prefPassword = password;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            hideDialog();
                            SpUtil.setPreferenceString(context, "email", prefEmail);
                            SpUtil.setPreferenceString(context, "password", prefPassword);

                        } else {
                            hideDialog();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        final EditText email = (EditText) findViewById(R.id.editTextEmail);
        final EditText parola = (EditText) findViewById(R.id.editTextParola);

        Button buttonCr = (Button) findViewById(R.id.buttonCreateUser);
        Button buttonLg = (Button) findViewById(R.id.buttonLogIn);
        Button buttonRp = (Button) findViewById(R.id.buttonResetPassword);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        setupFirebaseAuth();

        buttonRp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    final String emailRequest = email.getText().toString();
                    if(!emailRequest.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Reset password request sent to " + emailRequest,
                                Toast.LENGTH_LONG).show();
                        mAuth.sendPasswordResetEmail(emailRequest);
                    } else {
                        Toast.makeText(getApplicationContext(), "Insert an email!", Toast.LENGTH_LONG).show();
                    }
                    hideKeyboard(v);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Wrong email" ,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent switchIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(switchIntent);

            }
        });

        buttonLg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDialog();

                    final String stringEmail = email.getText().toString();
                    final String stringParola = parola.getText().toString();
                    signIn(stringEmail, stringParola);
                    hideKeyboard(view);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //check if email is verified
                    if(user.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Toast.makeText(getApplicationContext(), "Authentication succesful.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
