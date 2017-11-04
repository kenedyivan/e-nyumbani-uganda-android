package com.ruraara.ken.e_nyumbani.dummy;

import com.ruraara.ken.e_nyumbani.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 11/4/17.
 */

public class PropertyDetail {
    public final String id;
    public final String title;
    public final String description;
    public final int rating;
    public final String noReviews;
    public final String address;
    public final String type;
    public final String status;
    public final String agent;
    public final String price;
    public final String image;
    public final JSONArray otherImagesJson;

    public static List<String> otherImages = new ArrayList<>();

    private int numberOfOtherImages;

    public PropertyDetail(String id, String title, String descrition,
                          int rating, String noReviews,
                          String address,
                          String type, String status, String agent,
                          String price, String image, JSONArray otherImagesJson) {

        clearOtherImages();

        this.id = id;
        this.title = title;
        this.description = descrition;
        this.rating = rating;
        this.noReviews = noReviews;
        this.address = address;
        this.type = type;
        this.status = status;
        this.agent = agent;
        this.price = price;
        this.image = image;
        this.otherImagesJson = otherImagesJson;
    }


    public List<String> getOtherImages() {

        try {

            for (int i = 0; i < otherImagesJson.length(); i++) {
                JSONObject jsonObject = otherImagesJson.getJSONObject(i);
                String image = jsonObject.getString("image");

                otherImages.add(image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return otherImages;
    }

    public void clearOtherImages() {
        if (otherImages != null) {
            otherImages.clear();
        }

    }

    public int getNoOfOtherImages() {
        numberOfOtherImages = otherImages.size();
        return numberOfOtherImages;
    }

    @Override
    public String toString() {
        return title;
    }
}
