package com.ruraara.ken.e_nyumbani.dummy;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 10/30/17.
 */

public class PropertyForSale {
    static String TAG = PropertyForSale.class.getSimpleName();

    /**
     * An array of sample (dummy) items.
     */
    public static List<PropertyForSale.PropertyItem> ITEMS = new ArrayList<PropertyForSale.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, PropertyForSale.PropertyItem> ITEM_MAP = new HashMap<String, PropertyForSale.PropertyItem>();


    public static void addPropertyItem(PropertyForSale.PropertyItem item) {
        Log.w(TAG, "Creating property for rent item");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        Log.w(TAG, "Finished creating property for rent item");
        Log.w(TAG, String.valueOf(ITEM_MAP.size()));
        Log.w(TAG, String.valueOf(ITEMS.size()));

    }

    public static PropertyForSale.PropertyItem createPropertyItem(String id, String title, int rating, String address, String agent, String price, String image) {
        return new PropertyForSale.PropertyItem(id, title, rating, address, agent, price, image);
    }

    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final int rating;
        public final String address;
        public final String agent;
        public final String price;
        public final String image;

        public PropertyItem(String id, String title, int rating, String address, String agent, String price, String image) {
            this.id = id;
            this.title = title;
            this.rating = rating;
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
