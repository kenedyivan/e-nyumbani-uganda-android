package com.ruraara.ken.e_nyumbani;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.dummy.SearchProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;

public class AddProperty extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private String TAG = AddProperty.class.getSimpleName();
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mImageView4;
    private ImageView mImageView5;
    private ImageView mImageView6;
    private ProgressBar mProgressBar1;
    private ProgressBar mProgressBar2;
    private ProgressBar mProgressBar3;
    private ProgressBar mProgressBar4;
    private ProgressBar mProgressBar5;
    private ProgressBar mProgressBar6;
    private ImageButton mImageButton1;
    private ImageButton mImageButton2;
    private ImageButton mImageButton3;
    private ImageButton mImageButton4;
    private ImageButton mImageButton5;
    private ImageButton mImageButton6;
    private ImageView mCheckMark1;
    private ImageView mCheckMark2;
    private ImageView mCheckMark3;
    private ImageView mCheckMark4;
    private ImageView mCheckMark5;
    private ImageView mCheckMark6;
    private String imageName;
    private int layout;

    /*Other form fields*/
    private EditText mTitle;
    private EditText mDescription;
    private EditText mPrice;
    private EditText mAddress;
    private EditText mDistrict;
    private EditText mTown;
    private EditText mRegion;
    private Spinner mStatus;
    private Spinner mCurrency;
    private Spinner mType;
    /*end of other form fields*/

    private Button mAddProperty;

    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        time = System.currentTimeMillis(); //// TODO: 11/30/17 Make session time variable unchanging during various activity life cycles 

        Log.d("Time: ", String.valueOf(time));


        final String[] type = new String[1];
        final String[] status = new String[1];
        final String[] currency = new String[1];

        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mPrice = (EditText) findViewById(R.id.price);
        mAddress = (EditText) findViewById(R.id.address);
        mDistrict = (EditText) findViewById(R.id.district);
        mTown = (EditText) findViewById(R.id.town);
        mRegion = (EditText) findViewById(R.id.region);

        mStatus = (Spinner) findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.property_status, android.R.layout.simple_spinner_item);
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
        ArrayAdapter<CharSequence> currencyAapter = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item);
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

        mType = (Spinner) findViewById(R.id.type);
        String[] types = {"Apartment", "Condor", "Bungalow", "Mansion"};
        ArrayAdapter<String> typeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        typeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mType.setAdapter(typeSpinner);

        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Type: ", String.valueOf(adapterView.getItemAtPosition(i)));
                type[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAddProperty = (Button) findViewById(R.id.add_property);

        mAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title;
                String description;
                String price;
                String address;
                String district;
                String town;
                String region;
                String sType;
                String sStatus;
                String sCurrency;


                if (mTitle.getText().toString() != null && !mTitle.getText().toString().isEmpty()) {
                    title = mTitle.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Title empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mDescription.getText().toString() != null && !mDescription.getText().toString().isEmpty()) {
                    description = mDescription.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Description empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mPrice.getText().toString() != null && !mPrice.getText().toString().isEmpty()) {
                    price = mPrice.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Price empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mAddress.getText().toString() != null && !mAddress.getText().toString().isEmpty()) {
                    address = mAddress.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Address empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (mDistrict.getText().toString() != null && !mDistrict.getText().toString().isEmpty()) {
                    district = mDistrict.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "District empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (mTown.getText().toString() != null && !mTown.getText().toString().isEmpty()) {
                    town = mTown.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Town is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRegion.getText().toString() != null && !mRegion.getText().toString().isEmpty()) {
                    region = mRegion.getText().toString();
                } else {
                    Toast.makeText(AddProperty.this, "Region is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type[0] != null && !type[0].isEmpty()) {
                    sType = type[0];
                } else {
                    Toast.makeText(AddProperty.this, "Type not selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (status[0] != null && !status[0].isEmpty()) {
                    sStatus = status[0];
                } else {
                    Toast.makeText(AddProperty.this, "Region is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currency[0] != null && !currency[0].isEmpty()) {
                    sCurrency = currency[0];
                } else {
                    Toast.makeText(AddProperty.this, "Region is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                Log.d("Title: ", title);
                Log.d("Desc: ", description);
                Log.d("price: ", price);
                Log.d("add: ", address);
                Log.d("dist: ", district);
                Log.d("town: ", town);
                Log.d("region: ", region);
                Log.d("type: ", sType);
                Log.d("status: ", sStatus);
                Log.d("currency: ", sCurrency);

                if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty()
                        && !address.isEmpty() && !district.isEmpty()
                        && !town.isEmpty() && !region.isEmpty()
                        && !sType.isEmpty() && !sStatus.isEmpty() && !sCurrency.isEmpty()) {

                    uploadForm(title, description, price, address,
                            district, town, region,
                            sType, sStatus, sCurrency, time);

                } else {
                    Toast.makeText(AddProperty.this, "Cannot read fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mImageView1 = (ImageView) findViewById(R.id.image1);
        mImageView2 = (ImageView) findViewById(R.id.image2);
        mImageView3 = (ImageView) findViewById(R.id.image3);
        mImageView4 = (ImageView) findViewById(R.id.image4);
        mImageView5 = (ImageView) findViewById(R.id.image5);
        mImageView6 = (ImageView) findViewById(R.id.image6);

        mCheckMark1 = (ImageView) findViewById(R.id.checkmark1);
        mCheckMark2 = (ImageView) findViewById(R.id.checkmark2);
        mCheckMark3 = (ImageView) findViewById(R.id.checkmark3);
        mCheckMark4 = (ImageView) findViewById(R.id.checkmark4);
        mCheckMark5 = (ImageView) findViewById(R.id.checkmark5);
        mCheckMark6 = (ImageView) findViewById(R.id.checkmark6);

        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        mProgressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
        mProgressBar6 = (ProgressBar) findViewById(R.id.progressBar6);

        mProgressBar1.setVisibility(View.GONE);
        mProgressBar2.setVisibility(View.GONE);
        mProgressBar3.setVisibility(View.GONE);
        mProgressBar4.setVisibility(View.GONE);
        mProgressBar5.setVisibility(View.GONE);
        mProgressBar6.setVisibility(View.GONE);

        mCheckMark1.setVisibility(View.GONE);
        mCheckMark2.setVisibility(View.GONE);
        mCheckMark3.setVisibility(View.GONE);
        mCheckMark4.setVisibility(View.GONE);
        mCheckMark5.setVisibility(View.GONE);
        mCheckMark6.setVisibility(View.GONE);


        mImageButton1 = (ImageButton) findViewById(R.id.pick1);
        mImageButton2 = (ImageButton) findViewById(R.id.pick2);
        mImageButton3 = (ImageButton) findViewById(R.id.pick3);
        mImageButton4 = (ImageButton) findViewById(R.id.pick4);
        mImageButton5 = (ImageButton) findViewById(R.id.pick5);
        mImageButton6 = (ImageButton) findViewById(R.id.pick6);

        mImageButton1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 1;
                performFileSearch();
            }
        });
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 2;
                performFileSearch();
            }
        });
        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 3;
                performFileSearch();
            }
        });
        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 4;
                performFileSearch();
            }
        });
        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 5;
                performFileSearch();
            }
        });
        mImageButton6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 6;
                performFileSearch();
            }
        });

    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            switch (layout) {
                case 1:
                    mImageButton1.setVisibility(View.GONE);
                    break;
                case 2:
                    mImageButton2.setVisibility(View.GONE);
                    break;
                case 3:
                    mImageButton3.setVisibility(View.GONE);
                    break;
                case 4:
                    mImageButton4.setVisibility(View.GONE);
                    break;
                case 5:
                    mImageButton5.setVisibility(View.GONE);
                    break;
                case 6:
                    mImageButton6.setVisibility(View.GONE);
                    break;
            }


            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                //showImage(uri);

                try {
                    Log.i("Stream: ", readTextFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dumpImageMetaData(uri);
                upload(uri);    //// TODO: 11/30/17 Make image upload direct to cloudinary server from android client, if application server fails 
                try {
                    switch (layout) {
                        case 1:
                            mImageView1.setImageBitmap(getBitmapFromUri(uri));
                            break;
                        case 2:
                            mImageView2.setImageBitmap(getBitmapFromUri(uri));
                            break;
                        case 3:
                            mImageView3.setImageBitmap(getBitmapFromUri(uri));
                            break;
                        case 4:
                            mImageView4.setImageBitmap(getBitmapFromUri(uri));
                            break;
                        case 5:
                            mImageView5.setImageBitmap(getBitmapFromUri(uri));
                            break;
                        case 6:
                            mImageView6.setImageBitmap(getBitmapFromUri(uri));
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void dumpImageMetaData(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                imageName = displayName;

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        } finally {
            cursor.close();
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private void upload(Uri uri) {
        Log.i("Path: ", uri.getPath());

        String str = getImageBase64(uri);

        RequestParams params = new RequestParams();
        params.put("encoded_string", str);
        params.put("image", imageName);
        params.put("session_time", time);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout((50 * 1000));
        client.setResponseTimeout((50 * 1000));
        client.post(AppData.uploadPhoto(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
                switch (layout) {
                    case 1:
                        mProgressBar1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mProgressBar2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mProgressBar3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mProgressBar4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        mProgressBar5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        mProgressBar6.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Response: " + resp);


                switch (layout) {
                    case 1:
                        mProgressBar1.setVisibility(View.GONE);
                        mCheckMark1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mProgressBar2.setVisibility(View.GONE);
                        mCheckMark2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mProgressBar3.setVisibility(View.GONE);
                        mCheckMark3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mProgressBar4.setVisibility(View.GONE);
                        mCheckMark4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        mProgressBar5.setVisibility(View.GONE);
                        mCheckMark5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        mProgressBar6.setVisibility(View.GONE);
                        mCheckMark6.setVisibility(View.VISIBLE);
                        break;
                }


                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(AddProperty.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(AddProperty.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Converting Selected Image to Base64Encode String
    private String getImageBase64(Uri selectedImage) {
        Bitmap myImg = null;
        try {
            myImg = decodeUri(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        return android.util.Base64.encodeToString(byte_arr, 0);
    }

    //Reducing Image Size of a selected Image
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 500;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    private void uploadForm(String title, String description, String price,
                            String address, String district, String town,
                            String region, String type, String status, String currency, long sessionTime) {

        RequestParams params = new RequestParams();
        params.put("title", title);
        params.put("description", description);
        params.put("price", price);
        params.put("address", address);
        params.put("district", district);
        params.put("town", town);
        params.put("region", region);
        params.put("type", type);
        params.put("status", status);
        params.put("currency", currency);
        params.put("session_time", sessionTime);

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(AddProperty.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.createProperty(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Response: " + resp);

                //Toast.makeText(AddProperty.this, "Added successfully", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int status = jsonObject.getInt("status");
                    int error = jsonObject.getInt("error");

                    Log.d("State: ", String.valueOf(status));
                    Log.d("error: ", String.valueOf(error));

                    if (error == 0 && status == 1) {
                        Toast.makeText(AddProperty.this, "Added successfully", Toast.LENGTH_LONG).show();

                        //// TODO: 11/30/17 Intent redirect to agents pending properties
                        Intent i = new Intent(AddProperty.this, DrawerActivity.class);
                        i.putExtra("fragment", "myProperties");
                        // Closing all the Activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);

                        finish();

                    } else if (error == 1 && status == 0){
                        Toast.makeText(AddProperty.this, "Failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddProperty.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

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
                Toast.makeText(AddProperty.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(AddProperty.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
