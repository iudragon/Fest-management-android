package com.naveen.itfest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class tabsPager extends FragmentStatePagerAdapter {

    //place tab title here
    String[] titles=new String[]{"Events","Winners","Updates"};

    public tabsPager(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                CompetitionsFragment competitionsFragment =new CompetitionsFragment();
                return competitionsFragment;


            case 1:
                WinnersFragment winnersFragment =new WinnersFragment();
                return winnersFragment;


            case 2:
                UpdatesFragment updatesFragment=new UpdatesFragment();
                return  updatesFragment;




        }




        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

