package pl.eduweb.podcastplayer.screens.discover;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Autor on 2016-07-18.
 */
public class Subscription {

    public String userId;
    public long podcastId;

    @SerializedName("ACL")
    public JsonObject acl;
}
