package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.models.SearchProperty;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchResults extends AppCompatActivity {

    public static String QUERY;
    public static String TAG = SearchResults.class.getSimpleName();
    private Context context = SearchResults.this;
    View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.search_list);

        search();
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
            return true;
        }

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search() {
        String query = getIntent().getStringExtra(QUERY);
        Log.d(TAG, query);

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        RequestParams params = new RequestParams();
        params.put("query", query);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.search(), params, new AsyncHttpResponseHandler() {

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

                Log.e(TAG, String.valueOf(SearchProperty.ITEMS.size()));

                if (SearchProperty.ITEMS.size() > 0) {
                    SearchProperty.ITEMS.clear();
                }

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String address = jsonObject.getString("address");
                        String agentId = jsonObject.getString("agent_id");
                        String agent = jsonObject.getString("agent");
                        String price = jsonObject.getString("price");
                        String currency = jsonObject.getString("currency");
                        String image = jsonObject.getString("image");
                        SearchProperty.addPropertyItem(SearchProperty.createPropertyItem(String.valueOf(id),
                                title, address, agentId, agent, price, currency, image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Do the working from here

                setupRecyclerView((RecyclerView) recyclerView);

                //progressBar.setVisibility(View.INVISIBLE);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        recyclerView.setAdapter(new SearchResults.SimpleItemRecyclerViewAdapter(SearchProperty.ITEMS));
    }

    class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SearchResults.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<SearchProperty.PropertyItem> mValues;

        SimpleItemRecyclerViewAdapter(List<SearchProperty.PropertyItem> items) {
            mValues = items;
        }

        @Override
        public SearchResults.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.property_list_content, parent, false);
            return new SearchResults.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SearchResults.SimpleItemRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).title);
            holder.mAddressView.setText(mValues.get(position).address);
            holder.mAgentView.setText(mValues.get(position).agent);
            holder.mPriceView.setText(mValues.get(position).price);

            double amount = Double.parseDouble(mValues.get(position).price);
            //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
            DecimalFormat formatter = new DecimalFormat("#,###");

            holder.mPriceView.setText(mValues.get(position).currency.toUpperCase() + " " + formatter.format(amount));

            Picasso.with(context)
                    .load(mValues.get(position).image)
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

            holder.mAgentButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AgentProfileActivity.class);
                    intent.putExtra(AgentProfileActivity.ARG_AGENT_ID, mValues.get(position).agentId);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            final TextView mTitleView;
            final TextView mAddressView;
            final TextView mAgentView;
            final TextView mPriceView;
            final ImageView mImageView;
            final Button mAgentButton;
            public SearchProperty.PropertyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = view.findViewById(R.id.title);
                mAddressView = view.findViewById(R.id.address);
                mAgentView = view.findViewById(R.id.agent);
                mPriceView = view.findViewById(R.id.price);
                mImageView = view.findViewById(R.id.imageView);
                mAgentButton = view.findViewById(R.id.agent_button);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }


}
