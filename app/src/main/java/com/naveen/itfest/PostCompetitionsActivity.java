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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.util.jar.Pack200.Unpacker.FALSE;
import static java.util.jar.Pack200.Unpacker.TRUE;

public class PostCompetitionsActivity extends AppCompatActivity {
    private EditText mEvent;
    private EditText mVenue;
    private EditText mDate;
    private FirebaseAuth mAuth;

    private EditText getmEventDesc,cname,cpass;

//    String[] pr;
//    public String strpr="TRUE";

    private DatabaseReference mDatabaseReference;



    private Button mSubmitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_competitions);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth=FirebaseAuth.getInstance();
        cname=findViewById(R.id.cname);
        cpass=findViewById(R.id.cpass);

        mEvent=findViewById(R.id.event);
        mVenue=findViewById(R.id.venue);
        mDate=findViewById(R.id.date);
        getmEventDesc=findViewById(R.id.event_desc);
        mSubmitBtn=findViewById(R.id.submitbutton);

//        pr=getResources().getStringArray(R.array.depts);
//        Spinner s1=(Spinner) findViewById(R.id.pr);
//
       mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Events");
//
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pr);
//
//            s1.setAdapter(adapter);
//            s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                    int index = arg0.getSelectedItemPosition();
//                    if (index > 0) {
//                        Toast.makeText(getBaseContext(), "You Have Seleted " + pr[index], Toast.LENGTH_SHORT).show();
//                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        strpr=pr[index];
//                        //eventsPost.child("pr").setValue(pr[index]);
//                    }else{
//
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//
//
//                }
//            });
//






        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startposting();


            }
        });
    }



    private void startposting() {

        final String cname_val=cname.getText().toString().trim();
        String cpass_val=cpass.getText().toString().trim();
        String event_val=mEvent.getText().toString().trim();
        String venue_val=mVenue.getText().toString().trim();
        String date_val=mDate.getText().toString().trim();
        String event_desc_val=getmEventDesc.getText().toString().trim();



        if(!TextUtils.isEmpty(venue_val) && !TextUtils.isEmpty(event_desc_val) && !TextUtils.isEmpty(event_val)  && !TextUtils.isEmpty(date_val)){

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            final String uid=user.getUid();

            final DatabaseReference  eventsPost=mDatabaseReference.push();
            eventsPost.child("eventname").setValue(event_val);
            eventsPost.child("venue").setValue(venue_val);
            eventsPost.child("date").setValue(date_val);
            eventsPost.child("desc").setValue(event_desc_val);
            eventsPost.child("uid").setValue(uid);
            eventsPost.child("co_name").setValue(cname_val);
            eventsPost.child("co_pass").setValue(cpass_val);
          //  eventsPost.child("pr").setValue(strpr);



            mAuth.createUserWithEmailAndPassword(cname_val, cpass_val).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {

                     DatabaseReference   vDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

                        String user_id = mAuth.getCurrentUser().getUid();
                        String email = mAuth.getCurrentUser().getEmail();
                        DatabaseReference current_user_db =vDatabaseReference.child(user_id);

                        current_user_db.child("Email").setValue(email);

                        eventsPost.child("co_ordinator_name").setValue(cname_val);



                    }
                    Toast.makeText(PostCompetitionsActivity.this, "Event posted successfully", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(PostCompetitionsActivity.this, MainActivity.class));

                }

            });
        } else {

            Toast.makeText(PostCompetitionsActivity.this, "Fields should not be blank", Toast.LENGTH_SHORT).show();
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


