package com.example.mamunrax.chatup.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mamunrax.chatup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Edit_Activity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog mProgress;
    private Button mSave;
    private EditText userNameET, currentCityET, statusET, workET, show_date;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private Spinner spinner;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);

        mToolbar = findViewById(R.id.edit_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Account Settings");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //----Create toolbar back arrow button and color------
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //----Progress----
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Saving Changes");
        mProgress.setMessage("Please wait while we save the changes");
        mProgress.setCanceledOnTouchOutside(false);

        userNameET = findViewById(R.id.userNameET);
        currentCityET = findViewById(R.id.currentCityET);
        statusET = findViewById(R.id.statusET);
        workET = findViewById(R.id.workET);
        show_date = findViewById(R.id.show_date);
        radioGroup = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinnerEt);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String live_in = dataSnapshot.child("live_in").getValue().toString();
                String work = dataSnapshot.child("work").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String relationships = dataSnapshot.child("relationships").getValue().toString();
                String date_of_birth = dataSnapshot.child("date_of_birth").getValue().toString();

                userNameET.setText(name);
                currentCityET.setText(live_in);
                statusET.setText(status);
                workET.setText(work);
                show_date.setText(date_of_birth);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mSave = findViewById(R.id.saveBtn);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.show();

                String userName = userNameET.getText().toString();
                String currentCity = currentCityET.getText().toString();
                String status = statusET.getText().toString();
                String work = workET.getText().toString();
                String date = show_date.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                final String user_gander = radioButton.getText().toString();
                final String relation = spinner.getSelectedItem().toString();


                mStatusDatabase.child("name").setValue(userName);
                mStatusDatabase.child("live_in").setValue(currentCity);
                mStatusDatabase.child("status").setValue(status);
                mStatusDatabase.child("work").setValue(work);
                mStatusDatabase.child("gender").setValue(user_gander);
                mStatusDatabase.child("relationships").setValue(relation);
                mStatusDatabase.child("date_of_birth").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgress.dismiss();
                            Intent newIntent = new Intent(Edit_Activity.this, SettingsActivity.class);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(newIntent);
                            fileList();
                        }else {
                            Toast.makeText(Edit_Activity.this, "Thre was some error in saving changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        relationshipSpinner();

    }

    private void relationshipSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.relation,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void datePic(View view) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) +" / "+String.valueOf(monthOfYear)
                        +" / "+String.valueOf(year);
                show_date.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();

    }
}
