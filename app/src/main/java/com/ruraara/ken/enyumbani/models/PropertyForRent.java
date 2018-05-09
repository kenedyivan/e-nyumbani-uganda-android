package com.ruraara.ken.enyumbani.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 10/30/17.
 */

public class PropertyForRent {
    static String TAG = PropertyForRent.class.getSimpleName();

    /**
     * An array of sample (dummy) items.
     */
    public static List<PropertyForRent.PropertyItem> ITEMS = new ArrayList<PropertyForRent.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, PropertyForRent.PropertyItem> ITEM_MAP = new HashMap<String, PropertyForRent.PropertyItem>();


    public static void addPropertyItem(PropertyForRent.PropertyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);

    }

    public static PropertyForRent.PropertyItem createPropertyItem(String id, String title,
                                                                  int rating, String address,
                                                                  String agentId, String agent, String price,
                                                                  String currency,
                                                                  String image) {
        return new PropertyForRent.PropertyItem(id, title, rating, address, agentId,agent, price, currency, image);
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
