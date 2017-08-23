package com.cosmos.saiedattallah.twitterclone.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by saeed on 23/08/17.
 */

@Parcel(analyze = { User.class })
@Table(name = "Users")
public class User extends Model {
    @Column(name = "Name")
    public String name;

    @Column(name = "UID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long uid;

    @Column(name = "ScreenName")
    public String screenName;

    @Column(name = "ProfileImageUrl")
    public String profileImageUrl;

    @Column(name = "Tagline")
    public String tagline;

    @Column(name = "FollowersCount")
    public int followersCount;

    @Column(name = "FollowingCount")
    public int followingCount;

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        String biggerImageUrl = profileImageUrl.replace("_normal","");
        return biggerImageUrl;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public long getUid() {
        return uid;
    }


    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    // empty constructor needed by the Parceler library
    public User() {
        super();
    }

    public static User findOrCreateFromJson(JSONObject jsonObject) {
        long rid = 0;
        try {
            rid = jsonObject.getLong("id");
            User existingUser = new Select().from(User.class).where("uid = ?",rid).executeSingle();
            if (existingUser != null) {
                return existingUser;
            } else {
                User user = User.fromJSON(jsonObject);
                user.save();
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
