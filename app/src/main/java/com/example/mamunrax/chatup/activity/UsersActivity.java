package com.example.mamunrax.chatup.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mamunrax.chatup.R;
import com.example.mamunrax.chatup.model.All_Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserList;

    private Typeface myFont;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        myFont = Typeface.createFromAsset(this.getAssets(),"fonts/SourceSansPro-Semibold.otf");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = findViewById(R.id.user_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //----Create toolbar back arrow button and color------
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mUserList = findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<All_Users, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<All_Users, UserViewHolder>(

                All_Users.class,
                R.layout.users_single_layout,
                UserViewHolder.class,
                mUserDatabase

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, All_Users users, int position) {

                userViewHolder.setDisplayName(users.getName());
                userViewHolder.setDisplayStatus(users.getStatus());
                userViewHolder.setDisplayImage(users.getThumb_image(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                userViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };

        mUserList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String name){
            TextView mUserName;
            mUserName = mView.findViewById(R.id.userSingelName);
            mUserName.setText(name);

        }
        public void setDisplayStatus(String status){
            TextView mUserStatus;
            mUserStatus = mView.findViewById(R.id.userStatus);
            mUserStatus.setText(status);

        }
        public void setDisplayImage(String image, Context ctx){
            CircleImageView mUserImageView;
            mUserImageView = mView.findViewById(R.id.userSingeImage);
            Picasso.with(ctx).load(image).placeholder(R.drawable.defaultimg).into(mUserImageView);

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
