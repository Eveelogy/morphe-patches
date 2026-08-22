package app.evee.extension.gboard;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.libraries.inputmethod.preferencewidgets.CommonPreferenceFragment;
import java.lang.reflect.Method;

public class MorpheSettingsFragment extends CommonPreferenceFragment {

    @Override
    public int aB() {
        return 0x7f170f20; // R.xml.setting_expression
    }

    @Override
    protected void aC() {
        super.aC();
        try {
            PreferenceScreen screen = n();
            if (screen == null) return;
            Context context = x();
            if (context == null) context = E();
            if (context == null) context = GboardSettings.getAppContext();
            if (context == null) return;

            // Remove all existing items from the template layout
            try {
                Method clearMethod = screen.getClass().getMethod("ak");
                clearMethod.invoke(screen);
            } catch (Throwable t) {
                try {
                    Method clearMethod = screen.getClass().getMethod("removeAll");
                    clearMethod.invoke(screen);
                } catch (Throwable t2) {
                    try {
                        Method countMethod = screen.getClass().getMethod("k");
                        Method getMethod = screen.getClass().getMethod("o", int.class);
                        Method removeMethod = screen.getClass().getMethod("ao", Preference.class);
                        while (((Integer) countMethod.invoke(screen)) > 0) {
                            Object item = getMethod.invoke(screen, 0);
                            removeMethod.invoke(screen, item);
                        }
                    } catch (Throwable ignored) {}
                }
            }

            // General Category
            PreferenceCategory generalCat = new PreferenceCategory(context);
            setPrefTitle(generalCat, "General");
            addPref(screen, generalCat);

            // SafeSearch Switch
            SwitchPreferenceCompat safeSearchPref = new SwitchPreferenceCompat(context);
            setPrefKey(safeSearchPref, GboardSettings.KEY_DISABLE_SAFESEARCH);
            setPrefTitle(safeSearchPref, "Disable GIF SafeSearch");
            setPrefSummary(safeSearchPref, "Allow unrestricted Tenor GIF search results");
            setPrefChecked(safeSearchPref, GboardSettings.isSafeSearchDisabled());
            safeSearchPref.setOnPreferenceChangeListener((pref, newValue) -> {
                boolean val = (Boolean) newValue;
                GboardSettings.setSafeSearchDisabled(val);
                return true;
            });
            addPref(generalCat, safeSearchPref);

            // Meme Search Switch
            SwitchPreferenceCompat memeSearchPref = new SwitchPreferenceCompat(context);
            setPrefKey(memeSearchPref, GboardSettings.KEY_MEME_SEARCH);
            setPrefTitle(memeSearchPref, "Meme Search & Maker");
            setPrefSummary(memeSearchPref, "Replace the emoticon tab with still image meme search & maker");
            setPrefChecked(memeSearchPref, GboardSettings.isMemeSearchEnabled());
            memeSearchPref.setOnPreferenceChangeListener((pref, newValue) -> {
                boolean val = (Boolean) newValue;
                GboardSettings.setMemeSearchEnabled(val);
                return true;
            });
            addPref(generalCat, memeSearchPref);

            // Actions Category
            PreferenceCategory actionCat = new PreferenceCategory(context);
            setPrefTitle(actionCat, "Actions");
            addPref(screen, actionCat);

            // Restart Gboard Preference
            Preference restartPref = new Preference(context);
            setPrefKey(restartPref, "restart_gboard");
            setPrefTitle(restartPref, "Restart Gboard");
            setPrefSummary(restartPref, "Restart the keyboard process to reload settings");
            restartPref.setOnPreferenceClickListener(pref -> {
                Context ctx = pref.getContext();
                Toast.makeText(ctx, "Restarting Gboard...", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                }, 400);
                return true;
            });
            addPref(actionCat, restartPref);

        } catch (Throwable ignored) {
        }
    }

    private static void setPrefTitle(Preference pref, String title) {
        try {
            pref.setTitle(title);
        } catch (Throwable t) {
            try {
                Method m = pref.getClass().getMethod("N", CharSequence.class);
                m.invoke(pref, title);
            } catch (Throwable ignored) {}
        }
    }

    private static void setPrefSummary(Preference pref, String summary) {
        try {
            pref.setSummary(summary);
        } catch (Throwable t) {
            try {
                Method m = pref.getClass().getMethod("L", CharSequence.class);
                m.invoke(pref, summary);
            } catch (Throwable ignored) {}
        }
    }

    private static void setPrefKey(Preference pref, String key) {
        try {
            pref.setKey(key);
        } catch (Throwable t) {
            try {
                pref.getClass().getField("r").set(pref, key);
            } catch (Throwable ignored) {}
        }
    }

    private static void setPrefChecked(SwitchPreferenceCompat pref, boolean checked) {
        try {
            pref.setChecked(checked);
        } catch (Throwable t) {
            try {
                Method m = pref.getClass().getMethod("k", boolean.class);
                m.invoke(pref, checked);
            } catch (Throwable ignored) {}
        }
    }

    private static void addPref(Object group, Preference child) {
        try {
            Method m = group.getClass().getMethod("addPreference", Preference.class);
            m.invoke(group, child);
        } catch (Throwable t) {
            try {
                Method m = group.getClass().getMethod("an", Preference.class);
                m.invoke(group, child);
            } catch (Throwable ignored) {}
        }
    }
}
