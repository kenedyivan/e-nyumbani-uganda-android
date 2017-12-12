package com.ruraara.ken.e_nyumbani.sessions;

/**
 * Created by ken on 12/1/17.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ruraara.ken.e_nyumbani.LoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "E-NyumbaniPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User email (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // User password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // User id (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // User social id (make variable public to access from outside)
    public static final String KEY_SOCIAL_ID = "social_id";

    // User login type (make variable public to access from outside)
    public static final String KEY_LOGIN_TYPE = "login_type";

    // User social id (make variable public to access from outside)
    public static final String KEY_EMAIL_FLAG = "email_flag";

    // User login type (make variable public to access from outside)
    public static final String KEY_COMPANY_FLAG = "company_flag";

    //User type
    public static final String KEY_USER_TYPE_FLAG = "user_type";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String password, String id, int loginType) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing id in pref
        editor.putString(KEY_ID, id);

        // Storing login type in pref
        editor.putInt(KEY_LOGIN_TYPE, loginType);

        // commit changes
        editor.commit();
    }

    public void createLoginSession(String id, String socialId, int loginType) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //Removes email from field
        editor.remove(KEY_EMAIL);

        //Removes password from field
        editor.remove(KEY_PASSWORD);

        editor.putString(KEY_SOCIAL_ID, socialId);

        // Storing id in pref
        editor.putString(KEY_ID, id);

        // Storing login type in pref
        editor.putInt(KEY_LOGIN_TYPE, loginType);

        // commit changes
        editor.commit();
    }

    public void emailPresenceFlag(int email) {
        //Removes email flag
        editor.remove(KEY_EMAIL_FLAG);
        // Stores email glad in pref
        editor.putInt(KEY_EMAIL_FLAG, email);
        // commit changes
        editor.commit();
    }

    public void companyPresenceFlag(int company) {
        //Removes company flag
        editor.remove(KEY_COMPANY_FLAG);
        // Stores company flag in pref
        editor.putInt(KEY_COMPANY_FLAG, company);
        // commit changes
        editor.commit();

    }

    public void userTypePresenceFlag(int userType) {
        //Removes user type flag
        editor.remove(KEY_USER_TYPE_FLAG);
        // Stores user type flag in pref
        editor.putInt(KEY_USER_TYPE_FLAG, userType);
        // commit changes
        editor.commit();

    }

    public HashMap<String, Integer> getOtherDetailsFlags() {
        HashMap<String, Integer> flags = new HashMap<String, Integer>();

        // user email
        flags.put(KEY_EMAIL_FLAG, pref.getInt(KEY_EMAIL_FLAG, 0));
        flags.put(KEY_COMPANY_FLAG, pref.getInt(KEY_COMPANY_FLAG, 0));
        flags.put(KEY_USER_TYPE_FLAG, pref.getInt(KEY_USER_TYPE_FLAG, 0));

        return flags;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user password
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        // return user
        return user;
    }

    public String getUserID() {
        return pref.getString(KEY_ID, null);
    }

    public int getLoginType() {
        return pref.getInt(KEY_LOGIN_TYPE, 0);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Starting Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
