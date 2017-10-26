package com.ruraara.ken.e_nyumbani.dummy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ken on 10/25/17.
 */

public class Property {

    static String TAG = "PropertyItem";

    /**
     * An array of sample (dummy) items.
     */
    public static List<Property.PropertyItem> ITEMS = new ArrayList<Property.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Property.PropertyItem> ITEM_MAP = new HashMap<String, Property.PropertyItem>();


    public static void addPropertyItem(PropertyItem item) {
        Log.w(TAG, "Creating property item");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        Log.w(TAG, "Finished creating property item");
        Log.w(TAG, String.valueOf(ITEM_MAP.size()));
        Log.w(TAG, String.valueOf(ITEMS.size()));

    }

    public static PropertyItem createPropertyItem(String id, String title, String address, String agent, String price, String image) {
        return new PropertyItem(id, title, address, agent, price, image);
    }

    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final String address;
        public final String agent;
        public final String price;
        public final String image;

        public PropertyItem(String id, String title, String address, String agent, String price, String image) {
            this.id = id;
            this.title = title;
            this.address = address;
            this.agent = agent;
            this.price = price;
            this.image = image;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
