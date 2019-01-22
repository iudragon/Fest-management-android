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
public class UpdatesFragment extends Fragment {
    private RecyclerView mUpdateslist;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;


    public UpdatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_updates, container, false);

        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Updates");
        mDatabase.keepSynced(true);

        mUpdateslist = view.findViewById(R.id.updates_list);
        mUpdateslist.setHasFixedSize(true);
        mUpdateslist.setLayoutManager(new LinearLayoutManager(getActivity()));


        FirebaseRecyclerAdapter<Updates, UpdatesFragment.UpdatesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Updates, UpdatesViewHolder>(
                Updates.class,
                R.layout.updates_post_row,
                UpdatesFragment.UpdatesViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(UpdatesViewHolder viewHolder, Updates model, int position) {

                viewHolder.setNotification(model.getNotification());


                progressBar.setVisibility(View.INVISIBLE);


            }


        };


        mUpdateslist.setAdapter(firebaseRecyclerAdapter);


        return view;



    }


    public static class UpdatesViewHolder extends  RecyclerView.ViewHolder {

        View mView;

        public UpdatesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }



        public void setNotification(String notification){

            TextView post_updates=mView.findViewById(R.id.post_updates);
            post_updates.setText(notification);

        }


        }
    }




