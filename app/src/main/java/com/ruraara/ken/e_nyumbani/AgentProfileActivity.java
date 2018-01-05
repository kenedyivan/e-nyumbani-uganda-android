package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.classes.AgentProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AgentProfileActivity extends AppCompatActivity {

    private String TAG = AgentProfileActivity.class.getSimpleName();

    public static String ARG_AGENT_ID;

    private String agentId;

    private ImageView mAgentImage;
    private TextView mAgentName;
    private TextView mCompany;
    private TextView mOfficePhone;
    private TextView mMobilePhone;
    private TextView mEmail;
    private Button mAllAgentPropertiesBtn;

    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;

    public List<AgentProfile.AgentProperties> mAgentProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);
        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get agent id from intent
        agentId = getIntent().getStringExtra(ARG_AGENT_ID);
        Log.d("Agent ID", agentId);

        //Initialize views
        mAgentImage = (ImageView) findViewById(R.id.profile_picture);
        mAgentName = (TextView) findViewById(R.id.agent_name);
        mCompany = (TextView) findViewById(R.id.company);
        mOfficePhone = (TextView) findViewById(R.id.office_phone);
        mMobilePhone = (TextView) findViewById(R.id.mobile_phone);
        mEmail = (TextView) findViewById(R.id.email);
        mAllAgentPropertiesBtn = (Button) findViewById(R.id.all_agent_properties);

        mAllAgentPropertiesBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentProfileActivity.this, TabbedAgentPropertiesActivity.class);
                intent.putExtra(TabbedAgentPropertiesActivity.ARG_ITEM_ID, agentId);
                startActivity(intent);
            }
        });


        agentProfile(agentId);

    }

    private void agentProfile(String agentId) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(AgentProfileActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        RequestParams params = new RequestParams();

        params.put("agent_id", agentId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.agentProfile(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
                mProgressDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Agent: " + resp);

                AgentProfile agentDetail = null;

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String image = jsonObject.getString("profile_picture");
                    String firstName = jsonObject.getString("first_name");
                    String lastName = jsonObject.getString("last_name");
                    String company = jsonObject.getString("company");
                    String officePhone = jsonObject.getString("office_phone");
                    String mobilePhone = jsonObject.getString("mobile_phone");
                    String email = jsonObject.getString("email");

                    JSONArray agentProperties = new JSONArray(jsonObject.getString("agent_properties"));

                    agentDetail = new AgentProfile(image, firstName, lastName, company, officePhone,
                            mobilePhone, email,agentProperties);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Picasso.with(AgentProfileActivity.this)
                        .load(AppData.getAgentsImagesPath() + agentDetail.image)
                        .fit()
                        .into(mAgentImage);

                mAgentName.setText(agentDetail.firstName + " " + agentDetail.lastName);
                mCompany.setText(agentDetail.company);
                mOfficePhone.setText(agentDetail.officePhone);
                mMobilePhone.setText(agentDetail.mobilePhone);
                mEmail.setText(agentDetail.email);

                mAgentProperties = agentDetail.getAgentProeperties();

                horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);

                horizontalAdapter = new HorizontalAdapter(mAgentProperties, getApplication());

                //horizontalAdapter=new HorizontalAdapter(data, getApplication());

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AgentProfileActivity.this,
                        LinearLayoutManager.HORIZONTAL, false);
                horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
                horizontal_recycler_view.setAdapter(horizontalAdapter);

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(AgentProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Toast.makeText(AgentProfileActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class HorizontalAdapter extends RecyclerView.Adapter<AgentProfileActivity.HorizontalAdapter.MyViewHolder> {


        List<AgentProfile.AgentProperties> horizontalList = Collections.emptyList();
        Context context;


        HorizontalAdapter(List<AgentProfile.AgentProperties> horizontalList, Context context) {
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
        public AgentProfileActivity.HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_properties_content_row, parent, false);

            return new AgentProfileActivity.HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AgentProfileActivity.HorizontalAdapter.MyViewHolder holder, final int position) {

            //holder.imageView.setImageResource(horizontalList.get(position).image);

            Log.d("Related det: ", "posi-" + position);

            Picasso.with(AgentProfileActivity.this)
                    .load(AppData.getRelatedPropertiesImagesPath() + horizontalList.get(position).image)
                    .into(holder.imageView);

            holder.txtview.setText(horizontalList.get(position).title);

            holder.statusView.setText(horizontalList.get(position).status);

            holder.ratingBarView.setRating((float) horizontalList.get(position).rating);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetails.class);
                    intent.putExtra(PropertyDetails.ARG_ITEM_ID, horizontalList.get(position).id);
                    context.startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }

}
