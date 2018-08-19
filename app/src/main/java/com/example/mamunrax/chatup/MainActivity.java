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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkConnection(MainActivity.this)){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null){
                sentToLogin();
            }
        }else {
            //Intent noInternet = new Intent(MainActivity.this, No_Internet.class);
            //startActivity(noInternet);
            //finish();
        }
    }

    private void sentToLogin() {

    }


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

