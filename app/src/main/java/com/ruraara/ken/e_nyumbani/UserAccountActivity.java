package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

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


        //Set visibility gone
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
