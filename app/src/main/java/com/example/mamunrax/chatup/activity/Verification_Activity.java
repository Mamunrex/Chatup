package com.example.mamunrax.chatup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verification_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button email_verificationBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        email_verificationBtn = findViewById(R.id.email_verificationBtn);

        //----ProgressDialog-------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verified Account");
        progressDialog.setMessage("Please wait verified message sent your email !");
        progressDialog.setCanceledOnTouchOutside(false);


        email_verificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkConnection(Verification_Activity.this)){

                    sendMessageEmail();

                }else {
                    Toast.makeText(Verification_Activity.this, "Make sure your phone has an active data connection and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void sendMessageEmail() {

        progressDialog.show();

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent completeSendMsg = new Intent(Verification_Activity.this, LoginActivity.class);
                            completeSendMsg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(completeSendMsg);
                            finish();
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(Verification_Activity.this, "Field to sent Message", Toast.LENGTH_SHORT).show();
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
