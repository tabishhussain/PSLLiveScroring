package com.android.tabishhussain.pslInfo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.tabishhussain.pslInfo.fragments.TeamsFragment;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PSL Teams");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
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

        adapter.addFragment(islamabadFragment, " Islamabad \n United");
        adapter.addFragment(karachiFragment, " Karachi \n Kings");
        adapter.addFragment(lahoreFragment, "Lahore \n Qalandars");
        adapter.addFragment(peshawarFragment, " Peshawar \n Zalmi");
        adapter.addFragment(quettaFragment, "Quetta \n Gladiators");
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
