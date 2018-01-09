package com.ruraara.ken.e_nyumbani.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ken on 1/9/18.
 */

public class SharedPropertyEditState {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static final int EDITED = 1;
    public static final int NOT_EDITED = 2;

    private static final String EDIT_STATUS = "EDIT_STATUS";

    // Sharedpref file name
    private static final String PREF_NAME = "E-NyumbaniPref.sharedEditState";


    public static final String EDITED_FLAG = "EDITED_FLAG";
    public static final String PROPERTY_ID = "PROPERTY_ID";
    public static final String MY_PROPERTIES_REFRESH = "MY_PROPERTIES_REFRESH";

    // Constructor
    public SharedPropertyEditState(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createEditState(int status, String id) {


        editor.putInt(EDITED_FLAG, status);

        editor.putInt(MY_PROPERTIES_REFRESH, status);

        editor.putString(PROPERTY_ID, id);

        // commit changes
        editor.commit();
    }

    public void clearMYPropertyDetailsFlag() {


        editor.remove(EDITED_FLAG);

        // commit changes
        editor.commit();
    }

    public void clearMYPropertiesFlag() {

        editor.remove(MY_PROPERTIES_REFRESH);

        // commit changes
        editor.commit();
    }

    public int getEditStatus() {
        return pref.getInt(EDITED_FLAG, 0);
    }

    public int getMyPropertiesRefreshFlag() {
        return pref.getInt(MY_PROPERTIES_REFRESH, 0);
    }


    public String getId() {
        return pref.getString(PROPERTY_ID, null);
    }
}
