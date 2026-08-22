package com.google.android.libraries.inputmethod.preferencewidgets;

import android.app.Activity;
import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class CommonPreferenceFragment extends PreferenceFragmentCompat {
    public PreferenceScreen n() { return getPreferenceScreen(); }
    public Context x() { return getContext(); }
    public Activity E() { return getActivity(); }
    protected void aC() {}
    @Override
    public void onCreatePreferences(android.os.Bundle savedInstanceState, String rootKey) {}
}
