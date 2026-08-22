package app.evee.extension.gboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.libraries.inputmethod.preferencewidgets.CommonPreferenceFragment;

public class MorpheSettingsFragment extends CommonPreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getContext();
        if (context == null) context = getActivity();
        if (context == null) context = GboardSettings.getAppContext();
        if (context == null) return;

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(screen);

        // General Category
        PreferenceCategory generalCat = new PreferenceCategory(context);
        generalCat.setTitle("General");
        screen.addPreference(generalCat);

        // SafeSearch Switch
        SwitchPreferenceCompat safeSearchPref = new SwitchPreferenceCompat(context);
        safeSearchPref.setKey(GboardSettings.KEY_DISABLE_SAFESEARCH);
        safeSearchPref.setTitle("Disable GIF SafeSearch");
        safeSearchPref.setSummary("Allow unrestricted Tenor GIF search results");
        safeSearchPref.setDefaultValue(true);
        generalCat.addPreference(safeSearchPref);

        // Meme Search Switch
        SwitchPreferenceCompat memeSearchPref = new SwitchPreferenceCompat(context);
        memeSearchPref.setKey(GboardSettings.KEY_MEME_SEARCH);
        memeSearchPref.setTitle("Meme Search & Maker");
        memeSearchPref.setSummary("Replace the emoticon tab with still image meme search & maker");
        memeSearchPref.setDefaultValue(true);
        generalCat.addPreference(memeSearchPref);

        // Actions Category
        PreferenceCategory actionCat = new PreferenceCategory(context);
        actionCat.setTitle("Actions");
        screen.addPreference(actionCat);

        // Restart Gboard Preference
        Preference restartPref = new Preference(context);
        restartPref.setKey("restart_gboard");
        restartPref.setTitle("Restart Gboard");
        restartPref.setSummary("Restart the keyboard process to reload settings");
        restartPref.setOnPreferenceClickListener(pref -> {
            Toast.makeText(pref.getContext(), "Restarting Gboard...", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Process.killProcess(Process.myPid());
                System.exit(0);
            }, 400);
            return true;
        });
        actionCat.addPreference(restartPref);
    }

    @Override
    protected void aC() {
        super.aC();
        if (getPreferenceScreen() == null || getPreferenceScreen().getPreferenceCount() == 0) {
            onCreatePreferences(null, null);
        }
    }
}
