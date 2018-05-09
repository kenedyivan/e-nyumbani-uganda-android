package com.ruraara.ken.enyumbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ScreenSlideGalleryPageFragment extends Fragment {

    private String title;
    private int page;
    private int img = R.drawable.img2;
    String r;
    private static Context context;
    private static Bundle args;

    // newInstance constructor for creating fragment with arguments
    public static ScreenSlideGalleryPageFragment newInstance(Context c, int page, String image) {
        ScreenSlideGalleryPageFragment fragmentFirst = new ScreenSlideGalleryPageFragment();
        context = c;
        args = new Bundle();
        args.putInt("someInt", page);
        args.putString("img", image);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = getArguments().getString("img");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_screen_gallery_slide_page, container, false);

        //int r = R.drawable.img2;

        ImageView img = view.findViewById(R.id.imageView3);

        Picasso.with(getActivity())
                .load(r)
                .fit()
                .into(img);

        return view;
    }
}
