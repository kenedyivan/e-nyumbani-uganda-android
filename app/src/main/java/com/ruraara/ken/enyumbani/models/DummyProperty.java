package com.ruraara.ken.enyumbani.models;

import com.ruraara.ken.enyumbani.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 10/26/17.
 */

public class DummyProperty {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyProperty.DummyItem> ITEMS = new ArrayList<DummyProperty.DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyProperty.DummyItem> ITEM_MAP = new HashMap<String, DummyProperty.DummyItem>();

    private static final int COUNT = 4;

    static {
        int[] images = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};

        for (int i = 0; i < COUNT; i++) {
            addItem(new DummyProperty.DummyItem(String.valueOf(i), "Namanve Plaza",
                    "Namanve industrial area", "Ogola advocates", "100,000", images[i]));
        }
    }

    private static void addItem(DummyProperty.DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /*private static DummyProperty.DummyItem createDummyItem(String id, String title, String agent, String price, int image) {

        return new DummyProperty.DummyItem(id, title, agent, price, image);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String title;
        public final String address;
        public final String agent;
        public final String price;
        public final int image;

        public DummyItem(String id, String title, String address, String agent, String price, int image) {
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
