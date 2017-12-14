package com.example.x.bossbandsapp;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.x.bossbandsapp.R.xml.settings_screen;

public class SettingsActivity extends AppCompatActivity{ // implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_background);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(android.R.id.content, settingsFragment, "settings_fragment");
        fragmentTransaction.commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // LOAD SETTINGS_SCREEN XML
            addPreferencesFromResource(settings_screen);
            setHasOptionsMenu(true); //
            final AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            final Preference switchPref = findPreference("switchSound");
            switchPref.setDefaultValue(true);
            switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isOn = (boolean) newValue;
                    if (!isOn) {
                        // IF SWITCH IS OFF
                        // MUTE AUDIO
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

                        // CUSTOM OFF TOAST
                        generateToast(getString(R.string.settings_toast_sound_off));

                        // DEFAULT TOAST FOR REFERENCE
                        //Toast toast = Toast.makeText(getActivity(), "Sound OFF", Toast.LENGTH_SHORT);
                        //toast.show();

                    } else {
                        // IF SWITCH IS ON
                        // UNMUTE AUDIO
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);

                        // CUSTOM ON TOAST
                        generateToast(getString(R.string.settings_toast_sound_on));
                    }
                    return true;
                }
            });

            final Preference switchVibratePref = findPreference("switchVibrate");
            switchVibratePref.setDefaultValue(true);
            switchVibratePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean vibrateOn = (boolean) newValue;
                    if (!vibrateOn) {
                        // IF SWITCH IS OFF
                        // DISABLE VIBRATION
                        audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                                AudioManager.VIBRATE_SETTING_ON);
                        audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                                AudioManager.VIBRATE_SETTING_ON);

                        // CUSTOM OFF TOAST
                        generateToast(getString(R.string.settings_toast_vibration_off));

                    } else {
                        // IF SWITCH IS ON
                        // ENABLE VIBRATION
                        audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                                AudioManager.VIBRATE_SETTING_OFF);
                        audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                                AudioManager.VIBRATE_SETTING_OFF);

                        // CUSTOM ON TOAST
                        generateToast(getString(R.string.settings_toast_vibration_on));

                        // VIBRATE AS FEEDBACK ?
                        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000); // 1 SECOND
                    }
                    return true;
                }
            });
        }

        public void generateToast(String passedString){
            Toast toast = Toast.makeText(getActivity(), passedString, Toast.LENGTH_SHORT);

            // SET TOAST TYPEFACE
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTextView = (TextView) toastLayout.getChildAt(0);

            // GET TYPEFACE FROM OUTER CONTEXT
            toastTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Jura-Bold.ttf"));

            // SET TOAST BACKGROUND COLOUR
            View view = toast.getView();
            view.setBackgroundColor(getResources().getColor(R.color.primaryDarkTrans));

            // SET TOAST POSITION + BACKGROUND BEHAVIOUR
            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
