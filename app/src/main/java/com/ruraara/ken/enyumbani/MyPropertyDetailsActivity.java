package com.ruraara.ken.enyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ruraara.ken.enyumbani.utils.SharedPropertyEditState;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

public class MyPropertyDetailsActivity extends AppCompatActivity {

    private String TAG = PropertyDetailsActivity.class.getSimpleName();

    public static final String ARG_ITEM_ID = "item_id";
    public static final String REFRESH = "refresh";

    private int rpf;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    private Context context = MyPropertyDetailsActivity.this;

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
    private TextView mAddress;
    private ImageView mMainImage;
    private TextView mDate;
    private TextView mBy;
    private TextView mCompany;
    private View mSeparator;
    public List<String> mOtherImages;
    public List<PropertyDetail.RelatedProperty> mRelatedProperties;
    public RecyclerView recyclerView;


    //Labels
    private TextView mDetailsCrdTitle;
    private TextView mTypeLbl;
    private TextView mStatusLbl;
    private TextView mPriceLbl;


    String itemId;
    String agentId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(MyPropertyDetailsActivity.this);
        int edited = sharedPropertyEditState.getEditStatus();

        if(edited == SharedPropertyEditState.EDITED){

                itemId = sharedPropertyEditState.getId();
                loadDetails();
            Log.e("UpHome","up home button");
            sharedPropertyEditState.clearMYPropertyDetailsFlag();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_property_details);
        SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(MyPropertyDetailsActivity.this);
        sharedPropertyEditState.clearMYPropertyDetailsFlag();

        if(getIntent().getStringExtra(EditPropertyActivity.ARG_ITEM_ID) != null){
            itemId = getIntent().getStringExtra(EditPropertyActivity.ARG_ITEM_ID);
        }

        SessionManager sessionManager = new SessionManager(MyPropertyDetailsActivity.this);
        agentId = sessionManager.getUserID();

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
        mAddress = (TextView) findViewById(R.id.address);
        mMainImage = (ImageView) findViewById(R.id.main_image);
        mDate = (TextView) findViewById(R.id.date);
        mBy = (TextView) findViewById(R.id.by);
        mCompany = (TextView) findViewById(R.id.company);
        mSeparator = findViewById(R.id.separator);


        //Labels
        mDetailsCrdTitle = (TextView) findViewById(R.id.details_crd_title);
        mTypeLbl = (TextView) findViewById(R.id.type_lbl);
        mStatusLbl = (TextView) findViewById(R.id.status_lbl);
        mPriceLbl = (TextView) findViewById(R.id.price_lbl);

        //Sets views invisible
        mDetailsCrdTitle.setVisibility(View.INVISIBLE);
        mSeparator.setVisibility(View.INVISIBLE);
        mTypeLbl.setVisibility(View.INVISIBLE);
        mStatusLbl.setVisibility(View.INVISIBLE);
        mPriceLbl.setVisibility(View.INVISIBLE);
        mPropertyRating.setVisibility(View.INVISIBLE);


        //End of create layout dynamically

        PropertyDetail propertyDetail = new PropertyDetail();
        Log.d("IsLoaded", String.valueOf(propertyDetail.isLoaded()));
        loadDetails();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_property, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            Intent intent = new Intent(MyPropertyDetailsActivity.this, EditPropertyActivity.class);
            intent.putExtra(PropertyDetailsActivity.ARG_ITEM_ID, itemId);
            startActivityForResult(intent, 1);

            return true;
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDetails(){
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        RequestParams params = new RequestParams();
        params.put("id", itemId);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getMyProperty(), params, new AsyncHttpResponseHandler() {

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

                    //More images
                    JSONArray otherImages = new JSONArray(jsonObject.getString("other_images"));


                    propertyDetail = new PropertyDetail(String.valueOf(id),
                            jTitle, jDescrition, jRating, jNoReviews, jAddress,
                            jType, jStatus, jAgentId, jAgent, jPrice, jCurrency, jImage, jCreatedAt,
                            otherImages);




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
                mPagerAdapter = new MyPropertyDetailsActivity.ScreenSlidePagerAdapter(MyPropertyDetailsActivity.this, getSupportFragmentManager(), mOtherImages);
                mPager.setAdapter(mPagerAdapter);
                indicator.setViewPager(mPager);
                mPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());


                if (propertyDetail.getNoOfOtherImages() == 1) {
                    indicator.setVisibility(View.INVISIBLE);
                }

                //Sets view visible
                mDetailsCrdTitle.setVisibility(View.VISIBLE);
                mSeparator.setVisibility(View.VISIBLE);
                mTypeLbl.setVisibility(View.VISIBLE);
                mStatusLbl.setVisibility(View.VISIBLE);
                mPriceLbl.setVisibility(View.VISIBLE);
                mPropertyRating.setVisibility(View.VISIBLE);

                // Setting actionbar title
                getSupportActionBar().setTitle(propertyDetail.title);


                mTitle.setText(propertyDetail.title);
                mDescription.setText(propertyDetail.description);
                mPropertyRating.setRating((float) propertyDetail.rating);
                mNoReviews.setText("( " + propertyDetail.noReviews + " ) Reviews");
                mAddress.setText(propertyDetail.address);
                mType.setText(propertyDetail.type);
                mStatus.setText(propertyDetail.status);

                mDate.setText(propertyDetail.createdAt);
                //mBy.setText("By " + PropertyDetail.PropertyAgent.firstName + " " + PropertyDetail.PropertyAgent.lastName);
                //mCompany.setText("From " + PropertyDetail.PropertyAgent.company);

                double amount = Double.parseDouble(propertyDetail.price);
                //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
                DecimalFormat formatter = new DecimalFormat("#,###");

                mPrice.setText(propertyDetail.currency.toUpperCase() + " " + formatter.format(amount));

                Picasso.with(MyPropertyDetailsActivity.this)
                        .load(propertyDetail.image)
                        .fit()
                        .into(mMainImage);


                mProgressDialog.dismiss();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                mProgressDialog.dismiss();
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                mProgressDialog.dismiss();
                Toast.makeText(context, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {

                if(data.getIntExtra("refresh",45) == 1){
                    if(Objects.equals(data.getStringExtra(EditPropertyActivity.BACK_PRESS), "back_button")){
                        //itemId = data.getStringExtra(EditPropertyActivity.ARG_ITEM_ID);
                        loadDetails();
                        Log.e("Back button","back button");
                        SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(MyPropertyDetailsActivity.this);
                        sharedPropertyEditState.createEditState(SharedPropertyEditState.NOT_EDITED,null);
                    }

                }
            }
        }
    }*/


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

}
