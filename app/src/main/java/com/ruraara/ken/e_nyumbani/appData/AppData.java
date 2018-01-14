package com.ruraara.ken.e_nyumbani.appData;

public class AppData {
    private static final String HOST = "https://api.nyumbaniuganda.com";
    //public static final String HOST = "http://10.0.3.2:8000";

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

    public static String getFavorites() {
        return HOST + "/api/my-favorites";
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

    public static String getMyProperty() {
        return HOST + "/api/agent/my-property";
    }

    public static String createProperty() {
        return HOST + "/api/listings/property/create";
    }

    public static String editProperty() {
        return HOST + "/api/listings/property/edit";
    }

    public static String savePropertyChanges() {
        return HOST + "/api/listings/property/save";
    }


    public static String uploadPhoto() {
        return HOST + "/api/listings/property/upload-photo";
    }

    public static String uploadProfilePicture() {
        return HOST + "/api/agent/upload-profile-picture";
    }


    public static String updatePhoto() {
        return HOST + "/api/listings/property/update-photo";
    }

    public static String getAgentsImagesPath() {
        return HOST + "/images/agents/profile_330x330/";
    }

    /**
     * FeaturedProperty agents
     */
    public static String getAgents() {
        return HOST + "/api/agents";
    }

    public static String agentProfile() {
        return HOST + "/api/agent";
    }

    public static String agentAccount() {
        return HOST + "/api/agent/account-details";
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

    /**
     * Update account details
     */
    public static String updateProfile() {
        return HOST + "/api/agent/update-profile";
    }

    public static String updateContact() {
        return HOST + "/api/agent/update-contact";
    }

    public static String updateCompany() {
        return HOST + "/api/agent/update-company";
    }

    public static String getPropertyTypes() {
        return HOST + "/api/listings/types";
    }

    public static String filterProperty() {
        return HOST + "/api/listings/filter";
    }


}
