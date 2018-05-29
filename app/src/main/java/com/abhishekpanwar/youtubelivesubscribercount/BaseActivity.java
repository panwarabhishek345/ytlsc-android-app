package com.abhishekpanwar.youtubelivesubscribercount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences.Prefs;

public class BaseActivity extends AppCompatActivity {


    public boolean nightmode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nightmode = Prefs.getNightMode();

        if(nightmode)
            setTheme(R.style.AppThemeNight);
        else
            setTheme(R.style.AppTheme);

    }
}
