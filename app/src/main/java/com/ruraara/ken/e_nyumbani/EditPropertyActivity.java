package com.ruraara.ken.e_nyumbani;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.utils.SharedPropertyEditState;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static java.security.AccessController.getContext;

public class EditPropertyActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static int EDITED = 0;
    private String TAG = EditPropertyActivity.class.getSimpleName();
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
    private TextView mFileName1;
    private TextView mFileName2;
    private TextView mFileName3;
    private TextView mFileName4;
    private TextView mFileName5;
    private TextView mFileName6;
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

    String viewFileName;

    View view;


    private Button mEditPropertyActivity;

    private long time;

    final String[] type = new String[1];
    final String[] status = new String[1];
    final String[] currency = new String[1];

    public static final String ARG_ITEM_ID = "item_id";
    public static final String BACK_PRESS = "item_id";

    String itemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);


        itemId = getIntent().getStringExtra(ARG_ITEM_ID);

        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        time = System.currentTimeMillis(); //// TODO: 11/30/17 Make session time variable unchanging during various activity life cycles 

        Log.d("Time: ", String.valueOf(time));


        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mPrice = (EditText) findViewById(R.id.price);
        mAddress = (EditText) findViewById(R.id.address);
        mDistrict = (EditText) findViewById(R.id.district);
        mTown = (EditText) findViewById(R.id.town);
        mRegion = (EditText) findViewById(R.id.region);


        //Retrieves remote property details
        retrievePropertyDetails();

        mEditPropertyActivity = (Button) findViewById(R.id.add_property);

        mEditPropertyActivity.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(EditPropertyActivity.this, "Title empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mDescription.getText().toString() != null && !mDescription.getText().toString().isEmpty()) {
                    description = mDescription.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Description empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mPrice.getText().toString() != null && !mPrice.getText().toString().isEmpty()) {
                    price = mPrice.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Price empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mAddress.getText().toString() != null && !mAddress.getText().toString().isEmpty()) {
                    address = mAddress.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Address empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (mDistrict.getText().toString() != null && !mDistrict.getText().toString().isEmpty()) {
                    district = mDistrict.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "District empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (mTown.getText().toString() != null && !mTown.getText().toString().isEmpty()) {
                    town = mTown.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Town is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRegion.getText().toString() != null && !mRegion.getText().toString().isEmpty()) {
                    region = mRegion.getText().toString();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Region is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type[0] != null && !type[0].isEmpty()) {
                    sType = type[0];
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Type not selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (status[0] != null && !status[0].isEmpty()) {
                    sStatus = status[0];
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Region is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currency[0] != null && !currency[0].isEmpty()) {
                    sCurrency = currency[0];
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Region is empty", Toast.LENGTH_SHORT).show();
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

                    saveChanges(title, description, price, address,
                            district, town, region,
                            sType, sStatus, sCurrency, time);

                } else {
                    Toast.makeText(EditPropertyActivity.this, "Cannot read fields", Toast.LENGTH_SHORT).show();
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

        mFileName1 = (TextView) findViewById(R.id.file_name_1);
        mFileName2 = (TextView) findViewById(R.id.file_name_2);
        mFileName3 = (TextView) findViewById(R.id.file_name_3);
        mFileName4 = (TextView) findViewById(R.id.file_name_4);
        mFileName5 = (TextView) findViewById(R.id.file_name_5);
        mFileName6 = (TextView) findViewById(R.id.file_name_6);

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

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_1);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

                performFileSearch();
            }
        });
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 2;

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_2);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

                performFileSearch();
            }
        });
        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 3;

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_3);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

                performFileSearch();
            }
        });
        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 4;

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_4);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

                performFileSearch();
            }
        });
        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 5;

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_5);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

                performFileSearch();
            }
        });
        mImageButton6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                layout = 6;

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_file_name_5);
                TextView item = (TextView) layout.getChildAt(0);
                viewFileName = item.getText().toString();

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
                upload(uri, viewFileName);    //// TODO: 11/30/17 Make image upload direct to cloudinary server from android client, if application server fails
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

    private void upload(Uri uri, String currentImageName) {
        Log.i("Path: ", uri.getPath());

        String str = getImageBase64(uri);

        RequestParams params = new RequestParams(); ///// TODO: 1/8/18 Send flag for setting main property image 
        params.put("encoded_string", str);
        params.put("id", itemId);
        params.put("image", imageName);
        params.put("current_image_name", currentImageName);
        params.put("session_time", time);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout((50 * 1000));
        client.setResponseTimeout((50 * 1000));
        client.post(AppData.updatePhoto(), params, new AsyncHttpResponseHandler() {

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

                SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(EditPropertyActivity.this);
                sharedPropertyEditState.createEditState(SharedPropertyEditState.EDITED, itemId);

                Log.d("Edit state",sharedPropertyEditState.getEditStatus()+" "+sharedPropertyEditState.getId());


                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(EditPropertyActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(EditPropertyActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
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

    private void retrievePropertyDetails() {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(EditPropertyActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        RequestParams params = new RequestParams();
        params.put("id", itemId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.editProperty(), params, new AsyncHttpResponseHandler() {

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

                    String title = jsonObject.getString("title");
                    String description = jsonObject.getString("description");
                    String price = jsonObject.getString("price");
                    String PropertyStatus = jsonObject.getString("status");
                    String PriceCurrency = jsonObject.getString("currency");
                    String address = jsonObject.getString("address");
                    String district = jsonObject.getString("district");
                    String town = jsonObject.getString("town");
                    String region = jsonObject.getString("region");

                    JSONArray typesJsonArray = jsonObject.getJSONArray("types");
                    JSONArray imagesJsonArray = jsonObject.getJSONArray("images");

                    final PropertyType propertyType = new PropertyType();

                    for (int i = 0; i < typesJsonArray.length(); i++) {
                        JSONObject typeJsonObject = typesJsonArray.getJSONObject(i);
                        String id = typeJsonObject.getString("id");
                        String name = typeJsonObject.getString("name");

                        propertyType.propertyTypeList.add(new PropertyType(id, name));
                    }

                    List<ImageView> imgVz = new ArrayList<ImageView>();
                    List<TextView> txtVz = new ArrayList<TextView>();

                    mImageView1.setTag(1);
                    mImageView2.setTag(2);
                    mImageView3.setTag(3);
                    mImageView4.setTag(4);
                    mImageView5.setTag(5);
                    mImageView6.setTag(6);

                    txtVz.add(mFileName1);
                    txtVz.add(mFileName2);
                    txtVz.add(mFileName3);
                    txtVz.add(mFileName4);
                    txtVz.add(mFileName5);
                    txtVz.add(mFileName6);

                    imgVz.add(mImageView1);
                    imgVz.add(mImageView2);
                    imgVz.add(mImageView3);
                    imgVz.add(mImageView4);
                    imgVz.add(mImageView5);
                    imgVz.add(mImageView6);

                    for (int i = 0; i < imagesJsonArray.length(); i++) {
                        JSONObject imageJsonObject = imagesJsonArray.getJSONObject(i);
                        String image = imageJsonObject.getString("image");

                        txtVz.get(i).setText(image);

                        Picasso.with(EditPropertyActivity.this)
                                .load(AppData.getImagesPath() + image)
                                .into(imgVz.get(i));

                    }

                    mTitle.setText(title);
                    mDescription.setText(description);
                    mPrice.setText(price);
                    mAddress.setText(address);
                    mDistrict.setText(district);
                    mTown.setText(town);
                    mRegion.setText(region);

                    mType = (Spinner) findViewById(R.id.type);


                    String[] types = propertyType.typeNames();
                    ArrayAdapter<String> typeSpinner = new ArrayAdapter<String>(EditPropertyActivity.this, android.R.layout.simple_spinner_item, types);
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
                    int statusArr;
                    if (Objects.equals(PropertyStatus, "1")) {
                        statusArr = R.array.property_status;
                    } else {
                        statusArr = R.array.property_status_1;
                    }
                    ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(EditPropertyActivity.this,
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
                    int currencyArr;
                    if (Objects.equals(PriceCurrency, "ugx")) {
                        currencyArr = R.array.currency;
                    } else {
                        currencyArr = R.array.currency_1;
                    }
                    ArrayAdapter<CharSequence> currencyAapter = ArrayAdapter.createFromResource(EditPropertyActivity.this,
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
                Toast.makeText(EditPropertyActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(EditPropertyActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveChanges(String title, String description, String price,
                            String address, String district, String town,
                            String region, String type, String status, String currency, long sessionTime) {

        RequestParams params = new RequestParams();
        params.put("id", itemId);
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
        mProgressDialog = new ProgressDialog(EditPropertyActivity.this);
        mProgressDialog.setMessage("Saving........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.savePropertyChanges(), params, new AsyncHttpResponseHandler() {

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

                //Toast.makeText(EditPropertyActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int status = jsonObject.getInt("status");
                    int error = jsonObject.getInt("error");

                    Log.d("State: ", String.valueOf(status));
                    Log.d("error: ", String.valueOf(error));

                    if (error == 0 && status == 1) {
                        Toast.makeText(EditPropertyActivity.this, "Saved successfully", Toast.LENGTH_LONG).show();

                        //JSONObject propertyChanges = jsonObject.getJSONObject("prop");

                        EDITED = 1;

                        SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(EditPropertyActivity.this);
                        sharedPropertyEditState.createEditState(SharedPropertyEditState.EDITED, itemId);

                        Log.d("Edit state",sharedPropertyEditState.getEditStatus()+" "+sharedPropertyEditState.getId());


                    } else if (error == 1 && status == 0) {
                        Toast.makeText(EditPropertyActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditPropertyActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
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
                mProgressDialog.dismiss();
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(EditPropertyActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(EditPropertyActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class PropertyType {
        private String id;
        private String name;

        List<PropertyType> propertyTypeList = new ArrayList<>();

        PropertyType(String id, String name) {
            this.id = id;
            this.name = name;
        }

        PropertyType() {
        }


        private String[] typeNames() {
            String[] names = new String[propertyTypeList.size()];
            for (int i = 0; i < propertyTypeList.size(); i++) {
                names[i] = propertyTypeList.get(i).name;
            }

            return names;
        }

        private String typeId(int nameIndex) {
            String[] ids = new String[propertyTypeList.size()];
            for (int i = 0; i < propertyTypeList.size(); i++) {
                ids[i] = propertyTypeList.get(i).id;
            }

            return ids[nameIndex];
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent();
        intent.putExtra(MyPropertyDetailsActivity.ARG_ITEM_ID, itemId);
        intent.putExtra(MyPropertyDetailsActivity.REFRESH,1);
        intent.putExtra(BACK_PRESS, "back_button_pressed");
        if(EDITED == 1){
            setResult(RESULT_OK, intent);
        }else{
            setResult(RESULT_CANCELED, intent);
        }*/

        finish();
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
            Intent i = new Intent(EditPropertyActivity.this, TopSettingsActivity.class);
            startActivity(i);
            return true;
        }

        if(id == android.R.id.home){
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

