package com.ruraara.ken.enyumbani.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 12/11/17.
 */

public class AgentProperty {

    static String TAG = AgentProperty.class.getSimpleName();

    /**
     * An array of items.
     */
    public static List<AgentProperty.PropertyItem> ITEMS = new ArrayList<AgentProperty.PropertyItem>();

    public static List<AgentProperty.PropertyItem> RENT_ITEMS = new ArrayList<AgentProperty.PropertyItem>();
    public static List<AgentProperty.PropertyItem> SALE_ITEMS = new ArrayList<AgentProperty.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of items, by ID.
     */
    public static Map<String, AgentProperty.PropertyItem> ITEM_MAP = new HashMap<String, AgentProperty.PropertyItem>();

    public static void addPropertyItem(AgentProperty.PropertyItem item) {
        ITEMS.add(item);
        if(Integer.parseInt(item.status) == 1){
            SALE_ITEMS.add(item);
        }else{
            RENT_ITEMS.add(item);
        }

        ITEM_MAP.put(item.id, item);

    }

    public static AgentProperty.PropertyItem createPropertyItem(String id, String title,
                                                                int rating, String address,
                                                                String price, String status, String currency, String image) {
        return new AgentProperty.PropertyItem(id, title, rating, address, price, status, currency, image);
    }

    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem implements Serializable{
        public final String id;
        public final String title;
        public final int rating;
        public final String address;
        public final String price;
        public final String status;
        public final String currency;
        public final String image;

        public PropertyItem(String id, String title, int rating, String address, String price, String status, String currency, String image) {
            this.id = id;
            this.title = title;
            this.rating = rating;
            this.address = address;
            this.price = price;
            this.status = status;
            this.currency = currency;
            this.image = image;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
