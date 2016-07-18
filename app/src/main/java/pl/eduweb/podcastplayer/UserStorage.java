package pl.eduweb.podcastplayer;

import android.content.SharedPreferences;

import pl.eduweb.podcastplayer.api.UserResponse;

/**
 * Created by Autor on 2016-07-06.
 */
public class UserStorage {

    public static final String SESSION_TOKEN = "sessionToken";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_ID = "userId";
    private final SharedPreferences preferences;

    public UserStorage(SharedPreferences preferences) {

        this.preferences = preferences;
    }

    public void save(UserResponse userResponse) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SESSION_TOKEN, userResponse.sessionToken);
        editor.putString(USERNAME, userResponse.username);
        editor.putString(EMAIL, userResponse.email);
        editor.putString(FIRST_NAME, userResponse.firstName);
        editor.putString(LAST_NAME, userResponse.lastName);
        editor.putString(USER_ID, userResponse.objectId);
        editor.apply();

    }

    public boolean hasToLogin() {
        return preferences.getString(SESSION_TOKEN, "").isEmpty();
    }

    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }

    public String getFullName() {
        return String.format("%s %s", preferences.getString(FIRST_NAME, ""), preferences.getString(LAST_NAME, ""));
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public String getUserId() {
        return preferences.getString(USER_ID, "");
    }

    public String getToken() {
        return preferences.getString(SESSION_TOKEN, "");
    }
}
