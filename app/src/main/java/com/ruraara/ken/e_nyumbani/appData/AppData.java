package com.ruraara.ken.e_nyumbani.appData;

/**
 * Created by ken on 10/30/17.
 */

public class AppData {
    public static final String HOST = "http://10.0.3.2:8000";
    //static final String HOST = "http://192.168.43.28:5000";

    public static String getHost() {
        return HOST;
    }

    public static String loginUser() {
        return HOST + "/api/agent/login";
    }

    public static String loginSocialUser() {
        return HOST + "/api/agent/login-social-user";
    }

    public static String registerUser() {
        return HOST + "/api/agent/register";
    }

    public static String userProfile() {
        return HOST + "/api/agent/profile";
    }

    public static String userProfilePic() {
        return HOST + "/images/agents/profile_330x330/";
    }

    public static String getMyProperties() {
        return HOST + "/api/agent/my-properties";
    }

    public static String getFeatured() {
        return HOST + "/api/listings";
    }

    public static String getRent() {
        return HOST + "/api/listings/for-rent";
    }

    public static String getSale() {
        return HOST + "/api/listings/for-sale";
    }

    public static String search() {
        return HOST + "/api/listings/search";
    }

    public static String getProperty() {
        return HOST + "/api/listings/property";
    }

    public static String createProperty() {
        return HOST + "/api/listings/property/create";
    }

    public static String uploadPhoto() {
        return HOST + "/api/listings/property/upload-photo";
    }

    public static String getImagesPath() {
        return HOST + "/images/properties/agent_properties_120x120/";
    }

    public static String getRelatedPropertiesImagesPath() {
        return HOST + "/images/properties/latest_home_and_best_property_355x240/";
    }

    public static String getAgentsImagesPath() {
        return HOST + "/images/agents/profile_330x330/";
    }

    public static String getDetailsImagesPath() {
        return HOST + "/images/properties/property_listing_364x244/";
    }

    /**
     * Property agents
     */
    public static String getAgents() {
        return HOST + "/api/agents";
    }

    public static String getAgentProperties() {
        return HOST + "/api/agent/properties";
    }

    public static String setOtherDetails() {
        return HOST + "/api/agent/other-details";
    }

    public static String reviewProperty() {
        return HOST + "/api/review";
    }

    public static String addToFavorites() {
        return HOST + "/api/add-to-favorites";
    }

}
