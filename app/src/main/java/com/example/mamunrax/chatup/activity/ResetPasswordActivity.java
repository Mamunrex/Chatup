package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText input_email;
    private Button sub_button;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        input_email = findViewById(R.id.input_email);
        sub_button = findViewById(R.id.sub_button);

        //----ProgressDialog-------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Reset Password");
        progressDialog.setMessage("Please wait while we reset !");
        progressDialog.setCanceledOnTouchOutside(false);


        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final String email = input_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)){
                    if (checkConnection(ResetPasswordActivity.this)){

                        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                            Toast.makeText(ResetPasswordActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else {

                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(ResetPasswordActivity.this, "Send e-mail reset password", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        progressDialog.dismiss();

                                    }else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(ResetPasswordActivity.this, "Please Enter Valid Email Address" + errorMessage, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });
                        }

                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "Cannot Sign In. Please check the form and try again.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(ResetPasswordActivity.this, "Please Enter Your Email address", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
