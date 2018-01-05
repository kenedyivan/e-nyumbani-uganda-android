package com.ruraara.ken.e_nyumbani.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 12/2/17.
 */

public class MyProperty {

    private static String TAG = "MyProperty";

    public static List<MyProperty.PropertyItem> ITEMS = new ArrayList<MyProperty.PropertyItem>();

    public static Map<String, MyProperty.PropertyItem> ITEM_MAP = new HashMap<String, MyProperty.PropertyItem>();

    public static void addPropertyItem(MyProperty.PropertyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);

        Log.w(TAG, "Finished creating property for rent item");
        Log.w(TAG, String.valueOf(ITEM_MAP.size()));
        Log.w(TAG, String.valueOf(ITEMS.size()));

    }

    public static MyProperty.PropertyItem createPropertyItem(String id, String title,
                                                             int rating, String address,
                                                             String price, String currency,
                                                             String image) {
        return new MyProperty.PropertyItem(id, title, rating, address, price, currency, image);
    }

    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final int rating;
        public final String address;
        public final String price;
        public final String currency;
        public final String image;

        public PropertyItem(String id, String title, int rating, String address,
                            String price, String currency, String image) {
            this.id = id;
            this.title = title;
            this.rating = rating;
            this.address = address;
            this.price = price;
            this.currency = currency;
            this.image = image;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
