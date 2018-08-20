package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView createNewAccount, iHaveAlreadyAccount;
    private Typeface myFont;
    private Button onCreateBtn;
    private TextInputEditText mDisplayName;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        createNewAccount = findViewById(R.id.createNewAccount);
        iHaveAlreadyAccount = findViewById(R.id.iHaveAlreadyAccount);
        mDisplayName = (TextInputEditText)findViewById(R.id.userName);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mPassword = (TextInputEditText) findViewById(R.id.password);
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
        progressDialog.setMessage("Please wait reg...");
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
                String displayName = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                register_user(displayName, email, password);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void register_user(String displayName, String email, String password) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Intent newIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(newIntent);
                        finish();

                    } else {

                        Toast.makeText(RegistrationActivity.this, "You Got Some Error", Toast.LENGTH_SHORT).show();

                    }
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(RegistrationActivity.this, "Please Input all Fields", Toast.LENGTH_SHORT).show();
        }

    }

}
