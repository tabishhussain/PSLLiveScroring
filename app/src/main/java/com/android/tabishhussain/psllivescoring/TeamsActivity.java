package com.android.tabishhussain.psllivescoring;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.tabishhussain.psllivescoring.fragments.TeamsFragment;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle fragmentInfo = new Bundle();

        TeamsFragment islamabadFragment = new TeamsFragment();
        fragmentInfo.putString(getString(R.string.key_filename), "islamabad");
        fragmentInfo.putInt(getString(R.string.key_imageIds),
                R.array.islamabad_images);
        islamabadFragment.setArguments(fragmentInfo);

        TeamsFragment karachiFragment = new TeamsFragment();
        fragmentInfo = new Bundle();
        fragmentInfo.putString(getString(R.string.key_filename), "karachi");
        fragmentInfo.putInt(getString(R.string.key_imageIds),
                R.array.karachi_images);
        karachiFragment.setArguments(fragmentInfo);

        TeamsFragment lahoreFragment = new TeamsFragment();
        fragmentInfo = new Bundle();
        fragmentInfo.putString(getString(R.string.key_filename), "lahore");
        fragmentInfo.putInt(getString(R.string.key_imageIds),
                R.array.lahore_images);
        lahoreFragment.setArguments(fragmentInfo);

        TeamsFragment peshawarFragment = new TeamsFragment();
        fragmentInfo = new Bundle();
        fragmentInfo.putString(getString(R.string.key_filename), "peshawar");
        fragmentInfo.putInt(getString(R.string.key_imageIds),
                R.array.peshawar_images);
        peshawarFragment.setArguments(fragmentInfo);

        TeamsFragment quettaFragment = new TeamsFragment();
        fragmentInfo = new Bundle();
        fragmentInfo.putString(getString(R.string.key_filename), "quetta");
        fragmentInfo.putInt(getString(R.string.key_imageIds),
                R.array.quetta_images);
        quettaFragment.setArguments(fragmentInfo);

        adapter.addFragment(islamabadFragment, "ONE");
        adapter.addFragment(karachiFragment, "TWO");
        adapter.addFragment(lahoreFragment, "THREE");
        adapter.addFragment(peshawarFragment, "FOUR");
        adapter.addFragment(quettaFragment, "FIVE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
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
