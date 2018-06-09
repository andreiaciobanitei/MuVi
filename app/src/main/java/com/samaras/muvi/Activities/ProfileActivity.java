package com.samaras.muvi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samaras.muvi.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int PICTURE_RESULT = 42 ;
    private static Toolbar toolbar;
    private static ProgressDialog pDialog;
    public static EditText userName;
    public static Button changeProfilePictureButton;
    public static ImageView imageUploaded;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static Uri newProfilePictureUri;
    public static FirebaseUser user;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
             case R.id.save_profile: {
                hideKeyboard(findViewById(android.R.id.content));
                saveProfile();
                break;
            }
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pDialog != null)
            pDialog.dismiss();
    }

    public void saveProfile () {
        Log.d(TAG, "save profile button pressed.");
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        if(user != null) {
            UserProfileChangeRequest profileUpdates;

            if(newProfilePictureUri != null) {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userName.getText().toString().trim())
                        .setPhotoUri(newProfilePictureUri)
                        .build();
            } else {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userName.getText().toString().trim())
                        .build();
            }
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "onComplete: User profile updated. name: " + user.getDisplayName());
                                pDialog.hide();
                                Toast.makeText(getApplicationContext(), "Profile updated!",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.hide();
                            Toast.makeText(getApplicationContext(), "Something went wrong. Try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("profile_pictures");
        newProfilePictureUri = null;
        userName = (EditText) findViewById(R.id.userName);
        imageUploaded = (ImageView) findViewById(R.id.uploadedImage);
        changeProfilePictureButton = (Button) findViewById(R.id.btnImage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userName.setText(user.getDisplayName());
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Profile");
        }

        changeProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_profile_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Uploading image...");
        pDialog.setCancelable(false);
        pDialog.show();
        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            StorageReference ref = mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri)

                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                newProfilePictureUri = taskSnapshot.getDownloadUrl();
                                showImage(newProfilePictureUri.toString());
                                pDialog.hide();

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.hide();
                            Toast.makeText(getApplicationContext(), "Something went wrong!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Something went wrong!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void  showImage(String url) {
        if(url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerInside()
                    .into(imageUploaded);
        }
    }
}
