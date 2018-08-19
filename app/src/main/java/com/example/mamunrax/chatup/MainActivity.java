package com.example.mamunrax.chatup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkConnection(MainActivity.this)){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null){
                sentToLogin();
            }
        }else {
            Intent noInternet = new Intent(MainActivity.this, No_Internet.class);
            startActivity(noInternet);
            finish();
        }
    }

    //------send to start Activity method------
    private void sentToLogin() {
        Intent sentToLogin = new Intent(MainActivity.this, StartActivity.class);
        startActivity(sentToLogin);
        sentToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

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

