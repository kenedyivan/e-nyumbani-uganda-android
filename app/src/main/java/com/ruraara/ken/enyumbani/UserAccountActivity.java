package com.ruraara.ken.enyumbani;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.enyumbani.appData.AppData;
import com.ruraara.ken.enyumbani.sessions.SessionManager;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class UserAccountActivity extends AppCompatActivity {

    private String TAG = UserAccountActivity.class.getSimpleName();

    TextView mFirstNameView;
    TextView mLastNameView;
    TextView mUsernameView;
    TextView mUserTypeView;
    TextView mOfficePhoneView;
    TextView mMobilePhoneView;
    TextView mEmailView;
    TextView mCompanyView;
    TextView mPositionView;
    ImageView mImage;

    //Lables
    TextView mFirstNameViewLbl;
    TextView mLastNameViewLbl;
    TextView mUsernameViewLbl;
    TextView mUserTypeViewLbl;
    ImageView mOfficePhoneViewLbl;
    ImageView mMobilePhoneViewLbl;
    ImageView mEmailViewLbl;
    TextView mCompanyViewLbl;
    TextView mPositionViewLbl;
    ImageView mEditProfileIv;
    ImageView mEditContactIv;
    ImageView mEditCompanyIv;
    ImageView mEditImageIv;
    TextView mProfileCrdTitle;
    TextView mContactCrdTitle;
    TextView mCompanyCrdTitle;

    private ProgressBar mProgressBar;

    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;

    private static String IMAGE_URI = "image_uri";

    String imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        setupActionBar();

        mEditProfileIv = (ImageView) findViewById(R.id.pencil_edit_profile);
        mEditContactIv = (ImageView) findViewById(R.id.pencil_edit_contact);
        mEditCompanyIv = (ImageView) findViewById(R.id.pencil_edit_company);
        mEditImageIv = (ImageView) findViewById(R.id.pencil_edit_image);

        //Initialize label view
        mFirstNameViewLbl = (TextView) findViewById(R.id.first_name_lbl);
        mLastNameViewLbl = (TextView) findViewById(R.id.last_name_lbl);
        mUsernameViewLbl = (TextView) findViewById(R.id.username_lbl);
        mUserTypeViewLbl = (TextView) findViewById(R.id.user_type_lbl);
        mOfficePhoneViewLbl = (ImageView) findViewById(R.id.office_phone_lbl);
        mMobilePhoneViewLbl = (ImageView) findViewById(R.id.mobile_phone_lbl);
        mEmailViewLbl = (ImageView) findViewById(R.id.email_lbl);
        mCompanyViewLbl = (TextView) findViewById(R.id.company_lbl);
        mPositionViewLbl = (TextView) findViewById(R.id.position_lbl);

        mProfileCrdTitle = (TextView) findViewById(R.id.profile_crd_title);
        mContactCrdTitle = (TextView) findViewById(R.id.contact_crd_title);
        mCompanyCrdTitle = (TextView) findViewById(R.id.company_crd_title);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        //Set visibility gone
        mProgressBar.setVisibility(View.GONE);
        mFirstNameViewLbl.setVisibility(View.INVISIBLE);
        mLastNameViewLbl.setVisibility(View.INVISIBLE);
        mUsernameViewLbl.setVisibility(View.INVISIBLE);
        mUserTypeViewLbl.setVisibility(View.INVISIBLE);
        mOfficePhoneViewLbl.setVisibility(View.INVISIBLE);
        mMobilePhoneViewLbl.setVisibility(View.INVISIBLE);
        mEmailViewLbl.setVisibility(View.INVISIBLE);
        mCompanyViewLbl.setVisibility(View.INVISIBLE);
        mPositionViewLbl.setVisibility(View.INVISIBLE);
        mEditProfileIv.setVisibility(View.INVISIBLE);
        mEditContactIv.setVisibility(View.INVISIBLE);
        mEditCompanyIv.setVisibility(View.INVISIBLE);
        mEditImageIv.setVisibility(View.INVISIBLE);
        mProfileCrdTitle.setVisibility(View.INVISIBLE);
        mContactCrdTitle.setVisibility(View.INVISIBLE);
        mCompanyCrdTitle.setVisibility(View.INVISIBLE);

        mImage = (ImageView) findViewById(R.id.profile_picture);

        //Profile views
        mFirstNameView = (TextView) findViewById(R.id.ac_first_name);
        mLastNameView = (TextView) findViewById(R.id.ac_last_name);
        mUsernameView = (TextView) findViewById(R.id.ac_username);
        mUserTypeView = (TextView) findViewById(R.id.ac_user_type);

        //Contacts views
        mOfficePhoneView = (TextView) findViewById(R.id.ac_office_phone);
        mMobilePhoneView = (TextView) findViewById(R.id.ac_mobile_phone);
        mEmailView = (TextView) findViewById(R.id.ac_email);

        //Company
        mCompanyView = (TextView) findViewById(R.id.ac_company);
        mPositionView = (TextView) findViewById(R.id.ac_position);

        showAgentDetails();

        mEditImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        //Profile edit pencil
        mEditProfileIv.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String fName = mFirstNameView.getText().toString();
                String lName = mLastNameView.getText().toString();
                String uName = mUsernameView.getText().toString();
                String uType = mUserTypeView.getText().toString();

                editUserProfile(fName, lName, uName, uType);
            }
        });

        //Contact edit pencil
        mEditContactIv.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String oPhone = mOfficePhoneView.getText().toString();
                String mPhone = mMobilePhoneView.getText().toString();
                String email = mEmailView.getText().toString();

                editUserContact(oPhone, mPhone, email);
            }
        });


        //Company edit pencil
        mEditCompanyIv.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String company = mCompanyView.getText().toString();
                String position = mPositionView.getText().toString();

                editUserCompany(company, position);
            }
        });

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            Log.d("Avatar Uri", uri.toString());

            Intent i = new Intent(UserAccountActivity.this,ImageCropperActivity.class);
            i.putExtra(IMAGE_URI,uri.toString());
            startActivityForResult(i,CROP_IMAGE_REQUEST);

        }

        if(requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK){
            Uri croppedImageUri = Uri.parse(data.getExtras().getString(ImageCropperActivity.CROPPED_IMAGE_URI));
            dumpImageMetaData(croppedImageUri);
            uploadProfilePicture(croppedImageUri);
            try {
                mImage.setImageBitmap(getBitmapFromUri(croppedImageUri));
            } catch (IOException e) {
                e.printStackTrace();
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

    private void uploadProfilePicture(Uri uri) {
        Log.i("Path: ", uri.getPath());

        String str = getImageBase64(uri);

        SessionManager sessionManager = new SessionManager(UserAccountActivity.this);

        RequestParams params = new RequestParams();
        params.put("encoded_string", str);
        params.put("image", imageName);
        params.put("id", sessionManager.getUserID());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout((50 * 1000));
        client.setResponseTimeout((50 * 1000));
        client.post(AppData.uploadProfilePicture(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "Response: " + resp);

                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(UserAccountActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(UserAccountActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(UserAccountActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showAgentDetails(){
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(UserAccountActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(UserAccountActivity.this);
        String userId = sessionManager.getUserID();

        Log.d("ID", userId);

        RequestParams params = new RequestParams();

        params.put("id", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.agentAccount(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Account" + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        String firstName = jsonObject.getString("first_name");
                        String lastName = jsonObject.getString("last_name");
                        String userName = jsonObject.getString("username");
                        String userType = jsonObject.getString("user_type");
                        String officePhone = jsonObject.getString("office_phone");
                        String mobilePhone = jsonObject.getString("mobile_phone");
                        String email = jsonObject.getString("email");
                        String sCompany = jsonObject.getString("company");
                        String sPosition = jsonObject.getString("position");
                        String sImage = jsonObject.getString("image");

                        //Sets views visible
                        mFirstNameViewLbl.setVisibility(View.VISIBLE);
                        mLastNameViewLbl.setVisibility(View.VISIBLE);
                        mUsernameViewLbl.setVisibility(View.VISIBLE);
                        mUserTypeViewLbl.setVisibility(View.VISIBLE);
                        mOfficePhoneViewLbl.setVisibility(View.VISIBLE);
                        mMobilePhoneViewLbl.setVisibility(View.VISIBLE);
                        mEmailViewLbl.setVisibility(View.VISIBLE);
                        mCompanyViewLbl.setVisibility(View.VISIBLE);
                        mPositionViewLbl.setVisibility(View.VISIBLE);

                        //Sets edit views visible
                        mEditProfileIv.setVisibility(View.VISIBLE);
                        mEditContactIv.setVisibility(View.VISIBLE);
                        mEditCompanyIv.setVisibility(View.VISIBLE);
                        mEditImageIv.setVisibility(View.VISIBLE);

                        //Sets title labels visible
                        mProfileCrdTitle.setVisibility(View.VISIBLE);
                        mContactCrdTitle.setVisibility(View.VISIBLE);
                        mCompanyCrdTitle.setVisibility(View.VISIBLE);

                        Picasso.with(UserAccountActivity.this)
                                .load(AppData.getAgentsImagesPath() + sImage)
                                .fit()
                                .placeholder(R.drawable.avatar)
                                .into(mImage);

                        mFirstNameView.setText(firstName);
                        mLastNameView.setText(lastName);
                        mUsernameView.setText(userName);
                        mUserTypeView.setText(userType);
                        mOfficePhoneView.setText(officePhone);
                        mMobilePhoneView.setText(mobilePhone);
                        mEmailView.setText(email);
                        mCompanyView.setText(sCompany);
                        mPositionView.setText(sPosition);

                    } else {
                        //Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(UserAccountActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Toast.makeText(UserAccountActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editUserProfile(String firstName, String lastName, String userName, String type) {


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_edit, null);

        final int[] accountType = new int[1];

        final EditText mFirstNameView = dialogView.findViewById(R.id.first_name);
        final EditText mLastNameView = dialogView.findViewById(R.id.last_name);
        final EditText mUsernameView = dialogView.findViewById(R.id.username);

        mFirstNameView.setText(firstName);
        mLastNameView.setText(lastName);
        mUsernameView.setText(userName);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        if (Objects.equals(type, "agent")) {
            RadioButton agent = dialogView.findViewById(R.id.radio_agent);
            agent.setChecked(true);
            accountType[0] = 1;
        } else if (Objects.equals(type, "user")) {
            RadioButton user = dialogView.findViewById(R.id.radio_user);
            user.setChecked(true);
            accountType[0] = 0;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                // Is the button now checked?
                //boolean checked = ((RadioButton) view).isChecked();

                accountType[0] = 45;

                // Check which radio button was clicked
                switch (checkedId) {
                    case R.id.radio_agent:
                        accountType[0] = 1;
                        break;
                    case R.id.radio_user:
                        accountType[0] = 0;
                        break;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountActivity.this);
        // Add the buttons

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                String firstName = null;
                String lastName = null;
                String username = null;
                int type = accountType[0];

                firstName = mFirstNameView.getText().toString();

                lastName = mLastNameView.getText().toString();
                username = mUsernameView.getText().toString();

                updateUserProfile(firstName, lastName, username, type);

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

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editUserContact(String officePhone, String mobilePhone, String email) {


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_contact_edit, null);

        final EditText mOfficePhoneView = dialogView.findViewById(R.id.office_phone);
        final EditText mMobilePhoneView = dialogView.findViewById(R.id.mobile_phone);
        final EditText mEmailView = dialogView.findViewById(R.id.email);

        mOfficePhoneView.setText(officePhone);
        mMobilePhoneView.setText(mobilePhone);
        mEmailView.setText(email);


        AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountActivity.this);
        // Add the buttons

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                String sOfficePhone = null;
                String sMobilePhone = null;
                String sEmail = null;

                sOfficePhone = mOfficePhoneView.getText().toString();
                sMobilePhone = mMobilePhoneView.getText().toString();
                sEmail = mEmailView.getText().toString();

                updateUserContact(sOfficePhone, sMobilePhone, sEmail);

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

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editUserCompany(String company, String position) {


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_company_edit, null);

        final EditText mCompanyView = dialogView.findViewById(R.id.company);
        final EditText mPositionView = dialogView.findViewById(R.id.position);

        mCompanyView.setText(company);
        mPositionView.setText(position);


        AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountActivity.this);
        // Add the buttons

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                String sCompany = null;
                String sPosition = null;

                sCompany = mCompanyView.getText().toString();
                sPosition = mPositionView.getText().toString();

                updateUserCompany(sCompany, sPosition);

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

        dialog.show();
    }

    private void updateUserProfile(String firstName, String lastName, String username, int type) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(UserAccountActivity.this);
        mProgressDialog.setMessage("Saving........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(UserAccountActivity.this);
        String userId = sessionManager.getUserID();

        Log.d("ID", userId);

        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("username", username);
        params.put("user_type", type);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.updateProfile(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Profile" + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        String firstName = jsonObject.getString("first_name");
                        String lastName = jsonObject.getString("last_name");
                        String userName = jsonObject.getString("username");
                        String userType = jsonObject.getString("user_type");

                        mFirstNameView.setText(firstName);
                        mLastNameView.setText(lastName);
                        mUsernameView.setText(userName);
                        mUserTypeView.setText(userType);

                        Toast.makeText(UserAccountActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(UserAccountActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Toast.makeText(UserAccountActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_agent:
                if (checked)
                break;
            case R.id.radio_user:
                if (checked)
                break;
        }
    }

    private void updateUserContact(String officePhone, String mobilePhone, String email) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(UserAccountActivity.this);
        mProgressDialog.setMessage("Saving........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(UserAccountActivity.this);
        String userId = sessionManager.getUserID();

        Log.d("ID", userId);

        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("office_phone", officePhone);
        params.put("mobile_phone", mobilePhone);
        params.put("email", email);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.updateContact(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Contact" + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        String officePhone = jsonObject.getString("office_phone");
                        String mobilePhone = jsonObject.getString("mobile_phone");
                        String email = jsonObject.getString("email");

                        mOfficePhoneView.setText(officePhone);
                        mMobilePhoneView.setText(mobilePhone);
                        mEmailView.setText(email);

                        Toast.makeText(UserAccountActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(UserAccountActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Toast.makeText(UserAccountActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserCompany(String company, String position){

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(UserAccountActivity.this);
        mProgressDialog.setMessage("Saving........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(UserAccountActivity.this);
        String userId = sessionManager.getUserID();

        Log.d("ID", userId);

        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("company", company);
        params.put("position", position);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.updateCompany(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Company" + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        String sCompany = jsonObject.getString("company");
                        String sPosition = jsonObject.getString("position");

                        mCompanyView.setText(sCompany);
                        mPositionView.setText(sPosition);

                        Toast.makeText(UserAccountActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(UserAccountActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Toast.makeText(UserAccountActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
