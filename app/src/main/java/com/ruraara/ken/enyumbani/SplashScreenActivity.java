package com.ruraara.ken.enyumbani;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ruraara.ken.enyumbani.sessions.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final SessionManager sessionManager = new SessionManager(SplashScreenActivity.this);

        if (sessionManager.getUserID() != null) {
            Log.d("ID: ", "User; " + sessionManager.getUserID());
        } else {
            Log.d("ID: ", "New user");
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Intent i;

                if (sessionManager.isLoggedIn()) {
                    i = new Intent(SplashScreenActivity.this, DrawerActivity.class);
                } else {
                    i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }

                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
