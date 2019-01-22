package com.naveen.itfest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ITQuizTestActivity extends AppCompatActivity {
    private RecyclerView mItQuizlist;
    private DatabaseReference mDatabase;

    private ProgressBar progressBar;
    private TextView ques;
    private EditText ans;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itquiz_test);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        mAuth=FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ItQuiz");
        mDatabase.keepSynced(true);


        mItQuizlist = findViewById(R.id.itquiz_list);
        mItQuizlist.setHasFixedSize(true);
        mItQuizlist.setLayoutManager(new LinearLayoutManager(ITQuizTestActivity.this));


        FirebaseRecyclerAdapter<ItQuiz, ITQuizTestActivity.ItQuizViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItQuiz, ItQuizViewHolder>(
                ItQuiz.class,
                R.layout.activity_it_quiz_test_post_row,
                ITQuizTestActivity.ItQuizViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(ItQuizViewHolder viewHolder, ItQuiz model, int position) {



                viewHolder.setQuestion(model.getQuestion());
                viewHolder.setOption1(model.getOption1());
                viewHolder.setOption2(model.getOption2());
                viewHolder.setOption3(model.getOption3());
                viewHolder.setOption4(model.getOption4());

                progressBar.setVisibility(View.INVISIBLE);



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        DatabaseReference Database=FirebaseDatabase.getInstance().getReference().child("ItQuizTestAnswers");


                        ques=findViewById(R.id.post_question);
                        ans=findViewById(R.id.post_answer);


                        String answer = ans.getText().toString().trim();
                        String question=ques.getText().toString().trim();

                        if (!TextUtils.isEmpty(answer)|| !TextUtils.isEmpty(question)) {

                            String user_id = mAuth.getCurrentUser().getUid();
                            String email = mAuth.getCurrentUser().getEmail();
                            DatabaseReference current_user_db = Database.child(user_id);

                            current_user_db.child("category").setValue("ItQuiz");
                            current_user_db.child("User").setValue(email);
                            current_user_db.child("question").setValue(question);
                            current_user_db.child("answer").setValue(answer);

                            Toast.makeText(ITQuizTestActivity.this, "Your answer submitted successfully", Toast.LENGTH_LONG).show();
                            ans.setFocusable(false);

                        }else{

                            Toast.makeText(ITQuizTestActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }




        };


        mItQuizlist.setAdapter(firebaseRecyclerAdapter);

    }


    public static class ItQuizViewHolder extends  RecyclerView.ViewHolder {

        View mView;

        public ItQuizViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }



        public void setQuestion(String question){

            TextView post_ques=mView.findViewById(R.id.post_question);
            post_ques.setText(question);

        }

        public void setOption1(String option1){

            TextView post_option1=mView.findViewById(R.id.post_option1);
            post_option1.setText(option1);

        }

        public void setOption2(String option2){

            TextView post_option2=mView.findViewById(R.id.post_option2);
            post_option2.setText(option2);

        }

        public void setOption3(String option3){

            TextView post_option1=mView.findViewById(R.id.post_option3);
            post_option1.setText(option3);

        }

        public void setOption4(String option4){

            TextView post_option4=mView.findViewById(R.id.post_option4);
            post_option4.setText(option4);

        }




    }
}
