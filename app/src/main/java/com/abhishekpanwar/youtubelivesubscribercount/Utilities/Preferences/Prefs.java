package com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences;

import android.content.Context;
import android.support.annotation.NonNull;

public class Prefs {

    private static SharedPrefs sharedPrefs;

    public static void init(@NonNull Context context) {
        if (sharedPrefs != null) {
            throw new RuntimeException("Prefs has already been instantiated");
        }
        sharedPrefs = new SharedPrefs(context);
    }

    public static boolean getNightMode() {
        return getPrefs().get(Keys.NIGHT_MODE, false);
    }

    public static void setNightMode(boolean nightMode) {
        getPrefs().put(Keys.NIGHT_MODE, nightMode);
    }

    public static void setModeChanged(boolean modeChanged) {
        getPrefs().put(Keys.NIGHT_MODE_CHANGED, modeChanged);
    }

    public static boolean getModeChanged() {
        return getPrefs().get(Keys.NIGHT_MODE_CHANGED,false);
    }

    @NonNull
    private static SharedPrefs getPrefs() {
        if (sharedPrefs == null) {
            throw new RuntimeException("Prefs has not been instantiated. Call init() with context");
        }
        return sharedPrefs;
    }
}
