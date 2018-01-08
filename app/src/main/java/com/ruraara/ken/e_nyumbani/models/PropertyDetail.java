package com.ruraara.ken.e_nyumbani.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 11/4/17.
 */

public class PropertyDetail {
    public String id = null;
    public String title = null;
    public String description = null;
    public int rating = 0;
    public String noReviews = null;
    public String address = null;
    public String type = null;
    public String status = null;
    public String agentId = null;
    public String agent = null;
    public String price = null;
    public String currency = null;
    public String createdAt = null;
    public int favorite = 0;
    public String image = null;
    public JSONArray otherImagesJson = null;
    public JSONArray reviewsJson = null;
    public JSONArray relatedPropertiesJson = null;

    public static List<String> otherImages = new ArrayList<>();
    public static List<Review> reviews = new ArrayList<>();
    public static List<RelatedProperty> relatedProperties = new ArrayList<>();

    private int numberOfOtherImages;
    private int numberOfReviews;
    private int numberOfRelatedProperties;

    public PropertyDetail(String id, String title, String descrition,
                          int rating, String noReviews,
                          String address,
                          String type, String status, String agentId, String agent,
                          String price, String currency, String image, String createdAt,
                          JSONArray otherImagesJson) {

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
        this.agentId = agentId;
        this.price = price;
        this.currency = currency;
        this.image = image;
        this.createdAt = createdAt;
        this.otherImagesJson = otherImagesJson;

    }

    public PropertyDetail(String id, String title, String descrition,
                          int rating, String noReviews,
                          String address,
                          String type, String status, String agentId, String agent,
                          String price, String currency, String image, int favorite, String createdAt,
                          JSONArray otherImagesJson, JSONArray reviewsJson, JSONArray relatedPropertiesJson) {

        clearOtherImages();
        clearOtherReviews();
        clearRelatedProperties();

        this.id = id;
        this.title = title;
        this.description = descrition;
        this.rating = rating;
        this.noReviews = noReviews;
        this.address = address;
        this.type = type;
        this.status = status;
        this.agent = agent;
        this.agentId = agentId;
        this.price = price;
        this.currency = currency;
        this.image = image;
        this.createdAt = createdAt;
        this.favorite = favorite;
        this.otherImagesJson = otherImagesJson;
        this.reviewsJson = reviewsJson;
        this.relatedPropertiesJson = relatedPropertiesJson;
    }

    public PropertyDetail() {

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
                String createdAt = jsonObject.getString("created_at");

                reviews.add(new Review(id, (double) rating, review, username, profile_picture, createdAt));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    //Updates reviews list with recently added by the user
    public void getReviews(Review r) {

        reviews.add(0, new Review(r.id, r.rating, r.review, r.username, r.profile_picture, r.createdAt));
    }

    public void updatePropertyRating(int rating) {
        this.rating = rating;
        this.noReviews = String.valueOf(getNoOfReviews());
    }

    //Returns related properties
    public List<RelatedProperty> getRelatedProperties() {
        try {
            for (int i = 0; i < relatedPropertiesJson.length(); i++) {
                JSONObject jsonObject = relatedPropertiesJson.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String image = jsonObject.getString("image");
                String status = jsonObject.getString("status");
                int rating = jsonObject.getInt("rating");

                relatedProperties.add(new RelatedProperty(id, title, image, status, (double) rating));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return relatedProperties;

    }

    //Clears other reviews list
    private void clearOtherReviews() {
        if (reviews != null) {
            reviews.clear();
        }
    }

    //Returns number of reviews in list
    public int getNoOfReviews() {
        numberOfReviews = reviews.size();
        return numberOfReviews;
    }

    //Clears other images list
    private void clearOtherImages() {
        if (otherImages != null) {
            otherImages.clear();
        }

    }

    //Returns number of other images in list
    public int getNoOfOtherImages() {
        numberOfOtherImages = otherImages.size();
        return numberOfOtherImages;
    }

    //Returns number of related properties in list
    public int getNumberOfRelatedProperties() {
        numberOfOtherImages = relatedProperties.size();
        return numberOfRelatedProperties;
    }

    //clears related properties list
    private void clearRelatedProperties() {
        if (relatedProperties != null) {
            relatedProperties.clear();
        }
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
        public final String createdAt;

        public Review(String id, double rating, String review, String username, String profile_picture, String created_at) {
            this.id = id;
            this.rating = rating;
            this.review = review;
            this.username = username;
            this.profile_picture = profile_picture;
            this.createdAt = created_at;
        }

        @Override
        public String toString() {
            return review;
        }
    }

    public static class RelatedProperty {
        public final String id;
        public final String title;
        public final String image;
        public final String status;
        public final double rating;

        RelatedProperty(String id, String title, String image, String status, double rating) {
            this.id = id;
            this.title = title;
            this.image = image;
            this.status = status;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public static class PropertyAgent {

        public static String firstName = null;
        public static String lastName = null;
        public static String company = null;

        public PropertyAgent(String firstName, String lastName, String company) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;

        }
    }
}
