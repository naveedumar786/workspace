package suave.ms.tracker;

import suave.ms.tracker.helper.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MSTracker extends Activity {
	String tag = "MSTracker";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new MyAsyncTask().execute();
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			int userId = Utils.readPreferences(MSTracker.this, "userId", 0);

			Log.e(tag + " ", " userId= " + userId);

			Intent intent;

			if (userId == 0) {
				intent = new Intent(MSTracker.this, Login.class);
			} else {
				intent = new Intent(MSTracker.this, MSTrackerHome.class);
			}

			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}
}
