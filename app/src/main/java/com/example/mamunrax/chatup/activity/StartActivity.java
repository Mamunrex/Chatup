package com.example.mamunrax.chatup.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mamunrax.chatup.R;

public class StartActivity extends AppCompatActivity {

    private TextView textView, textView4;
    private Typeface myFont, myFont2;
    private Button haveAccount, needAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textView = findViewById(R.id.textView);
        textView4 = findViewById(R.id.textView4);
        haveAccount = findViewById(R.id.haveAccount);
        needAccount = findViewById(R.id.needAccount);

        myFont = Typeface.createFromAsset(this.getAssets(),"fonts/SourceSansPro-Regular.otf");
        myFont2 = Typeface.createFromAsset(this.getAssets(),"fonts/SourceSansPro-Semibold.otf");
        textView.setTypeface(myFont);
        textView4.setTypeface(myFont);
        haveAccount.setTypeface(myFont2);
        needAccount.setTypeface(myFont2);

    }

    public void haveAccount(View view) {

        Intent goToLoginActivity = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(goToLoginActivity);

    }

    public void needAccount(View view) {

        Intent goToRegActivity = new Intent(StartActivity.this, RegistrationActivity.class);
        startActivity(goToRegActivity);

    }
}
