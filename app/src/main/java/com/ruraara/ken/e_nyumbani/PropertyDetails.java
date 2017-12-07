package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.classes.PropertyDetail;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

public class PropertyDetails extends AppCompatActivity {

    private String TAG = PropertyDetails.class.getSimpleName();

    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    private Context context = PropertyDetails.this;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private TextView mTitle;
    private TextView mDescription;
    private RatingBar mPropertyRating;
    private TextView mNoReviews;
    private TextView mType;
    private TextView mStatus;
    private TextView mPrice;
    private TextView mReviewsTitle;
    private TextView mAddress;
    private ImageView mMainImage;
    public List<String> mOtherImages;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mPropertyRating = (RatingBar) findViewById(R.id.property_rating);
        mNoReviews = (TextView) findViewById(R.id.no_reviews);
        mType = (TextView) findViewById(R.id.type);
        mStatus = (TextView) findViewById(R.id.status);
        mPrice = (TextView) findViewById(R.id.price);
        mReviewsTitle = (TextView) findViewById(R.id.reviews_title);
        mAddress = (TextView) findViewById(R.id.address);
        mMainImage = (ImageView) findViewById(R.id.main_image);

        recyclerView = (RecyclerView) findViewById(R.id.reviews_list);
        recyclerView.setNestedScrollingEnabled(false);
        assert recyclerView != null;

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

        //Create layout dynamically


        //End of create layout dynamically

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        String item_id = getIntent().getStringExtra(ARG_ITEM_ID);

        RequestParams params = new RequestParams();
        params.put("id", item_id);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getProperty(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
                //progressBar.setVisibility(View.VISIBLE);
                mProgressDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "S: " + resp);



                PropertyDetail propertyDetail = null;



                try {
                    //JSONArray jsonArray = new JSONArray(resp);
                    //for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(resp);
                    int id = jsonObject.getInt("id");
                    String jTitle = jsonObject.getString("title");
                    String jDescrition = jsonObject.getString("description");
                    int jRating = jsonObject.getInt("rating");
                    String jNoReviews = jsonObject.getString("no_reviews");
                    String jAddress = jsonObject.getString("address");
                    String jType = jsonObject.getString("type");
                    String jStatus = jsonObject.getString("status");
                    String jAgent = jsonObject.getString("agent");
                    String jPrice = jsonObject.getString("price");
                    String jCurrency = jsonObject.getString("currency");
                    String jImage = jsonObject.getString("main_image");

                    //More images
                    JSONArray otherImages = new JSONArray(jsonObject.getString("other_images"));

                    //Property reviews
                    JSONArray reviews = new JSONArray(jsonObject.getString("reviews"));

                    Log.d("Reviews", reviews.toString());
                    //End processign more images


                    propertyDetail = new PropertyDetail(String.valueOf(id),
                            jTitle, jDescrition, jRating, jNoReviews, jAddress,
                            jType, jStatus, jAgent, jPrice, jCurrency, jImage, otherImages,reviews);

                    Log.e("Rev", propertyDetail.getReviews().toString());


                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Do the working from here

                assert propertyDetail != null;
                mOtherImages = propertyDetail.getOtherImages();

                Log.d("Other Images", mOtherImages.toString());


                mPager = (ViewPager) findViewById(R.id.pager);
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                mPagerAdapter = new ScreenSlidePagerAdapter(PropertyDetails.this, getSupportFragmentManager(),mOtherImages);
                mPager.setAdapter(mPagerAdapter);
                indicator.setViewPager(mPager);
                mPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());


                if(propertyDetail.getNoOfOtherImages() == 1){
                    indicator.setVisibility(View.INVISIBLE);
                }


                // Setting actionbar title
                getSupportActionBar().setTitle(propertyDetail.title);


                mTitle.setText(propertyDetail.title);
                mDescription.setText(propertyDetail.description);
                mPropertyRating.setRating((float) propertyDetail.rating);
                mNoReviews.setText("( "+propertyDetail.noReviews+" ) Reviews");
                mReviewsTitle.setText("Reviews ( "+propertyDetail.noReviews+" )");
                mAddress.setText(propertyDetail.address);
                mType.setText(propertyDetail.type);
                mStatus.setText(propertyDetail.status);

                double amount = Double.parseDouble(propertyDetail.price);
                //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
                DecimalFormat formatter = new DecimalFormat("#,###");

                mPrice.setText(propertyDetail.currency.toUpperCase()+" "+formatter.format(amount));

                Picasso.with(PropertyDetails.this)
                        .load(AppData.getImagesPath()+propertyDetail.image)
                        .fit()
                        .into(mMainImage);

                setupRecyclerView(recyclerView);


                mProgressDialog.dismiss();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(context, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ReviewsRecyclerViewAdapter(PropertyDetail.reviews,PropertyDetails.this));
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
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
            //return new ScreenSlidePageFragment();

            //return ScreenSlidePageFragment.newInstance(0, R.drawable.img2);

            return ScreenSlidePageFragment.newInstance(c, position, otherImages.get(position));


            /*switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ScreenSlidePageFragment.newInstance(0, R.drawable.img2);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ScreenSlidePageFragment.newInstance(1, R.drawable.img3);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ScreenSlidePageFragment.newInstance(2, R.drawable.img4);
                default:
                    return null;
            }*/
        }

        @Override
        public int getCount() {
            return NUM_IMAGES;
        }
    }

    class ReviewsRecyclerViewAdapter
            extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

        private final List<PropertyDetail.Review> mValues;
        private final Context mContext;

        public ReviewsRecyclerViewAdapter(List<PropertyDetail.Review> reviews, Context c) {
            mValues = reviews;
            mContext = c;
        }

        @Override
        public ReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.property_reviews, parent, false);
            return new ReviewsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReviewsRecyclerViewAdapter.ViewHolder holder, int position) {

            Log.e("RRReviews", mValues.toString());

            holder.mItem = mValues.get(position);
            holder.mName.setText(mValues.get(position).username);
            holder.mRatingBar.setRating((float) mValues.get(position).rating);
            holder.mContent.setText(mValues.get(position).review);

            Picasso.with(mContext)
                    .load(AppData.getAgentsImagesPath()+mValues.get(position).profile_picture)
                    .into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetails.class);
                    intent.putExtra(PropertyDetails.ARG_ITEM_ID, holder.mItem.id);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mName;
            final TextView mContent;
            final ImageView mImageView;
            final RatingBar mRatingBar;
            PropertyDetail.Review mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mName = view.findViewById(R.id.name);
                mRatingBar = view.findViewById(R.id.reviewRatingBar);
                mContent = view.findViewById(R.id.content);
                mImageView = view.findViewById(R.id.profile_image);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContent.getText() + "'";
            }
        }
    }
}
