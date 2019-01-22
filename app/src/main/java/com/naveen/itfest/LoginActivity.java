package com.naveen.itfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText email_field;
    private EditText password_field;
    private Button login_button;
    private Button create_account_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //google sign in
    private SignInButton Googlebtn;
    private static final int RC_SIGN_IN=1;
    private GoogleSignInClient mGoogleSignInClient;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#262626")));


        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        };

        Googlebtn=findViewById(R.id.btn_google);

        progressBar=new ProgressDialog(this);
        email_field=findViewById(R.id.login_email);
        password_field=findViewById(R.id.login_password);
        login_button=findViewById(R.id.btn_login);
       // create_account_button=findViewById(R.id.btn_create_account);



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//
//        create_account_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

        Googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLogin();
            }
        });
    }


    @Override
    public void onBackPressed() {

        //when the user clicks back button in login page the app will be exited
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void checkLogin() {

        String email = email_field.getText().toString().trim();
        String password = password_field.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            if (password.length() < 6) {

                Toast.makeText(LoginActivity.this, "Password must contain atleast 6 characters", Toast.LENGTH_LONG).show();
            } else {

                progressBar.setMessage("Loging in....");
                progressBar.show();


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user.getEmail().equals("admin@gmail.com")){

                                progressBar.dismiss();

                                Intent usnIntent = new Intent(LoginActivity.this, MainActivity.class);
                                usnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(usnIntent);

                            }else {
                                progressBar.dismiss();
                                Intent usnIntent = new Intent(LoginActivity.this, CoordinatorActivity.class);
                                usnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(usnIntent);

                            }

                        } else {

                            progressBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Error in login  " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }else{

            Toast.makeText(LoginActivity.this,"Fill all the fields",Toast.LENGTH_LONG).show();
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressBar.setMessage("Signing in....");
        progressBar.show();


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressBar.dismiss();
                Toast.makeText(LoginActivity.this,"Google sign in failed " ,Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            FirebaseUser user = mAuth.getCurrentUser();



                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Sign in failed! check your internet connection",Toast.LENGTH_LONG).show();
                        }

                        // ...

                    }
                });

    }


}

