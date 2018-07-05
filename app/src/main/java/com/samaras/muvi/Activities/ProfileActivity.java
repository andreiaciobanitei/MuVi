package com.samaras.muvi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int CHOOSE_PICTURE_RESULT = 42 ;
    static final int REQUEST_TAKE_PHOTO  = 1;
    private static Toolbar toolbar;
    private static ProgressDialog pDialog;
    public static EditText userName;
    public static Button changeProfilePictureButton;
    public static Button takeProfilePictureButton;
    public static ImageView imageUploaded;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static Uri newProfilePictureUri;
    public static FirebaseUser user;
    String mCurrentPhotoPath;
    Uri photoURI;
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
    public void updateProfile() {
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
            if(profileUpdates != null) {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: User profile updated. name: " + user.getDisplayName());
                                    pDialog.hide();
                                    Toast.makeText(getApplicationContext(), "Profile updated!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.hide();
                                Toast.makeText(getApplicationContext(), "Something went wrong. Try again!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }
    public void saveProfile () {
        Log.d(TAG, "save profile button pressed.");
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        storeImage(photoURI);
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
        changeProfilePictureButton = (Button) findViewById(R.id.chooseImageBtn);
        takeProfilePictureButton = (Button) findViewById(R.id.takeImageBtn);
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
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), CHOOSE_PICTURE_RESULT);
            }
        });

        takeProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_profile_menu, menu);
        return true;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        if(requestCode == CHOOSE_PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            photoURI = imageUri;
            showImage(photoURI.toString());
            pDialog.hide();
        }
        else {
            if(requestCode == REQUEST_TAKE_PHOTO  && resultCode == RESULT_OK) {
                showImageAndRotate(photoURI.toString());
                pDialog.hide();
            }
            else {
                Toast.makeText(getApplicationContext(), "Something went wrong!",
                        Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }
    }

    public void storeImage(Uri imageUri) {
        StorageReference ref = mStorageRef.child(imageUri.getLastPathSegment());
        ref.putFile(imageUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        newProfilePictureUri = taskSnapshot.getDownloadUrl();
                        updateProfile();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void  showImage(String url) {
        if(url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .centerInside()
                    .fit()
                    .into(imageUploaded);
        }
    }

    private void  showImageAndRotate(String url) {
        if(url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .fit()
                    .rotate(90)
                    .centerInside()
                    .into(imageUploaded);
        }
    }
}
