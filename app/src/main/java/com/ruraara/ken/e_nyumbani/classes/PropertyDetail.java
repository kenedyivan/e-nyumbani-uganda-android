package com.ruraara.ken.e_nyumbani.classes;

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
    public final String currency;
    public final String image;
    public final JSONArray otherImagesJson;
    public final JSONArray reviewsJson;

    public static List<String> otherImages = new ArrayList<>();
    public static List<Review> reviews = new ArrayList<>();

    private int numberOfOtherImages;
    private int numberOfReviews;

    public PropertyDetail(String id, String title, String descrition,
                          int rating, String noReviews,
                          String address,
                          String type, String status, String agent,
                          String price, String currency, String image,
                          JSONArray otherImagesJson, JSONArray reviewsJson) {

        clearOtherImages();
        clearOtherReviews();

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
        this.currency = currency;
        this.image = image;
        this.otherImagesJson = otherImagesJson;
        this.reviewsJson = reviewsJson;
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

    //Returns property reviews
    public List<Review> getReviews() {

        try {

            for (int i = 0; i < reviewsJson.length(); i++) {
                JSONObject jsonObject = reviewsJson.getJSONObject(i);
                String id = jsonObject.getString("id");
                int rating = jsonObject.getInt("rating");
                String review = jsonObject.getString("review");
                String username = jsonObject.getString("username");
                String profile_picture = jsonObject.getString("profile_picture");

                reviews.add(new Review(id,(double)rating,review,username,profile_picture));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public void clearOtherReviews(){
        if (reviews != null) {
            reviews.clear();
        }
    }

    public int getNoOfReviews(){
        numberOfReviews = reviews.size();
        return numberOfReviews;
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

    public static class Review {
        public final String id;
        public final double rating;
        public final String review;
        public final String username;
        public final String profile_picture;

        public Review(String id, double rating, String review, String username, String profile_picture) {
            this.id = id;
            this.rating = rating;
            this.review = review;
            this.username = username;
            this.profile_picture = profile_picture;
        }

        @Override
        public String toString() {
            return review;
        }
    }
}
