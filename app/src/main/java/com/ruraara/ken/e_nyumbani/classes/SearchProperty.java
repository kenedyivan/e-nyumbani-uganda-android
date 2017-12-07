package com.ruraara.ken.e_nyumbani.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 11/2/17.
 */

public class SearchProperty {
    static String TAG = "SearchSearchProperty";

    /**
     * An array of sample (dummy) items.
     */
    public static List<SearchProperty.PropertyItem> ITEMS = new ArrayList<SearchProperty.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, SearchProperty.PropertyItem> ITEM_MAP = new HashMap<String, SearchProperty.PropertyItem>();


    public static void addPropertyItem(SearchProperty.PropertyItem item) {
        Log.w(TAG, "Creating SearchProperty item");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        Log.w(TAG, "Finished creating SearchProperty item");
        Log.w(TAG, String.valueOf(ITEM_MAP.size()));
        Log.w(TAG, String.valueOf(ITEMS.size()));

    }

    public static SearchProperty.PropertyItem createPropertyItem(String id, String title, String address,
                                                                 String agent, String price, String currency, String image) {
        return new SearchProperty.PropertyItem(id, title, address, agent, price, currency, image);
    }

    /**
     * A SearchProperty item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final String address;
        public final String agent;
        public final String price;
        public final String currency;
        public final String image;

        public PropertyItem(String id, String title, String address, String agent, String price, String currency, String image) {
            this.id = id;
            this.title = title;
            this.address = address;
            this.agent = agent;
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
