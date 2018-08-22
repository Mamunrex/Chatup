package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mamunrax.chatup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileName, getmProfileStatus, getmProfileFriendRequest;
    private Button mProfileButton;

    private DatabaseReference mUserDatabase;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String id = getIntent().getStringExtra("user_id");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        mProfileImage = findViewById(R.id.profile_image);
        mProfileName = findViewById(R.id.profile_displayName);
        getmProfileStatus = findViewById(R.id.profile_stastus);
        getmProfileFriendRequest = findViewById(R.id.profile_totalFriends);
        mProfileButton = findViewById(R.id.profile_send_req_btn);

        //----- Placeholder -------
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Reset Password");
        mProgressDialog.setMessage("Please wait while we reset !");
        mProgressDialog.setCanceledOnTouchOutside(false);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mProgressDialog.show();
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                getmProfileStatus.setText(status);

                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.prf).into(mProfileImage);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
