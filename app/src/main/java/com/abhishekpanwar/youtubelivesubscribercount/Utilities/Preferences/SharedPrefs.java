package com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPrefs {

    private static final String PREFERENCES_NAME = "com.abhishekpanwar.youtubelivesubscribercount.SHARED_PREFS";
    private static final int PREFERENCES_MODE = Context.MODE_PRIVATE;

    private final SharedPreferences sharedPrefs;

    SharedPrefs(@NonNull Context context) {
        sharedPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE);
    }

    @NonNull
    private SharedPreferences.Editor getEditor() {
        return sharedPrefs.edit();
    }


    boolean get(@NonNull String key, boolean defaultValue) {
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    void put(@NonNull String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }
}
