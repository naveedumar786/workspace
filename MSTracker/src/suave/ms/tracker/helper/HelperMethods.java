package suave.ms.tracker.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class HelperMethods {

	public static void showToast(Context context, String message) {

		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			return activeNetworkInfo != null;
		} catch (Exception e) {

		}

		return false;
	}

	public static boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static int getStringResourceByName(String aString, Context context) {
		String packageName = context.getPackageName();
		int resId = context.getResources().getIdentifier(aString, "string",
				packageName);
		return resId;
	}

	public static double distanceBetweenTwoLatLongs(Location oldLocation,
			Location newLocation) {
		// double lat = 31.486843;
		// double lan = 74.293180;
		// double currentLat = myLocation.getLatitude();
		// double currentLan = myLocation.getLongitude();
		//
		double distance = 0;
		// Location locationB = new Location("B");
		// locationB.setLatitude(lat);
		// locationB.setLongitude(lan);
		// Log.e("newLocation is null", ";;");
		if (newLocation != null && oldLocation != null) {
			distance = oldLocation.distanceTo(newLocation); // in meters
		} else {
			if (newLocation == null) {
				Log.e("newLocation is null", ";;");
			}
			if (oldLocation == null) {
				Log.e("oldLocation is null", ";;");
			}
		}
		Log.e("distanceBetweenTwoLatLongs", "" + distance / 1000);
		return distance / 1000; // Convert meters to KM
	}

	public static String getFormatedDate(String strDate, String inFormate,
			String outFormate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(inFormate);
		Date myDate = null;
		try {
			myDate = dateFormat.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat(outFormate);
		String finalDate = timeFormat.format(myDate);

		return finalDate;
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static void hiddenKeyboard(Activity activity) {
		try {
			InputMethodManager input = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);

			input.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
