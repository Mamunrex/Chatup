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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView textView7, textView8, forgot_passTxt, needNewAccount;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button login_button;

    private Typeface myFont;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        forgot_passTxt = findViewById(R.id.textView8);
        needNewAccount = findViewById(R.id.needNewAccount);
        login_button = findViewById(R.id.login_button);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mEmail = (TextInputEditText)findViewById(R.id.emailAddress);
        mPassword = (TextInputEditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //----Create toolbar back arrow button and color------
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        myFont = Typeface.createFromAsset(this.getAssets(),"fonts/SourceSansPro-Semibold.otf");
        textView7.setTypeface(myFont);
        textView8.setTypeface(myFont);
        forgot_passTxt.setTypeface(myFont);
        needNewAccount.setTypeface(myFont);

        //----ProgressDialog-------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please wait while we check your credentials...!");
        progressDialog.setCanceledOnTouchOutside(false);

        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sentToReg = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(sentToReg);
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = mEmail.getText().toString();
                String loginPass = mPassword.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    progressDialog.show();

                    if (checkConnection(LoginActivity.this)){
                        mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    Successful();
                                }else {
                                    Toast.makeText(LoginActivity.this, "The Password is invalid", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();

                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please input all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void Successful() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.isEmailVerified()){

            String current_user_id = mAuth.getCurrentUser().getUid();
            String deviceToken = FirebaseInstanceId.getInstance().getToken();

            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();

                }
            });

        }else {
            Toast.makeText(this, "Please check your email for validation link", Toast.LENGTH_SHORT).show();
        }
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

    public void resetPassword(View view) {
        Intent resetPassword = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(resetPassword);
        finish();
    }
}
