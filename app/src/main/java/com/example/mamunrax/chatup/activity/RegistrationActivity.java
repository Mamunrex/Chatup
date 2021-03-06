package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView createNewAccount, iHaveAlreadyAccount;
    private Typeface myFont;
    private Button onCreateBtn;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextInputEditText mConfPassword;
    private TextInputEditText userName;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();


        createNewAccount = findViewById(R.id.createNewAccount);
        iHaveAlreadyAccount = findViewById(R.id.iHaveAlreadyAccount);
        mEmail = (TextInputEditText)findViewById(R.id.emailAddress);
        mPassword = (TextInputEditText) findViewById(R.id.password);
        mConfPassword = (TextInputEditText) findViewById(R.id.ConfirmPass);
        userName = (TextInputEditText) findViewById(R.id.userName);
        onCreateBtn = findViewById(R.id.signIn_Btn);

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //----Create toolbar back arrow button and color------
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //--- custom font add----
        myFont = Typeface.createFromAsset(this.getAssets(),"fonts/SourceSansPro-Semibold.otf");
        createNewAccount.setTypeface(myFont);
        iHaveAlreadyAccount.setTypeface(myFont);
        onCreateBtn.setTypeface(myFont);

        //----ProgressDialog-------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering User");
        progressDialog.setMessage("Please wait while we create your Account !");
        progressDialog.setCanceledOnTouchOutside(false);


        iHaveAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sentToLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(sentToLogin);
                finish();
            }
        });

        onCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                String cPass = mConfPassword.getText().toString();

                if (checkConnection(RegistrationActivity.this)){
                    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(cPass)) {
                        progressDialog.show();
                        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){

                            Toast.makeText(RegistrationActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else if(pass.length() < 6){

                            Toast.makeText(RegistrationActivity.this, "You must have 6 characters in your password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else {
                            if (pass.equals(cPass)){

                                register_user(username, email, pass);

                            }else {

                                Toast.makeText(RegistrationActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                    }else {
                        Toast.makeText(RegistrationActivity.this, "Please Input all Fields", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(RegistrationActivity.this, "Make sure your phone has an active data connection and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void register_user(final String name, String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("status", "Hi there, I'm using Chat up App.");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("live_in", "none");
                    userMap.put("work", "none");
                    userMap.put("gender", "none");
                    userMap.put("relationships", "none");
                    userMap.put("date_of_birth", "none");


                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                progressDialog.dismiss();
                                Intent newIntent = new Intent(RegistrationActivity.this, Verification_Activity.class);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(newIntent);
                                finish();

                            }
                        }
                    });


                } else {

                    progressDialog.hide();
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(RegistrationActivity.this, "Cannot Sign In. Please check the form and try again."+errorMessage, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    //-----Check Internet Connection Method------
    public static boolean checkConnection(Context context){

        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null){
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }

        return false;
    }

}
