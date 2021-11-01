package goma.minesweeper

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencescreen, rootKey)

    }
}