package com.abhishekpanwar.youtubelivesubscribercount;

import android.app.Application;

import com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences.Prefs;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.init(this);

    }
}
