package com.example.geeksreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewDataModel {

    private String ReviewID;
    private String BookCoverPicture;
    private String UserProfilePicture;
    private String UserName;
    private String NLikes;
    private String NComments;
    private String ReviewText;
    private String UserWhoWroteID;

    /**
     * fromJson: Put Data from a single JSONOBJECT into a UserDataModel
     *
     * @param jsonObject
     * @return Single User Prototype
     */
    private static ReviewDataModel fromJson(JSONObject jsonObject) {
        ReviewDataModel DummyUser = new ReviewDataModel();
        // Deserialize json into object fields
        try {
            //TODO: Check with backend the missing data!
            DummyUser.BookCoverPicture = jsonObject.getString("photourl");
            DummyUser.UserProfilePicture = jsonObject.getString("UserPicLink");
            DummyUser.UserName = jsonObject.getString("username");
            DummyUser.NLikes = jsonObject.getString("likes");
            DummyUser.NComments = jsonObject.getString("NumberComments");
            DummyUser.ReviewText = jsonObject.getString("body");
            DummyUser.ReviewID = jsonObject.getString("ReviewID");
            DummyUser.UserWhoWroteID = jsonObject.getString("UserWhoWroteID");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return DummyUser;
    }

    /**
     * fromJson: Function Takes jsonArray and divides it into
     * several json objects to be put in the form of UserDataModel
     *
     * @param jsonArray
     * @return ArrayList of Users Prototypes
     */
    public static ArrayList<ReviewDataModel> fromJson(JSONArray jsonArray) {
        JSONObject UsersJson;
        ArrayList<ReviewDataModel> AllUsers = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to UserModel object
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                UsersJson = jsonArray.getJSONObject(i);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            ReviewDataModel Model = ReviewDataModel.fromJson(UsersJson);

            if (Model != null) {
                AllUsers.add(i, Model);
            }

        }

        return AllUsers;
    }

    /**
     * getName: Gets Name of Current DataModelUser (Follower or Following)
     *
     * @return User_Name
     */
    String getBookCoverPicture() {
        return BookCoverPicture;
    }

    /**
     * getPicLink: Gets Profile Picture Url of Current DataModelUser (Follower or Following)
     *
     * @return Picture_Url
     */
    String getUserProfilePicture() {
        return UserProfilePicture;
    }

    /**
     * getNLikes: Gets Number of Likes of Current Review
     *
     * @return NLikes
     */
    String getNLikes() {
        return NLikes;
    }

    /**
     * getNComments: Gets Number of comments of Current Review
     *
     * @return NComments
     */
    String getNComments() {
        return NComments;
    }

    /**
     * getReviewText: Gets Text of Current Review
     *
     * @return ReviewText
     */
    String getReviewText() {
        return ReviewText;
    }

    /**
     * getUserName: Gets Text of Current Review
     *
     * @return ReviewText
     */
    String getUserName() {
        return UserName;
    }

    /**
     * getReviewID: Gets Id of current Review
     *
     * @return ReviewID
     */
    String getReviewID() {
        return ReviewID;
    }


    /**
     * getUserwhoWroteID: Gets Id of user who wrote the review
     *
     * @return UserwhoWroteID
     */
    String getUserWhoWroteID() {
        return UserWhoWroteID;
    }

}