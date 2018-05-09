package com.ruraara.ken.enyumbani;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruraara.ken.enyumbani.models.PropertyDetail;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GalleryActivity extends AppCompatActivity {

    private List<String> mOtherImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mOtherImages = PropertyDetail.getOtherImagesGallery();

        ViewPager mPager = (ViewPager) findViewById(R.id.gallery_pager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.gallery_indicator);
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(GalleryActivity.this,
                getSupportFragmentManager(), mOtherImages);
        mPager.setAdapter(mPagerAdapter);
        indicator.setViewPager(mPager);
        mPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        Context c;
        List<String> otherImages;
        private final int NUM_IMAGES;


        public ScreenSlidePagerAdapter(Context c, FragmentManager fm, List<String> otherImages) {
            super(fm);
            this.c = c;
            this.otherImages = otherImages;
            NUM_IMAGES = otherImages.size();
        }

        @Override
        public Fragment getItem(int position) {

            return ScreenSlideGalleryPageFragment.newInstance(c, position, otherImages.get(position));

        }
        @Override
        public int getCount() {
            return NUM_IMAGES;
        }
    }
}
