    package com.naveen.itfest;

    import android.content.ActivityNotFoundException;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.FloatingActionButton;
    import android.support.design.widget.TabLayout;
    import android.support.v4.view.ViewPager;
    import android.support.v7.app.AlertDialog;
    import android.view.View;
    import android.support.design.widget.NavigationView;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.auth.api.Auth;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.common.api.ResultCallback;
    import com.google.android.gms.common.api.Status;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class MainActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private GoogleApiClient mGoogleApiClient;
        private TextView headeremail,headername;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //check whether user logged in or not

            mAuth=FirebaseAuth.getInstance();

            mAuthListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if(firebaseAuth.getCurrentUser()==null) {

                        Intent loginIntent=new Intent(MainActivity.this, com.naveen.itfest.LoginActivity.class);

                        //user canot go back by pressing back key
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);

                    }
                }
            };


            FirebaseUser user_details=FirebaseAuth.getInstance().getCurrentUser();






            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



            TabLayout tabLayout=findViewById(R.id.tabs);
            ViewPager viewPager=findViewById(R.id.viewpager);

            //pass fragment as constructor parameter
            tabsPager tabsPager=new tabsPager(getSupportFragmentManager());

            viewPager.setAdapter(tabsPager);
            tabLayout.setupWithViewPager(viewPager);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header=navigationView.getHeaderView(0);

            headeremail=header.findViewById(R.id.headeremail);
            headername=header.findViewById(R.id.headername);

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){

                headername.setText(user.getDisplayName());
                headeremail.setText(user.getEmail());
            }
        }

        @Override
        protected void onStart() {

            mAuth.addAuthStateListener(mAuthListener);


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onStart();



        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                moveTaskToBack(true);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.logout) {


                logout();

            }

            return super.onOptionsItemSelected(item);
        }

        private void logout() {

            //firebase logout

            mAuth.signOut();

            //google signout


            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), com.naveen.itfest.LoginActivity.class);
                            startActivity(i);

                        }
                    });
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_share) {


                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "RNSIT-Placements");
                    String textLink = "\nPlacements - RNSIT\n\nOfficial Androd app for placements portal at RNS Institute of Technology. \n";
                    textLink = textLink + "https://play.google.com/store/apps/details?id="+getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, textLink);
                    startActivity(Intent.createChooser(i, "Share with"));
                } catch(Exception e) {
                    //e.toString();
                }



            } else if (id == R.id.nav_rate) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

