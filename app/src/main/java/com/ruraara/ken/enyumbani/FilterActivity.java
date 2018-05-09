package com.ruraara.ken.enyumbani;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.enyumbani.appData.AppData;
import com.ruraara.ken.enyumbani.models.PropertyType;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class FilterActivity extends AppCompatActivity {

    final String[] type = new String[1];
    final String[] status = new String[1];
    final String[] currency = new String[1];

    private Spinner mStatus;
    private Spinner mCurrency;
    private Spinner mType;

    private EditText mFromPrice;
    private EditText mToPrice;
    private EditText mAddress;
    private EditText mDistrict;
    private EditText mTown;
    private EditText mRegion;

    public static String FILTER_TAG = "FILTER_TAG";

    private String TAG = FilterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFromPrice = (EditText) findViewById(R.id.from_price);
        mToPrice = (EditText) findViewById(R.id.to_price);
        mAddress = (EditText) findViewById(R.id.address);
        mDistrict = (EditText) findViewById(R.id.district);
        mTown = (EditText) findViewById(R.id.town);
        mRegion = (EditText) findViewById(R.id.region);

        retrievePropertyDetails();

        Button mFilterButton = (Button) findViewById(R.id.filter_property);

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sFromPrice = mFromPrice.getText().toString();
                String sToPrice = mToPrice.getText().toString();
                String sAddress= mAddress.getText().toString();
                String sDistrict = mDistrict.getText().toString();
                String sTown = mTown.getText().toString();
                String sRegion = mRegion.getText().toString();
                String sType = type[0];
                String sStatus = status[0];
                String sCurrency = currency[0];


                String filter = sFromPrice+":"+sToPrice+":"+sAddress+":"+sDistrict+":"
                        +sTown+":"+sRegion+":"+sType+":"+sStatus+":"+sCurrency;

                Intent resultIntent = new Intent();
                resultIntent.putExtra(FILTER_TAG, filter);
                setResult(RESULT_OK, resultIntent);
                finish();
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
            Intent i = new Intent(FilterActivity.this, TopSettingsActivity.class);
            startActivity(i);
            return true;
        }

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrievePropertyDetails() {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(FilterActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getPropertyTypes(), new AsyncHttpResponseHandler() {

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

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Edit property" + resp);

                //Toast.makeText(EditPropertyActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    JSONArray typesJsonArray = jsonObject.getJSONArray("types");

                    final PropertyType propertyType = new PropertyType();

                    for (int i = 0; i < typesJsonArray.length(); i++) {
                        JSONObject typeJsonObject = typesJsonArray.getJSONObject(i);
                        String id = typeJsonObject.getString("id");
                        String name = typeJsonObject.getString("name");

                        propertyType.propertyTypeList.add(new PropertyType(id, name));
                    }


                    mType = (Spinner) findViewById(R.id.type);


                    String[] types = propertyType.typeNames();
                    ArrayAdapter<String> typeSpinner = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, types);
                    typeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    mType.setAdapter(typeSpinner);

                    mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //Log.d("Type: ", String.valueOf(adapterView.getItemAtPosition(i)));
                            Log.d("Type Id", propertyType.typeId(i));
                            type[0] = propertyType.typeId(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    mStatus = (Spinner) findViewById(R.id.status);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    int statusArr = R.array.property_status;
                    ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(FilterActivity.this,
                            statusArr, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    mStatus.setAdapter(statusAdapter);
                    mStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("Status: ", String.valueOf(adapterView.getItemAtPosition(i)));
                            status[0] = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    mCurrency = (Spinner) findViewById(R.id.currency);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    int currencyArr = R.array.currency;
                    ArrayAdapter<CharSequence> currencyAapter = ArrayAdapter.createFromResource(FilterActivity.this,
                            currencyArr, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    currencyAapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    mCurrency.setAdapter(currencyAapter);
                    mCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("Currency: ", String.valueOf(adapterView.getItemAtPosition(i)));
                            currency[0] = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(FilterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(FilterActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
