package com.naveen.itfest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CoordinatorActivity extends AppCompatActivity {
private EditText updates,itqus,itop1,itop2,itop3,itop4;
private Button btnupdates,btnitquiz;

    private Button madd_winners_btn;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        updates=findViewById(R.id.updates);

        btnupdates=findViewById(R.id.btn_updates);

        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Updates");
        madd_winners_btn=findViewById(R.id.add_winners);

        itqus=findViewById(R.id.itqus);
        itop1=findViewById(R.id.itop1);
        itop2=findViewById(R.id.itop2);
        itop3=findViewById(R.id.itop3);
        itop4=findViewById(R.id.itop4);


        btnitquiz=findViewById(R.id.btn_it);

       btnupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postupdates();


            }
        });

        madd_winners_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CoordinatorActivity.this,PostWinnersActivity.class));
            }
        });

        btnitquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postitquiztest();

            }
        });






    }

    private void postitquiztest() {

        String ques_val = itqus.getText().toString().trim();
        String itop1_val=itop1.getText().toString().trim();
        String itop2_val=itop2.getText().toString().trim();
        String itop3_val=itop3.getText().toString().trim();
        String itop4_val=itop4.getText().toString().trim();


        DatabaseReference DatabaseReference=FirebaseDatabase.getInstance().getReference().child("ItQuiz");
        if (!TextUtils.isEmpty(ques_val) || !TextUtils.isEmpty(itop1_val) || !TextUtils.isEmpty(itop2_val) || !TextUtils.isEmpty(itop3_val) || !TextUtils.isEmpty(itop4_val)) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = user.getUid();

            final DatabaseReference itquizPost = DatabaseReference.push();

            itquizPost.child("question").setValue(ques_val);
            itquizPost.child("option1").setValue(itop1_val);
            itquizPost.child("option2").setValue(itop2_val);
            itquizPost.child("option3").setValue(itop3_val);
            itquizPost.child("option4").setValue(itop4_val);


            Toast.makeText(CoordinatorActivity.this, "It quiz test posted successfully", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(CoordinatorActivity.this, "Fields should not be blank", Toast.LENGTH_SHORT).show();
        }


    }

    private void postupdates() {

        String updates_val = updates.getText().toString().trim();


        if (!TextUtils.isEmpty(updates_val)) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = user.getUid();

            final DatabaseReference updatesPost = mDatabaseReference.push();
            updatesPost.child("notification").setValue(updates_val);

            Toast.makeText(CoordinatorActivity.this, "Update posted successfully", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(CoordinatorActivity.this, "Fields should not be blank", Toast.LENGTH_SHORT).show();
        }
    }



}



