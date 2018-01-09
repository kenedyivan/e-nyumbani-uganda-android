package com.ruraara.ken.e_nyumbani.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ken on 1/9/18.
 */

public class SharedDrawerNavigationUpHomeState {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static final int GONE = 1;
    public static final int NOT_GONE = 2;

    private static final String EDIT_STATUS = "EDIT_STATUS";

    // Sharedpref file name
    private static final String PREF_NAME = "E-NyumbaniPref.drawerNavigationUpHomeState";


    public static final String RENT = "RENT_FLAG";
    public static final String SALE = "SALE_FLAG";
    public static final String AGENTS = "AGENTS_FLAG";
    public static final String FAVORITES = "FAVORITES_FLAG";
    public static final String PROPERTIES = "PROPERTIES_FLAG";
    public static final String SETTINGS = "SETTINGS_FLAG";

    // Constructor
    public SharedDrawerNavigationUpHomeState(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void goneToRent(int status) {

        editor.putInt(RENT, status);
        // commit changes
        editor.commit();
    }

    public void backFromRent(int status) {

        editor.remove(RENT);
        // commit changes
        editor.commit();
    }

    public int getRentViewState() {
        return pref.getInt(RENT, 0);
    }

    public void goneToSale(int status) {

        editor.putInt(SALE, status);
        // commit changes
        editor.commit();
    }

    public void backFromSale(int status) {

        editor.remove(SALE);
        // commit changes
        editor.commit();
    }

    public int getSaleViewState() {
        return pref.getInt(SALE, 0);
    }

    public void goneToAgents(int status) {

        editor.putInt(AGENTS, status);
        // commit changes
        editor.commit();
    }

    public void backFromAgents(int status) {

        editor.remove(AGENTS);
        // commit changes
        editor.commit();
    }

    public int getAgentsViewState() {
        return pref.getInt(AGENTS, 0);
    }

    public void goneToFavorites(int status) {

        editor.putInt(FAVORITES, status);
        // commit changes
        editor.commit();
    }

    public void backFromFavorites(int status) {

        editor.remove(FAVORITES);
        // commit changes
        editor.commit();
    }

    public int getFavoritesViewState() {
        return pref.getInt(FAVORITES, 0);
    }

    public void goneToProperties(int status) {

        editor.putInt(PROPERTIES, status);
        // commit changes
        editor.commit();
    }

    public void backFromProperties(int status) {

        editor.remove(PROPERTIES);
        // commit changes
        editor.commit();
    }
    public int getPropertiesViewState() {
        return pref.getInt(PROPERTIES, 0);
    }

    public void goneToSettings(int status) {

        editor.putInt(RENT, status);
        // commit changes
        editor.commit();
    }

    public void backFromSettings(int status) {

        editor.remove(SETTINGS);
        // commit changes
        editor.commit();
    }


    public int getSettingsViewState() {
        return pref.getInt(SETTINGS, 0);
    }

}
