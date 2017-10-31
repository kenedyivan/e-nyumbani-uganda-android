package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import me.relex.circleindicator.CircleIndicator;

public class PropertyDetails extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        indicator.setViewPager(mPager);
        mPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        /*ViewPagerIndicator viewPagerIndicator = findViewById(R.id.view_pager_indicator);

        viewPagerIndicator.setupWithViewPager(mPager);
        viewPagerIndicator.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Indicator","Page scrolled");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        Context c;
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return new ScreenSlidePageFragment();

            //return ScreenSlidePageFragment.newInstance(0, R.drawable.img2);

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ScreenSlidePageFragment.newInstance(0, R.drawable.img2);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ScreenSlidePageFragment.newInstance(1, R.drawable.img3);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ScreenSlidePageFragment.newInstance(2, R.drawable.img4);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
