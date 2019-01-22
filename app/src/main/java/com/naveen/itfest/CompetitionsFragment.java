package com.naveen.itfest;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionsFragment extends Fragment {
    private RecyclerView mEventslist;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private Button madd_competition_btn;
    private TextView mLabel;


    public CompetitionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_competitions, container, false);


        progressBar=view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        mLabel=view.findViewById(R.id.label);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatabase.keepSynced(true);
        madd_competition_btn=view.findViewById(R.id.add_competitions);

        //recycler view in fragment_interview
        mEventslist = view.findViewById(R.id.events_list);
        mEventslist.setHasFixedSize(true);
        mEventslist.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));

        FirebaseUser user_details=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user_details.getUid();
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("admin")){


                    madd_competition_btn.setVisibility((View.VISIBLE));
                    madd_competition_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(getContext(),PostCompetitionsActivity.class));
                        }
                    });
                }else{

                    madd_competition_btn.setVisibility(View.INVISIBLE);
                    mLabel.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Competitions,EventsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Competitions, EventsViewHolder>(
                Competitions.class,
                R.layout.competitions_post_row,
                EventsViewHolder.class,
                mDatabase

        ) {


            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, Competitions model, int position) {

                final String Events_post_key= getRef(position).getKey();



                viewHolder.setEventname(model.getEventname());
                viewHolder.setVenue(model.getVenue());
                viewHolder.setDate(model.getDate());
                progressBar.setVisibility(View.INVISIBLE);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleInterviewIntent=new Intent(getContext(),CompetitionsSingleActivity.class);
                        singleInterviewIntent.putExtra("Events_id",Events_post_key);
                        startActivity(singleInterviewIntent);


                    }
                });
            }

        };

       mEventslist.setAdapter(firebaseRecyclerAdapter);

        return  view;

    }


    public static class EventsViewHolder extends  RecyclerView.ViewHolder{

        View mView;
        public EventsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }





        public void setEventname(String event){

            TextView post_events=mView.findViewById(R.id.post_events);
            post_events.setText(event);

        }




        public void setVenue(String venue){

            TextView post_venue=mView.findViewById(R.id.post_venue);
            post_venue.setText(venue);


        }

        public void setDate(String date){

            TextView post_date=mView.findViewById(R.id.post_date);
            post_date.setText(date);


        }


    }


}


