package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog progressDialog;
    private CircleImageView mDisplayImage;
    private TextView mName, mStatus, countryTextView, workView, genderView, relationshipView, dateOfBirth;

    private static final int GALLERY_PICK = 1;

    //Storage Firebase
    private StorageReference mImageStorage;
    private Bitmap thumb_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = findViewById(R.id.settings_image);
        mName = findViewById(R.id.settingDisplayName);
        mStatus = findViewById(R.id.settingStatus);
        countryTextView = findViewById(R.id.countryTextView);
        workView = findViewById(R.id.workView);
        genderView = findViewById(R.id.genderView);
        relationshipView = findViewById(R.id.relationshipView);
        dateOfBirth = findViewById(R.id.dateOfBirth);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = mCurrentUser.getUid();

        //----ProgressDialog-------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.setMessage("Please wait while we upload image !");
        progressDialog.setCanceledOnTouchOutside(false);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String live_in = dataSnapshot.child("live_in").getValue().toString();
                String work = dataSnapshot.child("work").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String relationships = dataSnapshot.child("relationships").getValue().toString();
                String date_of_birth = dataSnapshot.child("date_of_birth").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
                countryTextView.setText(live_in);
                workView.setText(work);
                genderView.setText(gender);
                relationshipView.setText(relationships);
                dateOfBirth.setText(date_of_birth);

                if (!image.equals("default")){

                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.defaultimg).into(mDisplayImage);
                    //Picasso.with(SettingsActivity.this).load(image).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.defaultimg)).error(getApplicationContext().getResources().getDrawable(R.drawable.defaultimg)).into(mDisplayImage);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void changeStatus(View view) {
        Intent editActivity = new Intent(SettingsActivity.this, Edit_Activity.class);
        startActivity(editActivity);
    }

    public void imageBtn(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT_IMAGE"),GALLERY_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            progressDialog.show();
            Uri imageURI = data.getData();
            CropImage.activity(imageURI)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());

                String currentUserId = mCurrentUser.getUid();

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baso = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100, baso);
                final byte[] thumb_byte = baso.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_image").child(currentUserId + ".jpg");
                final StorageReference thumb_filePat = mImageStorage.child("profile_image").child("thumbs").child(currentUserId + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            final String download_url = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filePat.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map update_hasMap = new HashMap<>();
                                        update_hasMap.put("image", download_url);
                                        update_hasMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this, "Success uploading", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }else {

                                        Toast.makeText(SettingsActivity.this, "Error in uploading thumbnail", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }

                                }
                            });

                        }else {
                            Toast.makeText(SettingsActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newIntent = new Intent(SettingsActivity.this, MainActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newIntent);
        finish();
    }
}
