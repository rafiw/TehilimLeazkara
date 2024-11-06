package com.solve.it

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.ListPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import android.text.TextUtils
import android.util.Log

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
    override fun onDestroy() {
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener { _, _ -> }
        super.onDestroy()
    }
}