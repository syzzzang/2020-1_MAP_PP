package edu.skku.map.a2017311456_pp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class myFragmentStateAdapter extends FragmentStateAdapter {
    String name="";
    public myFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, String username) {
        super(fragmentActivity);
        name=username;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("id",name);
        switch(position){

            case 0:

                PersonalFragment personalFragment=new PersonalFragment();
                personalFragment.setArguments(bundle);
                return personalFragment;

            case 1:
                Bundle bundle1=new Bundle();
                bundle1.putString("id",name);
                MondayFragment mondayFragment=new MondayFragment();
                mondayFragment.setArguments(bundle);
                return mondayFragment;
            case 2:
                TuesdayFragment tuesdayFragment=new TuesdayFragment();
                tuesdayFragment.setArguments(bundle);
                return tuesdayFragment;
            case 3:
                WednesdayFragment wednesdayFragment=new WednesdayFragment();
                wednesdayFragment.setArguments(bundle);
                return wednesdayFragment;
            case 4:
                ThursdayFragment thursdayFragment=new ThursdayFragment();
                thursdayFragment.setArguments(bundle);
                return thursdayFragment;
            case 5:
                FridayFragment fridayFragment=new FridayFragment();
                fridayFragment.setArguments(bundle);
                return fridayFragment;
            case 6:
                SaturdayFragment saturdayFragment=new SaturdayFragment();
                saturdayFragment.setArguments(bundle);
                return saturdayFragment;
            case 7:
                SundayFragment sundayFragment=new SundayFragment();
                sundayFragment.setArguments(bundle);
                return sundayFragment;

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 8;
    }
}