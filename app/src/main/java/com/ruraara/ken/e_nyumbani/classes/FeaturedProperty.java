package com.ruraara.ken.e_nyumbani.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 10/25/17.
 */

public class FeaturedProperty {

    static String TAG = FeaturedProperty.class.getSimpleName();

    /**
     * An array of sample (dummy) items.
     */
    public static List<FeaturedProperty.PropertyItem> ITEMS = new ArrayList<FeaturedProperty.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, FeaturedProperty.PropertyItem> ITEM_MAP = new HashMap<String, FeaturedProperty.PropertyItem>();


    public static void addPropertyItem(PropertyItem item) {
        Log.w(TAG, "Creating property item");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        Log.w(TAG, "Finished creating property item");
        Log.w(TAG, String.valueOf(ITEM_MAP.size()));
        Log.w(TAG, String.valueOf(ITEMS.size()));

    }

    public static PropertyItem createPropertyItem(String id, String title,
                                                  int rating, String address, String agentId,
                                                  String agent, String price, String currency,
                                                  String image) {
        return new PropertyItem(id, title, rating, address, agentId, agent, price, currency, image);
    }

    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final int rating;
        public final String address;
        public final String agentId;
        public final String agent;
        public final String price;
        public final String currency;
        public final String image;

        public PropertyItem(String id, String title, int rating, String address, String agentId,
                            String agent, String price, String currency, String image) {
            this.id = id;
            this.title = title;
            this.rating = rating;
            this.address = address;
            this.agentId = agentId;
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
