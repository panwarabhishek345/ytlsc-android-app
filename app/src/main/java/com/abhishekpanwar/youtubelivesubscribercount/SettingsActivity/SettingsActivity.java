package com.abhishekpanwar.youtubelivesubscribercount.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.abhishekpanwar.youtubelivesubscribercount.BaseActivity;
import com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity.ChannelActivity;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.nightmodeSwitch)
    Switch nightModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(Prefs.getModeChanged())
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        ButterKnife.bind(this);

        nightmode = Prefs.getNightMode();
        nightModeSwitch.setChecked(nightmode);
        nightModeSwitch.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        boolean modeChanged = !Prefs.getModeChanged();
        Prefs.setModeChanged(modeChanged);

        if (b) {
            Prefs.setNightMode(true);
            restartSettings();
        } else {
            Prefs.setNightMode(false);
            restartSettings();
        }
    }

    private void restartSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (Prefs.getModeChanged()){
            Prefs.setModeChanged(false);
            Intent intent = new Intent(this, ChannelActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else {
            Prefs.setModeChanged(false);
            finish();
        }
    }
}
