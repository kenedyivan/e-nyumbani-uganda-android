package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.classes.AgentProperty;
import com.ruraara.ken.e_nyumbani.classes.PropertyForRent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TabbedAgentPropertiesActivity extends AppCompatActivity implements
        AgentAllFragment.OnFragmentInteractionListener,
        AgentSaleFragment.OnFragmentInteractionListener,
        AgentRentFragment.OnFragmentInteractionListener {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "name";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String TAG = "Tabbed Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_agent_properties);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(ARG_ITEM_NAME));
        setSupportActionBar(toolbar);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);


        loadAgentProperties();

    }

    private void setupViewPager(ViewPager viewPager, List<AgentProperty.PropertyItem> items) {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ArrayList<AgentProperty.PropertyItem> ap = new ArrayList<>(items);
        ArrayList<AgentProperty.PropertyItem> ssp = new ArrayList<>(AgentProperty.SALE_ITEMS);
        ArrayList<AgentProperty.PropertyItem> rrp = new ArrayList<>(AgentProperty.RENT_ITEMS);

        ArrayList<Human> l = new ArrayList<>();
        l.add(new Human("akena", "kenedy"));
        l.add(new Human("otim", "kevin"));
        l.add(new Human("ojara", "tom"));
        l.add(new Human("okello", "duram"));

        Fragment all = new AgentAllFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("items", ap);
        all.setArguments(bundle);

        Fragment sale = new AgentSaleFragment();
        Bundle saleBundle = new Bundle();
        saleBundle.putSerializable("sale", ssp);
        sale.setArguments(saleBundle);

        Fragment rent = new AgentRentFragment();
        Bundle rentBundle = new Bundle();
        rentBundle.putSerializable("rent", rrp);
        rent.setArguments(rentBundle);


        adapter.addFragment(all, "All");
        adapter.addFragment(sale, "Sale");
        adapter.addFragment(rent, "Rent");

        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_property_agent, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }

    private void loadAgentProperties() {


        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(TabbedAgentPropertiesActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        String item_id = getIntent().getStringExtra(ARG_ITEM_ID);

        RequestParams params = new RequestParams();
        params.put("id", item_id);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getAgentProperties(), params, new AsyncHttpResponseHandler() {

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

                Log.e(TAG, String.valueOf(AgentProperty.ITEMS.size()));
                Log.e(TAG, String.valueOf(AgentProperty.RENT_ITEMS.size()));
                Log.e(TAG, String.valueOf(AgentProperty.SALE_ITEMS.size()));

                if (AgentProperty.ITEMS.size() > 0) {
                    AgentProperty.ITEMS.clear();
                    AgentProperty.RENT_ITEMS.clear();
                    AgentProperty.SALE_ITEMS.clear();
                }

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        int rating = jsonObject.getInt("rating");
                        String address = jsonObject.getString("address");
                        String status = jsonObject.getString("status");
                        String price = jsonObject.getString("price");
                        String currency = jsonObject.getString("currency");
                        String image = jsonObject.getString("image");

                        AgentProperty.addPropertyItem(AgentProperty.createPropertyItem(String.valueOf(id),
                                title, rating, address, price, status, currency, image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setupViewPager(mViewPager, AgentProperty.ITEMS);

                //mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);

                //Do the working from here

                //setupViewPager(mViewPager, AgentProperty.ITEMS);

                //setupRecyclerView((RecyclerView) recyclerView);

                //progressBar.setVisibility(View.INVISIBLE);

                mProgressDialog.dismiss();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(TabbedAgentPropertiesActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(TabbedAgentPropertiesActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static class Human implements Serializable {
        String fn;
        String ln;

        public Human(String fn, String ln) {
            this.fn = fn;
            this.ln = ln;
        }

        @Override
        public String toString() {
            return fn + " " + ln;

        }
    }
}
