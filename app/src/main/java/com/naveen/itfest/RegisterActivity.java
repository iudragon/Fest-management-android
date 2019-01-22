package com.naveen.itfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG ="failed" ;

    private EditText emailField;
    private EditText passwordField;
    private Button login_link;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressBar;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth=FirebaseAuth.getInstance();

        Database= FirebaseDatabase.getInstance().getReference().child("Users");


        progressBar=new ProgressDialog(this);


        emailField = findViewById(R.id.input_email);
        passwordField = findViewById(R.id.input_password);
        registerBtn = findViewById(R.id.btn_signup);
        login_link=findViewById(R.id.btn_login_account);

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
            }
        });


    }

    @Override
    public void onBackPressed() {

        //when the user clicks back button in register page the app will be exited
        moveTaskToBack(true);
    }

    private void startRegister() {


        final String email=emailField.getText().toString().trim();
        String password=passwordField.getText().toString().trim();



        if( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ) {
            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this,"Password should contain atleast 6 characters",Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setMessage("Signing Up....");
                progressBar.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {


                            String user_id = mAuth.getCurrentUser().getUid();
                            String email = mAuth.getCurrentUser().getEmail();
                            DatabaseReference current_user_db = Database.child(user_id);

                            current_user_db.child("Email").setValue(email);
                            progressBar.dismiss();

                            Intent usnIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            usnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(usnIntent);


                        } else {

                            progressBar.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                });
            }
        }else {
            Toast.makeText(RegisterActivity.this,"Fill all the Fields",Toast.LENGTH_SHORT).show();
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
