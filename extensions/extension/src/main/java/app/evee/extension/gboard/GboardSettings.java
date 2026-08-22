package app.evee.extension.gboard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

@SuppressWarnings("unused")
public class GboardSettings {

    private static final String PREFS_NAME = "evee_gboard_preferences";
    public static final String KEY_DISABLE_SAFESEARCH = "disable_gif_safesearch";
    public static final String KEY_MEME_SEARCH = "replace_emoticon_with_meme_search";

    public static Context getAppContext() {
        try {
            Application app = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
            if (app != null) {
                return app.getApplicationContext();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static SharedPreferences getPrefs() {
        Context ctx = getAppContext();
        if (ctx != null) {
            return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return null;
    }

    public static boolean isSafeSearchDisabled() {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            return prefs.getBoolean(KEY_DISABLE_SAFESEARCH, true);
        }
        return true;
    }

    public static String getContentFilterLevel() {
        return isSafeSearchDisabled() ? "off" : "medium";
    }

    public static boolean isMemeSearchEnabled() {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            return prefs.getBoolean(KEY_MEME_SEARCH, true);
        }
        return true;
    }

    public static void setSafeSearchDisabled(boolean disabled) {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putBoolean(KEY_DISABLE_SAFESEARCH, disabled).apply();
        }
    }

    public static void setMemeSearchEnabled(boolean enabled) {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putBoolean(KEY_MEME_SEARCH, enabled).apply();
        }
    }

    public static void addMorphePreferenceCategory(Object fragmentObj) {
        try {
            if (!(fragmentObj instanceof PreferenceFragmentCompat)) {
                return;
            }
            PreferenceFragmentCompat fragment = (PreferenceFragmentCompat) fragmentObj;
            PreferenceScreen screen = fragment.getPreferenceScreen();
            if (screen == null) {
                return;
            }
            Context context = fragment.getContext();
            if (context == null) {
                context = fragment.getActivity();
            }
            if (context == null) {
                return;
            }

            if (screen.findPreference("morphe_settings_category") != null) {
                return;
            }

            PreferenceCategory category = new PreferenceCategory(context);
            category.setKey("morphe_settings_category");
            category.setTitle("Morphe Settings");
            screen.addPreference(category);

            SwitchPreferenceCompat safeSearchPref = new SwitchPreferenceCompat(context);
            safeSearchPref.setKey(KEY_DISABLE_SAFESEARCH);
            safeSearchPref.setTitle("Disable GIF SafeSearch");
            safeSearchPref.setSummary("Allow unrestricted Tenor GIF search results");
            safeSearchPref.setChecked(isSafeSearchDisabled());
            safeSearchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean val = (Boolean) newValue;
                setSafeSearchDisabled(val);
                return true;
            });
            category.addPreference(safeSearchPref);

            SwitchPreferenceCompat memeSearchPref = new SwitchPreferenceCompat(context);
            memeSearchPref.setKey(KEY_MEME_SEARCH);
            memeSearchPref.setTitle("Emoticon Tab as GIF / Meme Search");
            memeSearchPref.setSummary("Replace the emoticon keyboard with GIF / meme search");
            memeSearchPref.setChecked(isMemeSearchEnabled());
            memeSearchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean val = (Boolean) newValue;
                setMemeSearchEnabled(val);
                return true;
            });
            category.addPreference(memeSearchPref);
        } catch (Throwable ignored) {
        }
    }
}
