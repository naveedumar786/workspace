package suave.ms.tracker;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {
	Context activeActivityContext;
	Button btnHome;
	static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
	}

	public static void _finish() {
		((Activity) context).finish();
	}

	// also define in MSTrackerHome.java file

	public void launchOnClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnHome:
			((Activity) activeActivityContext).finish();
			intent = new Intent(getApplicationContext(), MSTrackerHome.class);
			intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP
					| intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);

			break;
		case R.id.btnTodayMedication:
			intent = new Intent(getApplicationContext(), TodayMedication.class);
			startActivity(intent);

			break;
		case R.id.btnFutureAppointment:
			intent = new Intent(getApplicationContext(),
					FutureAppointment.class);
			startActivity(intent);
			break;
		case R.id.btnSettings:
			HelperMethods.showToast(getApplicationContext(),
					"Settings activity");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	};

}
