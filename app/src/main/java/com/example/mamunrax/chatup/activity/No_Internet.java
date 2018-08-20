package com.example.mamunrax.chatup.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;

public class No_Internet extends AppCompatActivity {

    private TextView retryBtn;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,5000);

        retryBtn = findViewById(R.id.retryBtn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection(No_Internet.this)){
                    Intent intent = new Intent(No_Internet.this, MainActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }else {
                    Toast.makeText(No_Internet.this, "Check Your Mobile Data or WiFi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            try {
                if (checkConnection(No_Internet.this)){
                    Intent intent = new Intent(No_Internet.this, MainActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }
            }catch (Exception e){ }
            No_Internet.this.mHandler.postDelayed(m_Runnable, 5000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(m_Runnable);
    }

    //-----check internet connection------
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
