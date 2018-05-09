package com.ruraara.ken.enyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.enyumbani.appData.AppData;
import com.ruraara.ken.enyumbani.models.PropertyDetail;
import com.ruraara.ken.enyumbani.sessions.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

public class PropertyDetailsActivity extends AppCompatActivity {

    private String TAG = PropertyDetailsActivity.class.getSimpleName();

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

    private Context context = PropertyDetailsActivity.this;

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
    private TextView mDate;
    private TextView mBy;
    private TextView mCompany;
    private Button mAgentBtn;
    private View mSeparator;
    public List<String> mOtherImages;
    public List<PropertyDetail.RelatedProperty> mRelatedProperties;
    public RecyclerView recyclerView;

    //Interactive top icons
    private ImageView mLikeView;
    private ImageView mRateView;
    private ImageView mShareView;

    //Labels
    private TextView mDetailsCrdTitle;
    private TextView mRelatedCrdTitle;
    private TextView mTypeLbl;
    private TextView mStatusLbl;
    private TextView mPriceLbl;


    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    private List<Data> data;

    String itemId;
    String agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        itemId = getIntent().getStringExtra(ARG_ITEM_ID); // TODO: 12/13/17 uncomment this line after for dynamic property id
        //itemId = "34"; // TODO: 12/13/17 Remove or comment this line after running test on the property details activity

        SessionManager sessionManager = new SessionManager(PropertyDetailsActivity.this);
        agentId = sessionManager.getUserID();

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


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
        mDate = (TextView) findViewById(R.id.date);
        mBy = (TextView) findViewById(R.id.by);
        mCompany = (TextView) findViewById(R.id.company);
        mAgentBtn = (Button) findViewById(R.id.agent_button);
        mSeparator = findViewById(R.id.separator);

        mLikeView = (ImageView) findViewById(R.id.like);
        mRateView = (ImageView) findViewById(R.id.rate);
        mShareView = (ImageView) findViewById(R.id.share);

        //Labels
        mDetailsCrdTitle = (TextView) findViewById(R.id.details_crd_title);
        mRelatedCrdTitle = (TextView) findViewById(R.id.related_crd_title);
        mTypeLbl = (TextView) findViewById(R.id.type_lbl);
        mStatusLbl = (TextView) findViewById(R.id.status_lbl);
        mPriceLbl = (TextView) findViewById(R.id.price_lbl);

        //Sets views invisible
        mLikeView.setVisibility(View.INVISIBLE);
        mRateView.setVisibility(View.INVISIBLE);
        mShareView.setVisibility(View.INVISIBLE);
        mDetailsCrdTitle.setVisibility(View.INVISIBLE);
        mRelatedCrdTitle.setVisibility(View.INVISIBLE);
        mReviewsTitle.setVisibility(View.INVISIBLE);
        mAgentBtn.setVisibility(View.INVISIBLE);
        mSeparator.setVisibility(View.INVISIBLE);
        mTypeLbl.setVisibility(View.INVISIBLE);
        mStatusLbl.setVisibility(View.INVISIBLE);
        mPriceLbl.setVisibility(View.INVISIBLE);
        mPropertyRating.setVisibility(View.INVISIBLE);

        mLikeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addToFavorite();
            }
        });

        mRateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                rateDialog();
            }
        });

        mShareView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String shareLink = "https://www.nyumbaniuganda.com/property-details/"+itemId;
                Log.d("Sharelink",shareLink);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,shareLink ); // TODO: 5/6/18 Retrieve full path from backend server
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this property!");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });


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


        RequestParams params = new RequestParams();
        params.put("property_id", itemId);
        params.put("agent_id", agentId);


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
                    String jAgentId = jsonObject.getString("agent_id");
                    String jAgent = jsonObject.getString("agent");
                    String jPrice = jsonObject.getString("price");
                    String jCurrency = jsonObject.getString("currency");
                    String jImage = jsonObject.getString("main_image");
                    String jCreatedAt = jsonObject.getString("created_at");
                    int jfavorite = jsonObject.getInt("favorite");

                    //FeaturedProperty agent
                    JSONObject agentJsonObject = jsonObject.getJSONObject("agent");
                    String firstName = agentJsonObject.getString("first_name");
                    String lastName = agentJsonObject.getString("last_name");
                    String company = agentJsonObject.getString("company");

                    new PropertyDetail.PropertyAgent(firstName, lastName, company);

                    //More images
                    JSONArray otherImages = new JSONArray(jsonObject.getString("other_images"));

                    //FeaturedProperty reviews
                    JSONArray reviews = new JSONArray(jsonObject.getString("reviews"));

                    JSONArray relatedProperties = new JSONArray(jsonObject.getString("related_properties"));

                    Log.d("Reviews", reviews.toString());
                    //End processign more images

                    Log.d("Related properties: ", relatedProperties.toString());


                    propertyDetail = new PropertyDetail(String.valueOf(id),
                            jTitle, jDescrition, jRating, jNoReviews, jAddress,
                            jType, jStatus, jAgentId, jAgent, jPrice, jCurrency, jImage, jfavorite, jCreatedAt,
                            otherImages, reviews, relatedProperties);

                    Log.e("Rev", propertyDetail.getReviews().toString());


                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Do the working from here

                assert propertyDetail != null;
                mOtherImages = PropertyDetail.getOtherImages();

                Log.d("Other Images", mOtherImages.toString());


                mPager = (ViewPager) findViewById(R.id.pager);
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                mPagerAdapter = new ScreenSlidePagerAdapter(PropertyDetailsActivity.this, getSupportFragmentManager(), mOtherImages);
                mPager.setAdapter(mPagerAdapter);
                indicator.setViewPager(mPager);
                mPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());


                if (propertyDetail.getNoOfOtherImages() == 1) {
                    indicator.setVisibility(View.INVISIBLE);
                }

                //Sets view visible
                mLikeView.setVisibility(View.VISIBLE);
                mRateView.setVisibility(View.VISIBLE);
                mShareView.setVisibility(View.VISIBLE);
                mDetailsCrdTitle.setVisibility(View.VISIBLE);
                mRelatedCrdTitle.setVisibility(View.VISIBLE);
                mReviewsTitle.setVisibility(View.VISIBLE);
                mAgentBtn.setVisibility(View.VISIBLE);
                mSeparator.setVisibility(View.VISIBLE);
                mTypeLbl.setVisibility(View.VISIBLE);
                mStatusLbl.setVisibility(View.VISIBLE);
                mPriceLbl.setVisibility(View.VISIBLE);
                mPropertyRating.setVisibility(View.VISIBLE);

                // Setting actionbar title
                getSupportActionBar().setTitle(propertyDetail.title);

                final PropertyDetail finalPropertyDetail1 = propertyDetail;
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(PropertyDetailsActivity.this, ChatActivity.class);
                        i.putExtra("agentId", finalPropertyDetail1.agentId);
                        i.putExtra("propertyId", itemId);
                        i.putExtra("propertyTitle", finalPropertyDetail1.title);
                        startActivity(i);
                    }
                });

                final PropertyDetail finalPropertyDetail = propertyDetail;
                mAgentBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PropertyDetailsActivity.this, AgentProfileActivity.class);
                        intent.putExtra(AgentProfileActivity.ARG_AGENT_ID, finalPropertyDetail.agentId);
                        context.startActivity(intent);
                    }
                });


                mTitle.setText(propertyDetail.title);
                mDescription.setText(propertyDetail.description);
                mPropertyRating.setRating((float) propertyDetail.rating);
                mNoReviews.setText("( " + propertyDetail.noReviews + " ) Reviews");
                mReviewsTitle.setText("Reviews ( " + propertyDetail.noReviews + " )");
                mAddress.setText(propertyDetail.address);
                mType.setText(propertyDetail.type);
                mStatus.setText(propertyDetail.status);

                mDate.setText(propertyDetail.createdAt);
                mBy.setText("By " + PropertyDetail.PropertyAgent.firstName + " " + PropertyDetail.PropertyAgent.lastName);
                mCompany.setText("From " + PropertyDetail.PropertyAgent.company);

                Log.d("Fav flag: ", String.valueOf(propertyDetail.favorite));
                if (propertyDetail.favorite == 1) {
                    mLikeView.setImageResource(R.drawable.icons8_heart_outline_24_active);
                } else {
                    mLikeView.setImageResource(R.drawable.icons8_heart_24_grey);
                }

                double amount = Double.parseDouble(propertyDetail.price);
                //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
                DecimalFormat formatter = new DecimalFormat("#,###");

                mPrice.setText(propertyDetail.currency.toUpperCase() + " " + formatter.format(amount));

                Picasso.with(PropertyDetailsActivity.this)
                        .load(propertyDetail.image)
                        .fit()
                        .into(mMainImage);

                //Sets up related properties horizontal recycler view
                //data = fill_with_data(); // TODO: 12/13/17 uncomment when testing horizontal recycler view with Data model

                mRelatedProperties = propertyDetail.getRelatedProperties();

                horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);

                horizontalAdapter = new HorizontalAdapter(mRelatedProperties, getApplication());

                //horizontalAdapter=new HorizontalAdapter(data, getApplication());

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PropertyDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
                horizontal_recycler_view.setAdapter(horizontalAdapter);

                //Sets up reviews recycler view
                reviewsRecyclerView(recyclerView);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.property_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(PropertyDetailsActivity.this, TopSettingsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            /*Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);*/
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reviewsRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ReviewsRecyclerViewAdapter(PropertyDetail.reviews, PropertyDetailsActivity.this));
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

            return ScreenSlidePageFragment.newInstance(c, position, otherImages.get(position));

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
            holder.mDate.setText(mValues.get(position).createdAt);

            Picasso.with(mContext)
                    .load(AppData.getAgentsImagesPath() + mValues.get(position).profile_picture)
                    .placeholder(R.drawable.avatar)
                    .into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetailsActivity.class);
                    intent.putExtra(PropertyDetailsActivity.ARG_ITEM_ID, holder.mItem.id);
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
            final TextView mDate;
            PropertyDetail.Review mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mName = view.findViewById(R.id.name);
                mRatingBar = view.findViewById(R.id.reviewRatingBar);
                mContent = view.findViewById(R.id.content);
                mImageView = view.findViewById(R.id.profile_image);
                mDate = view.findViewById(R.id.date);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContent.getText() + "'";
            }
        }
    }


    class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<PropertyDetail.RelatedProperty> horizontalList = Collections.emptyList();
        Context context;


        HorizontalAdapter(List<PropertyDetail.RelatedProperty> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {

            View mView;
            ImageView imageView;
            TextView txtview;
            TextView statusView;
            RatingBar ratingBarView;

            MyViewHolder(View view) {
                super(view);
                mView = view;
                imageView = view.findViewById(R.id.image);
                txtview = view.findViewById(R.id.title);
                statusView = view.findViewById(R.id.status);
                ratingBarView = view.findViewById(R.id.rating);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_properties_content_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            //holder.imageView.setImageResource(horizontalList.get(position).image);

            Log.d("Related det: ", "posi-" + position);
            Log.d("Related image: ", horizontalList.get(position).image);

            Picasso.with(PropertyDetailsActivity.this)
                    .load(horizontalList.get(position).image)
                    .into(holder.imageView);

            holder.txtview.setText(horizontalList.get(position).title);

            holder.statusView.setText(horizontalList.get(position).status);

            holder.ratingBarView.setRating((float) horizontalList.get(position).rating);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetailsActivity.class);
                    intent.putExtra(PropertyDetailsActivity.ARG_ITEM_ID, horizontalList.get(position).id);
                    context.startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data(R.drawable.img1, "Image 1"));
        data.add(new Data(R.drawable.img2, "Image 2"));
        data.add(new Data(R.drawable.img3, "Image 3"));
        data.add(new Data(R.drawable.img1, "Image 1"));
        data.add(new Data(R.drawable.img2, "Image 2"));
        data.add(new Data(R.drawable.img3, "Image 3"));
        data.add(new Data(R.drawable.img1, "Image 1"));
        data.add(new Data(R.drawable.img2, "Image 2"));
        data.add(new Data(R.drawable.img3, "Image 3"));


        return data;
    }

    public class Data {
        public int imageId;
        public String txt;

        Data(int imageId, String text) {

            this.imageId = imageId;
            this.txt = text;
        }
    }

    private void rateDialog() {


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_property_rating, null);

        final RatingBar mRatingBarView = dialogView.findViewById(R.id.ratingBar);
        final EditText mReviewView = dialogView.findViewById(R.id.review);
        final float[] rate = new float[1];

        mRatingBarView.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                rate[0] = rating;
                //Toast.makeText(PropertyDetailsActivity.this,String.valueOf(rating),Toast.LENGTH_SHORT).show();

            }
        });

        mRatingBarView.setNumStars(5);


        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyDetailsActivity.this);
        // Add the buttons

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String review = mReviewView.getText().toString();

                postReview(rate[0], review, itemId, agentId);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });


        builder.setView(dialogView);
        // Set other dialog properties
        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {

            }
        });

        dialog.show();
    }

    private void postReview(float rating, String review, String itemId, String agentId) {
        RequestParams params = new RequestParams();

        params.put("property_id", itemId);
        params.put("agent_id", agentId);
        params.put("rating", rating);
        params.put("review", review);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.reviewProperty(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Review: " + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {


                        int newPropertyRating = jsonObject.getInt("new_rating");
                        int number_of_reviews = jsonObject.getInt("number_of_reviews");


                        JSONObject reviewJsonObject = jsonObject.getJSONObject("rate");

                        String id = reviewJsonObject.getString("id");
                        int rating = reviewJsonObject.getInt("rating");
                        String review = reviewJsonObject.getString("review");
                        String username = reviewJsonObject.getString("username");
                        String profile_picture = reviewJsonObject.getString("profile_picture");
                        String createdAt = reviewJsonObject.getString("created_at");

                        PropertyDetail propertyDetail = new PropertyDetail();

                        propertyDetail.updatePropertyRating(newPropertyRating);
                        mPropertyRating.setRating((float) propertyDetail.rating);
                        mNoReviews.setText("( " + number_of_reviews + " ) Reviews");
                        mReviewsTitle.setText("Reviews ( " + number_of_reviews + " )");

                        propertyDetail.getReviews
                                (new PropertyDetail.Review(id, rating, review, username, profile_picture, createdAt));

                        //Refreshes review recycler view list adapter
                        recyclerView.setAdapter(new ReviewsRecyclerViewAdapter(PropertyDetail.reviews, PropertyDetailsActivity.this));
                        recyclerView.invalidate();

                        Toast.makeText(PropertyDetailsActivity.this, "FeaturedProperty reviewed", Toast.LENGTH_SHORT).show();

                    } else {
                        //Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    /**
     * Posts favorite property to remote server
     */
    private void addToFavorite() {
        RequestParams params = new RequestParams();

        params.put("property_id", itemId);
        params.put("agent_id", agentId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.addToFavorites(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Favorite: " + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int status = jsonObject.getInt("status");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(status));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && status == 1) {
                        mLikeView.setImageResource(R.drawable.icons8_heart_outline_24_active);
                        Toast.makeText(PropertyDetailsActivity.this, "Added to favorites", Toast.LENGTH_LONG).show();

                    } else if (error == 0 && status == 2) {
                        mLikeView.setImageResource(R.drawable.icons8_heart_24_grey);
                        Toast.makeText(PropertyDetailsActivity.this, "Removed from favorites", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


}
