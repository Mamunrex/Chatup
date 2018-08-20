package com.example.mamunrax.chatup.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mamunrax.chatup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat up");


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.accoutnSetting){

        }
        if (item.getItemId() == R.id.allUsers){

        }
        if (item.getItemId() == R.id.logOut){

            logout();

        }

        return true;
    }

    private void logout() {
        mAuth.signOut();
        sentToLogin();
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

