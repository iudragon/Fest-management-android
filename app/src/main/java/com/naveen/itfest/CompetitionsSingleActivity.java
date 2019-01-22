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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompetitionsSingleActivity extends AppCompatActivity {

    private Button mEventReg;
    private String mEvents_post_key=null;
    private DatabaseReference mDatabase,mDatabasePostKey;
    private TextView mSingleEvent,mSingleVenue,mSingleDate,mSingleEventdesc,mName,mPhone,mBdate;
    private FirebaseAuth mAuth;

    private Button mSingleRemoveBtn,mTestbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions_single);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSingleRemoveBtn = findViewById(R.id.removebtn);

        mSingleEvent = findViewById(R.id.single_event);
        mSingleVenue = findViewById(R.id.single_venue);
        mSingleDate = findViewById(R.id.single_date);
        mSingleEventdesc = findViewById(R.id.single_event_desc);
        mTestbtn = findViewById(R.id.testbtn);
        //Event register

        mName = findViewById(R.id.name);
        mBdate = findViewById(R.id.bdate);
        mPhone = findViewById(R.id.phno);
        mEventReg = findViewById(R.id.event_reg);


        mEventReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventRegister();
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");


        mAuth = FirebaseAuth.getInstance();

        mEvents_post_key = getIntent().getExtras().getString("Events_id");


        mDatabase.child(mEvents_post_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String single_event = (String) dataSnapshot.child("eventname").getValue();
                String single_venue = (String) dataSnapshot.child("venue").getValue();
                String single_date = (String) dataSnapshot.child("date").getValue();
                String single_eventdesc = (String) dataSnapshot.child("desc").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();


                mSingleEvent.setText(single_event);
                mSingleVenue.setText(single_venue);
                mSingleDate.setText(single_date);
                mSingleEventdesc.setText(single_eventdesc);




                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Test");

                databaseReference.child("value").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue().equals("True")){

                            mTestbtn.setVisibility(View.VISIBLE);
                            mTestbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FirebaseUser user_details=FirebaseAuth.getInstance().getCurrentUser();
                                    String uid=user_details.getUid();
                                    DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference().child("CompetitionRegister").child(uid);
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){


                                                if(mSingleEvent.getText().equals("It Quiz")){


                                                    Intent itIntent = new Intent(CompetitionsSingleActivity.this, ITQuizTestActivity.class);
                                                    itIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(itIntent);

                                                }else if(mSingleEvent.getText().equals("Treasure Hunt")){

                                                    Intent treasureIntent = new Intent(CompetitionsSingleActivity.this,TreasureTestActivity.class);
                                                    treasureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(treasureIntent);

                                                }else if(mSingleEvent.getText().equals("Coding And Debugging")){

                                                    Intent codingIntent = new Intent(CompetitionsSingleActivity.this, CodingTestActivity.class);
                                                    codingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(codingIntent);

                                                }

                                            }else{

                                                Toast.makeText(CompetitionsSingleActivity.this, "Please do register to take up the test", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                    mSingleRemoveBtn.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mEvents_post_key).removeValue();
                Toast.makeText(CompetitionsSingleActivity.this, "Post removed", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(CompetitionsSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });


    }



    private void EventRegister() {

        DatabaseReference Database= FirebaseDatabase.getInstance().getReference().child("CompetitionRegister");
        String name_val=mName.getText().toString().trim();
        String bdate_val=mBdate.getText().toString().trim();
        String phone_val=mPhone.getText().toString().trim();

        if(!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(bdate_val) && !TextUtils.isEmpty(phone_val)){

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String uid=user.getUid();
            String email=user.getEmail();

            DatabaseReference current_user_db = Database.child(uid);


            current_user_db.child("name").setValue(name_val);
            current_user_db.child("birth date").setValue(bdate_val);
            current_user_db.child("phone").setValue(phone_val);
            current_user_db.child("Email").setValue(email);
            current_user_db.child("Event name").setValue(mSingleEvent.getText().toString());
            current_user_db.child("Event Date").setValue(mSingleDate.getText().toString());

            Toast.makeText(CompetitionsSingleActivity.this,"Registered successfully",Toast.LENGTH_LONG).show();


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
