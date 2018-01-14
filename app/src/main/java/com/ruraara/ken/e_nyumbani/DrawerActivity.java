package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.models.DummyContent;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;
import com.ruraara.ken.e_nyumbani.utils.SharedDrawerNavigationUpHomeState;
import com.ruraara.ken.e_nyumbani.utils.SharedPropertyEditState;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FeaturedPropertyFragment.OnListFragmentInteractionListener,
        ForRentPropertyFragment.OnListFragmentInteractionListener,
        ForSalePropertyFragment.OnListFragmentInteractionListener,
        MyPropertiesFragment.OnFragmentInteractionListener,
        PropertyAgentFragment.OnListFragmentInteractionListener,
        MyFavoritesFragment.OnListFragmentInteractionListener,
        FeaturedPropertyFragment.OnDataPass,
        MyFavoritesFragment.OnDataPass,
        MyPropertiesFragment.OnEmptyList,
        FeaturedPropertyFragment.OnEmptyList,
        ForRentPropertyFragment.OnEmptyList,
        ForSalePropertyFragment.OnEmptyList,
        PropertyAgentFragment.OnEmptyList,
        MyFavoritesFragment.OnEmptyList {

    NavigationView navigationView;
    int position;
    String TAG = DrawerActivity.class.getSimpleName();
    Fragment fragment = null;
    Class fragmentClass = null;
    MaterialSearchView searchView;
    Toolbar toolbar;

    TextView mName;
    TextView mEmail;
    ImageView mProfilePicture;

    boolean press;
    CircleImageView mAvatarView;
    ImageView mChangePickView;

    private ProgressBar mProgressBar;

    String profilePicture;

    public static final String REFRESH = "refresh";

    boolean retrivedFeatured = false;

    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;
    private int FILTER_PROPERTY_REQUEST = 3;

    public static String IMAGE_URI = "image_uri";

    Uri croppedImage;

    String imageName;

    TextView mEmptyList;

    View dialogView;
    AlertDialog dialog;
    boolean reloadDialog;

    Button mFilterBtn;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Drawer", "onResume");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mEmptyList.setVisibility(View.GONE);
        mFilterBtn.setVisibility(View.VISIBLE);

        //Track Drawer navigation
        SharedDrawerNavigationUpHomeState sd = new SharedDrawerNavigationUpHomeState(DrawerActivity.this);
        if (sd.getRentViewState() == SharedDrawerNavigationUpHomeState.GONE) {
            navigationView.getMenu().getItem(1).setChecked(true);

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = ForRentPropertyFragment.class;
            position = 2;
            toolbar.setTitle("For rent");

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            sd.backFromRent(SharedDrawerNavigationUpHomeState.NOT_GONE);
        }

        if (sd.getSaleViewState() == SharedDrawerNavigationUpHomeState.GONE) {
            navigationView.getMenu().getItem(2).setChecked(true);
            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = ForSalePropertyFragment.class;
            position = 3;
            toolbar.setTitle("For sale");

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            sd.backFromSale(SharedDrawerNavigationUpHomeState.NOT_GONE);
        }

        if (sd.getAgentsViewState() == SharedDrawerNavigationUpHomeState.GONE) {
            navigationView.getMenu().getItem(3).setChecked(true);
            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = PropertyAgentFragment.class;
            position = 4;
            toolbar.setTitle("Agents");

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            sd.backFromAgents(SharedDrawerNavigationUpHomeState.NOT_GONE);
        }

        if (sd.getFavoritesViewState() == SharedDrawerNavigationUpHomeState.GONE) {
            navigationView.getMenu().getItem(4).setChecked(true);
            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = MyFavoritesFragment.class;
            position = 5;
            toolbar.setTitle("My favorites");

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            sd.backFromFavorites(SharedDrawerNavigationUpHomeState.NOT_GONE);
        }

        if (sd.getPropertiesViewState() == SharedDrawerNavigationUpHomeState.GONE) {
            navigationView.getMenu().getItem(5).setChecked(true);

            mFilterBtn.setVisibility(View.GONE);
            SharedPropertyEditState sharedPropertyEditState = new SharedPropertyEditState(DrawerActivity.this);
            int edited = sharedPropertyEditState.getMyPropertiesRefreshFlag();

            if (edited == SharedPropertyEditState.EDITED) { ///// TODO: 1/9/18 Do a network operation here when property is edited

                fragmentClass = MyPropertiesFragment.class;
                position = 5;
                toolbar.setTitle("MyProperties");

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();

                Log.e("UpHome", "up home button");
                sharedPropertyEditState.clearMYPropertiesFlag();

            } else {
                fragmentClass = MyPropertiesFragment.class;
                position = 6;
                toolbar.setTitle("My properties");

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            }

            sd.backFromProperties(SharedDrawerNavigationUpHomeState.NOT_GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Log.e("Drawer", "onCreate");

        mEmptyList = (TextView) findViewById(R.id.empty_view);
        mEmptyList.setVisibility(View.GONE);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Featured");
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        mName = hView.findViewById(R.id.name);
        mEmail = hView.findViewById(R.id.email);
        mProfilePicture = hView.findViewById(R.id.profile_picture);
        setDrawerProfile();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                trackCallToAction();
                Intent i = new Intent(DrawerActivity.this, AddPropertyActivity.class);
                startActivity(i);
            }
        });

        mFilterBtn = (Button) findViewById(R.id.filter_button);

        mFilterBtn.setVisibility(View.VISIBLE);

        mFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerActivity.this,FilterActivity.class);
                startActivityForResult(intent,FILTER_PROPERTY_REQUEST);
            }
        });

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(DrawerActivity.this, SearchResults.class);
                intent.putExtra(SearchResults.QUERY, query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().getStringExtra("fragment") != null) {
            Intent intent = getIntent();
            String name = intent.getStringExtra("fragment");

            if (name != null) {

                Log.d("Name: ", name);

                mFilterBtn.setVisibility(View.GONE);
                fragmentClass = MyPropertiesFragment.class;
                toolbar.setTitle("MyProperties");

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            }
        } else if (getIntent().getStringExtra("fragment") == null) {
            Log.d("Name: ", "Intent is null");

            //Sets navigation drawer first item to checked
            navigationView.getMenu().getItem(0).setChecked(true);
            if (navigationView.getMenu().findItem(R.id.nav_camera).isChecked()) {
                position = 0;
            }


            if (position == 0) {
                mFilterBtn.setVisibility(View.VISIBLE);
                fragmentClass = FeaturedPropertyFragment.class;
                toolbar.setTitle("Featured");

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            }
        } else {
            //Sets navigation drawer first item to checked
            navigationView.getMenu().getItem(0).setChecked(true);
            if (navigationView.getMenu().findItem(R.id.nav_camera).isChecked()) {
                position = 0;
            }


            if (position == 0) {
                mFilterBtn.setVisibility(View.VISIBLE);
                fragmentClass = FeaturedPropertyFragment.class;
                toolbar.setTitle("Featured");

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
            }
        }

        //Alert dialog gathers other user information


    }


    //End of alert dialog


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("Position: ", String.valueOf(position));

        if (position > 0) {

            mFilterBtn.setVisibility(View.VISIBLE);
            drawer.closeDrawer(GravityCompat.START);
            fragmentClass = FeaturedPropertyFragment.class;
            toolbar.setTitle("Featured");

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            position = 0;
            navigationView.getMenu().getItem(0).setChecked(true);


            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();


            ;

            /*position = 0;
            navigationView.getMenu().getItem(0).setChecked(true);*/

        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
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
            trackCallToAction();
            Intent i = new Intent(DrawerActivity.this, TopSettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void trackCallToAction() {
        final SharedDrawerNavigationUpHomeState sd = new SharedDrawerNavigationUpHomeState(DrawerActivity.this);
        switch (position) {
            case 0:
                break;
            case 2:
                sd.goneToRent(SharedDrawerNavigationUpHomeState.GONE);
                break;
            case 3:
                sd.goneToSale(SharedDrawerNavigationUpHomeState.GONE);
                break;
            case 4:
                sd.goneToAgents(SharedDrawerNavigationUpHomeState.GONE);
                break;
            case 5:
                sd.goneToFavorites(SharedDrawerNavigationUpHomeState.GONE);
                break;
            case 6:
                sd.goneToProperties(SharedDrawerNavigationUpHomeState.GONE);
                break;
            default:
                Log.d("Debug", "Not gone");
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mEmptyList.setVisibility(View.GONE);

        if (id == R.id.nav_camera) {

            mFilterBtn.setVisibility(View.VISIBLE);
            fragmentClass = FeaturedPropertyFragment.class;
            position = 0;
            toolbar.setTitle("Featured");

        } else if (id == R.id.nav_gallery) {

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = ForRentPropertyFragment.class;
            position = 2;
            toolbar.setTitle("For rent");

        } else if (id == R.id.nav_slideshow) {

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = ForSalePropertyFragment.class;
            position = 3;
            toolbar.setTitle("For sale");

        } else if (id == R.id.nav_manage) {

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = PropertyAgentFragment.class;
            position = 4;
            toolbar.setTitle("Agents");

        } else if (id == R.id.nav_my_favorites) {

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = MyFavoritesFragment.class;
            position = 5;
            toolbar.setTitle("My favorites");

        } else if (id == R.id.nav_my_properties) {

            mFilterBtn.setVisibility(View.GONE);
            fragmentClass = MyPropertiesFragment.class;
            position = 6;
            toolbar.setTitle("My properties");

        } else if (id == R.id.nav_settings) {

            mFilterBtn.setVisibility(View.GONE);
            position = 7;
            Intent i = new Intent(DrawerActivity.this, TopSettingsActivity.class);
            startActivity(i);

        }

        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    private void setDrawerProfile() {

        SessionManager sessionManager = new SessionManager(DrawerActivity.this);
        String userId = sessionManager.getUserID();
        final int loginType = sessionManager.getLoginType();

        Log.d("LoginType: ", String.valueOf(loginType));

        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("login_type", loginType);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.userProfile(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Profile: " + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        String lastname = jsonObject.getString("lastname");
                        String firstname = jsonObject.getString("firstname");

                        String lastCap = lastname.substring(0, 1).toUpperCase() + lastname.substring(1);

                        String firstCap = firstname.substring(0, 1).toUpperCase() + firstname.substring(1);

                        mName.setText(lastCap + " " + firstCap);

                        String email = jsonObject.getString("email");
                        String company = jsonObject.getString("company");
                        String userType = jsonObject.getString("user_type");

                        Log.w("EEEmail: ", "++" + email);
                        Log.w("EECompany: ", "--" + company);
                        Log.w("EEUserType: ", "**" + userType);

                        SessionManager s = new SessionManager(DrawerActivity.this);

                        if (email == null || Objects.equals(email, "") || Objects.equals(email, "NULL")) {
                            Log.e("Email", "no email");
                            int e = 0;

                            s.emailPresenceFlag(e);
                        } else {
                            Log.e("Email", "has email");
                            mEmail.setText(jsonObject.getString("email"));
                            s.emailPresenceFlag(1);
                        }


                        if (company == null || Objects.equals(company, "") || Objects.equals(company, "NULL")) {
                            Log.e("Company", "no company");
                            int c = 0;
                            s.companyPresenceFlag(c);
                        } else {
                            Log.e("Company", "has company");
                            s.companyPresenceFlag(1);
                        }

                        if (userType == null || Objects.equals(userType, "") || Objects.equals(userType, "NULL")) {
                            Log.e("User Type", "no user type");
                            int u = 0;
                            s.userTypePresenceFlag(u);
                        } else {
                            Log.e("User Type", "has user type");
                            s.userTypePresenceFlag(1);
                        }

                        Log.w("profile other: ", s.getOtherDetailsFlags().toString());


                        //String profilePicture;

                        if (loginType == 2) {
                            profilePicture = jsonObject.getString("profile_picture");
                        } else {
                            profilePicture = AppData.userProfilePic() + jsonObject.getString("profile_picture");
                        }

                        Picasso.with(DrawerActivity.this)
                                .load(profilePicture)
                                .placeholder(R.drawable.avatar)
                                .resize(70, 70)
                                .centerCrop()
                                .into(mProfilePicture);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onDataPass(boolean data) {
        if (data) {
            SessionManager sm = new SessionManager(DrawerActivity.this);
            HashMap<String, Integer> hMap = sm.getOtherDetailsFlags();
            Log.d("Other det: ", hMap.toString());
            int e = hMap.get(SessionManager.KEY_EMAIL_FLAG);
            int c = hMap.get(SessionManager.KEY_COMPANY_FLAG);
            int u = hMap.get(SessionManager.KEY_USER_TYPE_FLAG);

            if (e == 0 || c == 0 || u == 0) {
                otherInfo(hMap.get(SessionManager.KEY_EMAIL_FLAG), hMap.get(SessionManager.KEY_COMPANY_FLAG),
                        hMap.get(SessionManager.KEY_USER_TYPE_FLAG), null);
            }

        }
    }

    private void otherInfo(int e, int c, int u, Uri uri) {


        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_other_details, null);

        mAvatarView = dialogView.findViewById(R.id.avatar);
        mChangePickView = dialogView.findViewById(R.id.pencil_edit_image);
        mProgressBar = dialogView.findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.GONE);

        mChangePickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                reloadDialog = true;
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        if (uri != null) {
            try {
                mAvatarView.setImageBitmap(getBitmapFromUri(uri));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            Picasso.with(DrawerActivity.this)
                    .load(profilePicture)
                    .placeholder(R.drawable.avatar)
                    .resize(70, 70)
                    .centerCrop()
                    .into(mAvatarView);
        }


        final int[] profile = new int[1];

        final EditText mEmailView = dialogView.findViewById(R.id.email);
        final EditText mCompanyView = dialogView.findViewById(R.id.company);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        TextView mPromptView = dialogView.findViewById(R.id.prompt);

        if (e == 0 && c == 1 && u == 1) {
            mPromptView.setText("Add your email address");
        } else if (c == 0 && e == 1 && u == 1) {
            mPromptView.setText("Add company name");
        } else if (u == 0 && e == 1 && c == 1) {
            mPromptView.setText("Select your user role");
        } else if (u == 0 && e == 0 && c == 0) {
            mPromptView.setText("User role, email and company name");
        }


        String email = null;
        String company = null;

        if (e == 1) {
            mEmailView.setVisibility(View.GONE);
        }

        Log.d("FinalCompany:", "io--" + mCompanyView.getText().toString());

        if (c == 1) {
            mCompanyView.setVisibility(View.GONE);
        }

        if (u == 1) {
            radioGroup.setVisibility(View.GONE);
        } else {
            RadioButton agent = dialogView.findViewById(R.id.radio_agent);
            agent.setChecked(true);
            profile[0] = 1;
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                // Is the button now checked?
                //boolean checked = ((RadioButton) view).isChecked();

                profile[0] = 45;

                // Check which radio button was clicked
                switch (checkedId) {
                    case R.id.radio_agent:
                        //if (checked)
                        // FeaturedProperty agents
                        Log.d("Radio: ", "Agent");
                        profile[0] = 1;
                        break;
                    case R.id.radio_user:
                        //if (checked)
                        // FeaturedProperty buyers
                        Log.d("Radio: ", "User");
                        profile[0] = 0;
                        break;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
        // Add the buttons
        final String finalEmail = email;
        final String finalCompany = company;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                press = true;

                String email = null;
                String company = null;

                email = mEmailView.getText().toString();

                company = mCompanyView.getText().toString();

                Log.d("FinalProfile:", "io--" + profile[0]);
                Log.d("FinalEmail:", "io--" + email);
                Log.d("FinalCompany:", "io--" + company);


                updateOtherDetails(profile[0], email, company);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                press = false;
            }
        });


        builder.setView(dialogView);
        // Set other dialog properties
        // Create the AlertDialog
        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {

                if (!press && reloadDialog) {
                    //reload
                    reloadDialog = false;
                } else if(!press){
                    Log.d("Dialog: ", "dismissed");
                    finish();
                }
            }
        });

        dialog.show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_agent:
                if (checked)
                    Log.d("Radio: ", "agent");
                // FeaturedProperty agents
                break;
            case R.id.radio_user:
                if (checked)
                    // FeaturedProperty buyers
                    Log.d("Radio: ", "buyer");
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("REQUEST CODE",String.valueOf(requestCode));

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            Log.d("Avatar Uri", uri.toString());

            Intent i = new Intent(DrawerActivity.this, ImageCropperActivity.class);
            i.putExtra(IMAGE_URI, uri.toString());
            startActivityForResult(i, CROP_IMAGE_REQUEST);

        }

        if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri croppedImageUri = Uri.parse(data.getExtras().getString(ImageCropperActivity.CROPPED_IMAGE_URI));
            dumpImageMetaData(croppedImageUri);
            uploadProfilePicture(croppedImageUri);

            croppedImage = croppedImageUri;

            SessionManager sm = new SessionManager(DrawerActivity.this);
            HashMap<String, Integer> hMap = sm.getOtherDetailsFlags();
            otherInfo(hMap.get(SessionManager.KEY_EMAIL_FLAG), hMap.get(SessionManager.KEY_COMPANY_FLAG),
                    hMap.get(SessionManager.KEY_USER_TYPE_FLAG), croppedImageUri);

        }

        if (requestCode == FILTER_PROPERTY_REQUEST && resultCode == RESULT_OK) {
            String filter = data.getExtras().getString(FilterActivity.FILTER_TAG);

            Log.d("Filter", filter);

            String filterArray[]= filter.split(":");

            String sFromPrice = filterArray[0];
            String sToPrice = filterArray[1];
            String sAddress= filterArray[2];
            String sDistrict = filterArray[3];
            String sTown = filterArray[4];
            String sRegion = filterArray[5];
            String sType = filterArray[6];
            String sStatus = filterArray[7];
            String sCurrency = filterArray[8];

            String filter1 = sFromPrice+":"+sToPrice+":"+sAddress+":"+sDistrict+":"
                    +sTown+":"+sRegion+":"+sType+":"+sStatus+":"+sCurrency;

            getFilteredProperties(filterArray);
            
            ///Log.d("filter1",filter1);

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

        SessionManager sessionManager = new SessionManager(DrawerActivity.this);

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
                Toast.makeText(DrawerActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(DrawerActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(DrawerActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateOtherDetails(int profile, String email, String company) {
        SessionManager sessionManager = new SessionManager(DrawerActivity.this);
        String userId = sessionManager.getUserID();

        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("profile", profile);
        params.put("email", email);
        params.put("company", company);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppData.setOtherDetails(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Other details: " + resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    int success = jsonObject.getInt("success");
                    int error = jsonObject.getInt("error");

                    Log.d("Success: ", String.valueOf(success));
                    Log.d("Error: ", String.valueOf(error));

                    if (error == 0 && success == 1) {

                        SessionManager s = new SessionManager(DrawerActivity.this);
                        s.emailPresenceFlag(1);

                        s.companyPresenceFlag(1);

                        Log.d("After up: ", s.getOtherDetailsFlags().toString());

                        setDrawerProfile();

                        Toast.makeText(DrawerActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

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

    @Override
    public void listEmpty(boolean data) {
        if (data) {
            mEmptyList.setVisibility(View.VISIBLE);
            Log.e("Empty list", String.valueOf(data));
        }

    }

    private void getFilteredProperties(String[] filterArray) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(DrawerActivity.this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        String sFromPrice = filterArray[0];
        String sToPrice = filterArray[1];
        String sAddress= filterArray[2];
        String sDistrict = filterArray[3];
        String sTown = filterArray[4];
        String sRegion = filterArray[5];
        String sType = filterArray[6];
        String sStatus = filterArray[7];
        String sCurrency = filterArray[8];

        RequestParams params = new RequestParams();
        params.put("lower_price", sFromPrice);
        params.put("higher_price", sToPrice);
        params.put("address", sAddress);
        params.put("district", sDistrict);
        params.put("town", sTown);
        params.put("region", sRegion);
        params.put("type", sType);
        params.put("status", sStatus);
        params.put("currency", sCurrency);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.filterProperty(), params, new AsyncHttpResponseHandler() {

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
                Log.d(TAG, "Filtered property" + resp);

                //Toast.makeText(EditPropertyActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();

                try {
                    //JSONObject jsonObject = new JSONObject(resp);
                    JSONArray jsonArray = new JSONArray(resp);

                    //Sets navigation drawer first item to checked
                    navigationView.getMenu().getItem(0).setChecked(true);
                    if (navigationView.getMenu().findItem(R.id.nav_camera).isChecked()) {
                        position = 10;
                    }


                    if (position == 10) {
                        mFilterBtn.setVisibility(View.GONE);
                        fragment = new FeaturedPropertyFragment();
                        Bundle featuredBundle = new Bundle();
                        featuredBundle.putString("filtered", jsonArray.toString());
                        fragment.setArguments(featuredBundle);
                        toolbar.setTitle("Filtered");

                        /*try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.f_content, fragment).commit();
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
                Toast.makeText(DrawerActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(DrawerActivity.this, "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
