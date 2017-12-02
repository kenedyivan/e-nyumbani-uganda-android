package com.ruraara.ken.e_nyumbani;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.dummy.DummyContent;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.loopj.android.http.AsyncHttpClient.log;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FeaturedPropertyFragment.OnListFragmentInteractionListener,
        ForRentPropertyFragment.OnListFragmentInteractionListener,
        ForSalePropertyFragment.OnListFragmentInteractionListener,
        MyPropertiesFragment.OnFragmentInteractionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);




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
                Intent i = new Intent(DrawerActivity.this, AddProperty.class);
                startActivity(i);
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

        if(getIntent().getStringExtra("fragment") != null){
            Intent intent = getIntent();
            String name = intent.getStringExtra("fragment");

            if(name != null){

                Log.d("Name: ", name);

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
        }else if(getIntent().getStringExtra("fragment") == null){
            Log.d("Name: ", "Intent is null");

            //Sets navigation drawer first item to checked
            navigationView.getMenu().getItem(0).setChecked(true);
            if (navigationView.getMenu().findItem(R.id.nav_camera).isChecked()) {
                position = 0;
            }


            if (position == 0) {
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
        }else{
            //Sets navigation drawer first item to checked
            navigationView.getMenu().getItem(0).setChecked(true);
            if (navigationView.getMenu().findItem(R.id.nav_camera).isChecked()) {
                position = 0;
            }


            if (position == 0) {
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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("Position: ", String.valueOf(position));

        if (position > 0) {

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {

            fragmentClass = FeaturedPropertyFragment.class;
            position = 0;
            toolbar.setTitle("Featured");

        } else if (id == R.id.nav_gallery) {

            fragmentClass = ForRentPropertyFragment.class;
            position = 2;
            toolbar.setTitle("For rent");

        } else if (id == R.id.nav_slideshow) {

            fragmentClass = ForSalePropertyFragment.class;
            position = 3;
            toolbar.setTitle("For sale");

        } else if (id == R.id.nav_manage) {
            position = 4;
            Log.d("MenuItem: ", String.valueOf(item.getTitle()));
        } else if (id == R.id.nav_my_properties) {
            fragmentClass = MyPropertiesFragment.class;
            position = 5;
            toolbar.setTitle("My properties");
            Log.d("MenuItem: ", String.valueOf(item.getTitle()));
        } else if (id == R.id.nav_send) {
            position = 6;
            Log.d("MenuItem: ", String.valueOf(item.getTitle()));
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
        RequestParams params = new RequestParams();

        params.put("id", userId);

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
                Log.d(TAG, "Response: " + resp);

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
                        mEmail.setText(jsonObject.getString("email"));

                        Picasso.with(DrawerActivity.this)
                                .load(AppData.userProfilePic() + jsonObject.getString("profile_picture"))
                                .resize(200, 200)
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
}
