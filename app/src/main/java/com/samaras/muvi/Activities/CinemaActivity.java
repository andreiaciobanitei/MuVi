package com.samaras.muvi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.samaras.muvi.Backend.Models.CinemaSchedules;
import com.samaras.muvi.Fragments.FridayFragment;
import com.samaras.muvi.Fragments.MondayFragment;
import com.samaras.muvi.Fragments.SaturdayFragment;
import com.samaras.muvi.Fragments.SundayFragment;
import com.samaras.muvi.Fragments.ThursdayFragment;
import com.samaras.muvi.Fragments.TuesdayFragment;
import com.samaras.muvi.Fragments.WednesdayFragment;
import com.samaras.muvi.R;

import java.util.ArrayList;
import java.util.List;



public class CinemaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static MenuItem item;
    private  static CinemaSchedules cinemaSchedules;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);
        Intent intent = getIntent();
        String cinemaName = intent.getExtras().getString("cinema");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBar(cinemaName);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), CinemaActivity.this);
        adapter.addFragment(new MondayFragment(), "Monday");
        adapter.addFragment(new TuesdayFragment(), "Tuesday");
        adapter.addFragment(new WednesdayFragment(), "Wednesday");
        adapter.addFragment(new ThursdayFragment(), "Thursday");
        adapter.addFragment(new FridayFragment(), "Friday");
        adapter.addFragment(new SaturdayFragment(), "Saturday");
        adapter.addFragment(new SundayFragment(), "Sunday");
        viewPager.setAdapter(adapter);
    }

    public void setActionBar(String name) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            if(name.equals("cinema_city_afi")) {
                actionBar.setTitle("Cinema City Afi Schedule");
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        Context context;

        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return  mFragmentList!= null ? mFragmentList.size() : 0;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}