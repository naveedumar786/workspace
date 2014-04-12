package suave.ms.tracker;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SlidingMainMenu extends Fragment implements OnClickListener {

	RelativeLayout rlSchedule, rlDiagnosis, rlTreatment, rlImages,
			rlMsrelapesExacerbation, rlLabs, rlMsProfile, rlMsResources,
			rlAboutMs, rlInjectionTracker, rlSymptoms, rlLogout;

	static Context context;

	static LinearLayout linearLayoutListView;
	Button btnBack;

	public static SlidingMainMenu newInstance(int position) {
		SlidingMainMenu f = new SlidingMainMenu();
		Bundle b = new Bundle();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();

		View view = inflater.inflate(R.layout.left_menu, container, false);

		rlSchedule = (RelativeLayout) view.findViewById(R.id.rlSchedule);
		rlSchedule.setOnClickListener(this);

		rlTreatment = (RelativeLayout) view.findViewById(R.id.rlTreatment);
		rlTreatment.setOnClickListener(this);

		rlDiagnosis = (RelativeLayout) view.findViewById(R.id.rlDiagnosis);
		rlDiagnosis.setOnClickListener(this);

		rlImages = (RelativeLayout) view.findViewById(R.id.rlImages);
		rlImages.setOnClickListener(this);

		rlMsrelapesExacerbation = (RelativeLayout) view
				.findViewById(R.id.rlMsrelapesExacerbation);
		rlMsrelapesExacerbation.setOnClickListener(this);

		rlLabs = (RelativeLayout) view.findViewById(R.id.rlLabs);
		rlLabs.setOnClickListener(this);

		rlMsProfile = (RelativeLayout) view.findViewById(R.id.rlMsProfile);
		rlMsProfile.setOnClickListener(this);

		rlMsResources = (RelativeLayout) view.findViewById(R.id.rlMsResources);
		rlMsResources.setOnClickListener(this);

		rlAboutMs = (RelativeLayout) view.findViewById(R.id.rlAboutMs);
		rlAboutMs.setOnClickListener(this);

		rlInjectionTracker = (RelativeLayout) view
				.findViewById(R.id.rlInjectionTracker);
		rlInjectionTracker.setOnClickListener(this);

		rlSymptoms = (RelativeLayout) view.findViewById(R.id.rlSymptoms);
		rlSymptoms.setOnClickListener(this);

		rlLogout = (RelativeLayout) view.findViewById(R.id.rlLogout);
		rlLogout.setOnClickListener(this);

		return view;
	}

	public void intentCall(Context context, Class<?> cls) {
		((Activity) context).finish();
		Intent intent = new Intent(context, cls);
		startActivity(intent);
		((Activity) context).finish();
		// if (context == Home) {
		//
		// }else {
		// context.finish();
		// }

		// if (Home.MainMenu != null) {
		// Home.MainMenu.toggle();
		// }
		// else {
		// Home.MainMenu.toggle();
		// }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlSchedule:
			intentCall(context, Schedule.class);
			break;
		case R.id.rlTreatment:
			intentCall(context, Treatment.class);
			break;
		case R.id.rlDiagnosis:
			intentCall(context, Diagnosis.class);
			break;
		case R.id.rlImages:
			HelperMethods.showToast(context, "Images under Development.");
			// intentCall(context, MSImages.class);
			break;
		case R.id.rlMsrelapesExacerbation:
			HelperMethods.showToast(context,
					"MS Relapes Exacerbation under Development.");
			// intentCall(context, MSRelapesExacerbation.class);
			break;
		case R.id.rlLabs:
			HelperMethods.showToast(context, "Lab under Development.");
			// intentCall(context, Lab.class);
			break;
		case R.id.rlMsProfile:
			HelperMethods.showToast(context, "Diagnosis under Development.");
			// intentCall(context, MSProfile.class);
			break;
		case R.id.rlMsResources:
			HelperMethods.showToast(context, "MS Resources under Development.");
			// intentCall(context, MSResources.class);
			break;
		case R.id.rlAboutMs:
			HelperMethods.showToast(context, "About MS under Development.");
			// intentCall(context, AboutMs.class);
			break;
		case R.id.rlInjectionTracker:
			HelperMethods.showToast(context,
					"Injection Tracker under Development.");
			// intentCall(context, InjectionTracker.class);
			break;
		case R.id.rlSymptoms:
			HelperMethods.showToast(context, "Symptoms under Development.");
			// intentCall(context, Symptoms.class);
			break;
		case R.id.rlLogout:
			logout();
			// intentCall(context, Symptoms.class);
			break;
		}
	}

	private void logout() {
		Utils.clearPreferences((Activity) context, "userId");
		Utils.clearPreferences((Activity) context, "userEmail");
		intentCall(context, Login.class);
	}

}
