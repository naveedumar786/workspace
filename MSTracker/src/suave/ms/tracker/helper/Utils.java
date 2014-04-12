package suave.ms.tracker.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

public class Utils {

	public static void savePreferences(Activity activity, String key,
			String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void savePreferences(Activity activity, String key, int value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void savePreferences(Activity activity, String key,
			boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String readPreferences(Activity activity, String key,
			String defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		return sp.getString(key, defaultValue);
	}

	public static int readPreferences(Activity activity, String key,
			int defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		return sp.getInt(key, defaultValue);
	}

	public static boolean readPreferences(Activity activity, String key,
			boolean defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		return sp.getBoolean(key, defaultValue);
	}

	public static void clearPreferences(Activity activity, String key) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void hideSoftkeybord(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

}
