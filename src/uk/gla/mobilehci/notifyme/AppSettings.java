package uk.gla.mobilehci.notifyme;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by Rafael on 3/8/2015.
 */
public class AppSettings extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.xml.app_settings);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MyPreferenceFragment())
				.commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.app_settings);

			setSummary(this.getPreferenceScreen());
		}

		private void setSummary(PreferenceScreen prefScreen) {

			for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {

				PreferenceCategory prefCat = (PreferenceCategory) prefScreen
						.getPreference(i);

				for (int f = 0; f < prefCat.getPreferenceCount(); f++) {

					Preference pref = prefCat.getPreference(f);

					if (pref instanceof EditTextPreference) {
						EditTextPreference edPref = (EditTextPreference) pref;
						pref.setSummary(edPref.getText());
					}
				}

			}
		}

	}
}
