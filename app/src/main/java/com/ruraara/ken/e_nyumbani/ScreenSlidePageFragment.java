package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.squareup.picasso.Picasso;


public class ScreenSlidePageFragment extends Fragment {

    private String title;
    private int page;
    private int img = R.drawable.img2;
    String r;
    private static Context context;

    // newInstance constructor for creating fragment with arguments
    public static ScreenSlidePageFragment newInstance(Context c, int page, String image) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        context = c;
        Bundle args = new Bundle();
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
                R.layout.fragment_screen_slide_page, container, false);

        //int r = R.drawable.img2;

        ImageView img = view.findViewById(R.id.imageView3);

        Picasso.with(getActivity())
                .load(AppData.getDetailsImagesPath()+r)
                .fit()
                .into(img);

        Log.d("View pager Image", AppData.getDetailsImagesPath()+r);

        return view;
    }
}
