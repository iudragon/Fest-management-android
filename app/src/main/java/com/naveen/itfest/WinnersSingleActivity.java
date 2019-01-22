package com.naveen.itfest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WinnersSingleActivity extends AppCompatActivity {

    private String mWinners_Post_key=null;
    private DatabaseReference mDatabase;
    private TextView mSingleWinner;
    private FirebaseAuth mAuth;

    private Button mSingleRemoveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners_single);

       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSingleWinner=findViewById(R.id.experience);

        mSingleRemoveBtn=findViewById(R.id.removebtn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Winners");
        mWinners_Post_key=getIntent().getExtras().getString("winner_id");



        mDatabase.child(mWinners_Post_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                mSingleWinner.setText(post_desc);


                if(mAuth.getCurrentUser().getUid().equals(post_uid)){

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

                mDatabase.child(mWinners_Post_key).removeValue();
                Toast.makeText(WinnersSingleActivity.this, "Post removed", Toast.LENGTH_SHORT).show();

                Intent mainIntent=new Intent(WinnersSingleActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });


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
