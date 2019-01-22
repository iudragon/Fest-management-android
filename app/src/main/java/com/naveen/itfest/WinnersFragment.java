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
import android.widget.Toast;

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
public class WinnersFragment extends Fragment {

    private RecyclerView mWinnerslist;
    private DatabaseReference mDatabase;

    private ProgressBar progressBar;
    private TextView mLabel;

    public WinnersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_winners, container, false);

        progressBar=view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        mLabel=view.findViewById(R.id.label);






        mDatabase = FirebaseDatabase.getInstance().getReference().child("Winners");
        mDatabase.keepSynced(true);

        mWinnerslist = view.findViewById(R.id.winners_list);
        mWinnerslist.setHasFixedSize(true);
        mWinnerslist.setLayoutManager(new LinearLayoutManager(getActivity()));




        FirebaseRecyclerAdapter<Winners,ExperienceViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Winners, ExperienceViewHolder>(
                Winners.class,
                R.layout.winners_post_row,
                ExperienceViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(ExperienceViewHolder viewHolder, Winners model, int position) {



                final String winner_post_key=getRef(position).getKey().toString();


                viewHolder.setEventname(model.getEventname());
                viewHolder.setEdate(model.getEdate());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setWinnername(model.getWinnername());
                viewHolder.setDesc(model.getDesc());

                progressBar.setVisibility(View.INVISIBLE);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleExperienceIntent=new Intent(getContext(),WinnersSingleActivity.class);
                        singleExperienceIntent.putExtra("winner_id",winner_post_key);
                        startActivity(singleExperienceIntent);


                    }
                });
            }



        };



        mWinnerslist.setAdapter(firebaseRecyclerAdapter);


        return  view;


    }


    public static class ExperienceViewHolder extends  RecyclerView.ViewHolder{

        View mView;
        public ExperienceViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setEventname(String eventname){

            TextView post_eventname=mView.findViewById(R.id.post_eventname);
            post_eventname.setText(eventname);

        }

        public void setWinnername(String winnername){

            TextView post_winnername=mView.findViewById(R.id.post_wname);
            post_winnername.setText(winnername);

        }

        public void setEmail(String email){

            TextView post_email=mView.findViewById(R.id.post_email);
            post_email.setText(email);

        }


        public void setEdate(String edate){

            TextView post_eventdate=mView.findViewById(R.id.post_eventdate);
            post_eventdate.setText(edate);
        }

        public void setDesc(String desc){

            TextView post_desc=mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);


        }


    }


}
