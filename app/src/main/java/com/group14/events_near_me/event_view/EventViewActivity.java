package com.group14.events_near_me.event_view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.group14.events_near_me.R;

import java.util.ArrayList;

/**
 * Created by Ben on 05/03/2018.
 *
 * this activity produces the viewpager to the 3 fragments that make up viewing an event
 */
public class EventViewActivity extends FragmentActivity {
    private ArrayList<Fragment> fragments;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        // get what event to work with from the intent
        eventID = getIntent().getStringExtra("EventID");
        Log.d("MyDebug", eventID);

        fragments = new ArrayList<>();

        // add each of the three fragments to the adapter
        fragments.add(new EventViewDiscussionFragment());
        fragments.add(new EventViewAttendingFragment());
        fragments.add(new EventViewSignUpFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                // this will always be 3 as there are 3 fragments
                return 3;
            }
        };

        ViewPager viewPager = findViewById(R.id.eventViewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    public String getEventID() {
        return eventID;
    }
}
