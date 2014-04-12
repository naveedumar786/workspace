package suave.ms.tracker.helper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class Webservices {

	private static AsyncHttpClient client = new AsyncHttpClient();

	static JSONObject json;
	static String tag = "Webservices";
	private static boolean testMode = false;
	static String baseUrl;
	private OnTaskCompleted listener;

	public static void getData(final OnTaskCompleted listener,
			final String action, String parms) {

		client.setTimeout(15000);

		if (testMode) {
			baseUrl = "http://192.168.0.116/ms_tracker/webservice/executev1.php?task=";
		} else {
			baseUrl = "http://mslogs.com/ios/mstracker/executev1.php?task=";
		}

		String parameters = parms.length() > 0 ? '&' + parms : "";

		String webUrl = baseUrl + action + parameters;

		Log.e(tag, "Web URL = " + webUrl);

		client.get(webUrl, new AsyncHttpResponseHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					Log.e("response " + action, response);
					json = new JSONObject(response);

					listener.onSuccessWebservice(json, action);

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.e("onFailure getCategories webservice",
						"" + arg3.toString());
				listener.onFailureWebservice(true);

			}

			// @Override
			// public void onFailure(Throwable arg0) {
			// super.onFailure(arg0);
			// listener.onFailureWebservice(true);
			// Log.v("onFailure getCategories webservice",
			// "" + arg0.toString());
			// }
		});

	}
}
