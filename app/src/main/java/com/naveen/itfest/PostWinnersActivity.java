package com.naveen.itfest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostWinnersActivity extends AppCompatActivity {
private EditText mEventName;
private EditText mWname;
private EditText mWemail;
private EditText mEdate;
private EditText getmEventDesc;

private DatabaseReference mDatabaseReference;



private Button mSubmitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_winners);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mEventName=findViewById(R.id.eventname);
        mWname=findViewById(R.id.wname);
        mWemail=findViewById(R.id.wemail);
        mEdate=findViewById(R.id.eventdate);
        getmEventDesc=findViewById(R.id.eventdesc);
        mSubmitBtn=findViewById(R.id.submitbutton);




        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Winners");


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startposting();

            }
        });




  }



    private void startposting() {

        String eventname_val=mEventName.getText().toString().trim();
        String wname_val=mWname.getText().toString().trim();
        String wemail_val=mWemail.getText().toString().trim();
        String edate_val=mEdate.getText().toString().trim();
        String desc_val=getmEventDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(eventname_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(wname_val) && !TextUtils.isEmpty(wemail_val) && !TextUtils.isEmpty(edate_val)){

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            final String uid=user.getUid();
            DatabaseReference  experiencePost=mDatabaseReference.push();
            experiencePost.child("eventname").setValue(eventname_val);
            experiencePost.child("winnername").setValue(wname_val);
            experiencePost.child("email").setValue(wemail_val);
            experiencePost.child("edate").setValue(edate_val);
            experiencePost.child("desc").setValue(desc_val);
            experiencePost.child("uid").setValue(uid);
           // newPost.child("uid").setValue(FirebaseAuth)

            Toast.makeText(PostWinnersActivity.this,"Winner details added successfully",Toast.LENGTH_LONG).show();

            startActivity(new Intent(PostWinnersActivity.this,MainActivity.class));

        }else{
            Toast.makeText(this, "Fields should not be blank", Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home) {

            finish();
        }


        return super.onOptionsItemSelected(item);

    }
}
