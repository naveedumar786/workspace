package suave.ms.tracker;

import suave.ms.tracker.R.menu;
import suave.ms.tracker.fragments.ImagesCategoryFragment;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class MSTrackerHome extends MainActivity implements OnTouchListener {
	public static SlidingMenu MainMenu;
	/***** Buttons start ********/
	RelativeLayout btnSchedule, btnDiagnosis, btnTreatment, btnImages,
			btnMsrelapesExacerbation, btnLabs, btnMsProfile, btnMsResources,
			btnAboutMs, btnInjectionTracker, btnSymptoms;
	Button btnMenu;

	TextView tvScheduleText, tvDiagnosisText, tvTreatmentText, tvImagesText,
			tvMsrelapesExacerbationText, tvLabsText, tvMsProfileText,
			tvMsResourcesText, tvAboutMsText, tvInjectionTrackerText,
			tvSymptomsText;
	private String Tag = "MSTrackerHome ";

	/***** Buttons start ********/
	// Bottom menu handler also define in MianActivity.java file for global
	// schop

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mstracker);

		findViewById(R.id.btnHome).setBackgroundResource(R.drawable.home_t);
		((TextView) findViewById(R.id.btnHomeText)).setTextColor(Color
				.parseColor(Globals.tabedTextColor));

		btnMenu = (Button) findViewById(R.id.btnMenu);
		// btnMenu
		btnMenu.setOnTouchListener(this);

		btnSchedule = (RelativeLayout) findViewById(R.id.btnSchedule);
		tvScheduleText = (TextView) findViewById(R.id.tvScheduleText);
		btnSchedule.setOnTouchListener(this);

		btnDiagnosis = (RelativeLayout) findViewById(R.id.btnDiagnosis);
		tvDiagnosisText = (TextView) findViewById(R.id.tvDiagnosisText);
		btnDiagnosis.setOnTouchListener(this);

		btnTreatment = (RelativeLayout) findViewById(R.id.btnTreatment);
		tvTreatmentText = (TextView) findViewById(R.id.tvTreatmentText);
		btnTreatment.setOnTouchListener(this);

		btnImages = (RelativeLayout) findViewById(R.id.btnImages);
		tvImagesText = (TextView) findViewById(R.id.tvImagesText);
		btnImages.setOnTouchListener(this);

		btnMsrelapesExacerbation = (RelativeLayout) findViewById(R.id.btnMsrelapesExacerbation);
		tvMsrelapesExacerbationText = (TextView) findViewById(R.id.tvMsrelapesExacerbationText);
		btnMsrelapesExacerbation.setOnTouchListener(this);

		btnLabs = (RelativeLayout) findViewById(R.id.btnLabs);
		tvLabsText = (TextView) findViewById(R.id.tvLabsText);
		btnLabs.setOnTouchListener(this);

		btnMsProfile = (RelativeLayout) findViewById(R.id.btnMsProfile);
		tvMsProfileText = (TextView) findViewById(R.id.tvMsProfileText);
		btnMsProfile.setOnTouchListener(this);

		btnMsResources = (RelativeLayout) findViewById(R.id.btnMsResources);
		tvMsResourcesText = (TextView) findViewById(R.id.tvMsResources);
		btnMsResources.setOnTouchListener(this);

		btnAboutMs = (RelativeLayout) findViewById(R.id.btnAboutMs);
		tvAboutMsText = (TextView) findViewById(R.id.tvAboutMs);
		btnAboutMs.setOnTouchListener(this);

		btnInjectionTracker = (RelativeLayout) findViewById(R.id.btnInjectionTracker);
		tvInjectionTrackerText = (TextView) findViewById(R.id.tvInjectionTracker);
		btnInjectionTracker.setOnTouchListener(this);

		btnSymptoms = (RelativeLayout) findViewById(R.id.btnSymptoms);
		tvSymptomsText = (TextView) findViewById(R.id.tvSymptoms);
		btnSymptoms.setOnTouchListener(this);

		// check network status
		if (!HelperMethods.isNetworkAvailable(getApplication())) {
			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
		}
		/***************** Sliding Menu Start ********************/
		Display mDisplay = ((Activity) MSTrackerHome.this).getWindowManager()
				.getDefaultDisplay();
		int width = mDisplay.getWidth();

		Log.e("width", "" + width);

		MainMenu = new SlidingMenu(this);
		MainMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		MainMenu.setShadowWidth(50);
		MainMenu.setFadeEnabled(true);
		MainMenu.setFadeDegree(1.0f);
		MainMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		MainMenu.setMenu(R.layout.menu_frame_filter);
		MainMenu.setBehindWidth((int) Math.round(width / 1.4));
		MainMenu.setMode(SlidingMenu.LEFT);
		MainMenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				// SearchMenu.setSlidingEnabled(false);
			}
		});
		MainMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// SearchMenu.setSlidingEnabled(true);
			}
		});
		MainMenu.setAnimation(null);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_filter, new SlidingMainMenu())
				.commit();
		/***************** Sliding Menu End ********************/

		Log.e(Tag,
				"vlaues_folder_name = "
						+ getResources().getString(R.string.vlaues_folder_name));
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		finish();
	}

	String tabedTextColor = "#007eff";
	String simpleTextColor = "#121212";

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			doOnDownAction(v);
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			doOnUpAction(v);
			break;
		}

		v.invalidate();

		return true;
	}

	private void doOnUpAction(View v) {
		switch (v.getId()) {
		case R.id.btnMenu:
			v.setBackgroundResource(R.drawable.sidemenu);
			MainMenu.toggle();
			break;
		case R.id.btnSchedule:
			v.setBackgroundResource(R.drawable.schedule);
			tvScheduleText.setTextColor(Color.parseColor(simpleTextColor));
			startActivity(Schedule.class);
			break;

		case R.id.btnDiagnosis:
			v.setBackgroundResource(R.drawable.diagnosis);
			tvDiagnosisText.setTextColor(Color.parseColor(simpleTextColor));
			startActivity(Diagnosis.class);
			break;

		case R.id.btnTreatment:
			v.setBackgroundResource(R.drawable.treatment);
			tvTreatmentText.setTextColor(Color.parseColor(simpleTextColor));
			startActivity(Treatment.class);
			break;
		case R.id.btnImages:

			v.setBackgroundResource(R.drawable.images);
			tvImagesText.setTextColor(Color.parseColor(simpleTextColor));

			Intent intent = new Intent(this, Images.class);
			intent.putExtra("isFromImages", true);
			startActivity(intent);
			finish();
			break;

		case R.id.btnMsrelapesExacerbation:
			v.setBackgroundResource(R.drawable.ms_relapes_exacerbation);
			tvMsrelapesExacerbationText.setTextColor(Color
					.parseColor(simpleTextColor));
			break;

		case R.id.btnLabs:
			v.setBackgroundResource(R.drawable.labs);
			tvLabsText.setTextColor(Color.parseColor(simpleTextColor));

			Intent intent1 = new Intent(this, Images.class);
			intent1.putExtra("isFromImages", false);
			startActivity(intent1);
			finish();

			break;

		case R.id.btnMsProfile:
			v.setBackgroundResource(R.drawable.ms_profile);
			tvMsProfileText.setTextColor(Color.parseColor(simpleTextColor));
			startActivity(MSProfile.class);
			break;

		case R.id.btnMsResources:
			v.setBackgroundResource(R.drawable.ms_resources);
			tvMsResourcesText.setTextColor(Color.parseColor(simpleTextColor));
			break;

		case R.id.btnAboutMs:
			v.setBackgroundResource(R.drawable.about_ms);
			tvAboutMsText.setTextColor(Color.parseColor(simpleTextColor));

			startActivity(AboutMS.class);
			break;

		case R.id.btnInjectionTracker:
			v.setBackgroundResource(R.drawable.injection_tracker);
			tvInjectionTrackerText.setTextColor(Color
					.parseColor(simpleTextColor));

			startActivity(InjectionTracker.class);
			break;

		case R.id.btnSymptoms:
			v.setBackgroundResource(R.drawable.symptoms);
			tvSymptomsText.setTextColor(Color.parseColor(simpleTextColor));
			break;

		}
	}

	private void startActivity(Class className) {
		Intent intent = new Intent(this, className);
		startActivity(intent);
		// overridePendingTransition(17432579, 17432578);
		finish();

	}

	private void doOnDownAction(View v) {

		switch (v.getId()) {
		case R.id.btnMenu:
			v.setBackgroundResource(R.drawable.sidemenu_t);
			v.invalidate();
			break;

		case R.id.btnSchedule:
			v.setBackgroundResource(R.drawable.schedule_t);
			tvScheduleText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnDiagnosis:
			v.setBackgroundResource(R.drawable.diagnosis_t);
			tvDiagnosisText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;
		case R.id.btnTreatment:
			v.setBackgroundResource(R.drawable.treatment_t);
			tvTreatmentText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnImages:
			v.setBackgroundResource(R.drawable.images_t);
			tvImagesText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnMsrelapesExacerbation:
			v.setBackgroundResource(R.drawable.ms_relapes_exacerbation_t);
			tvMsrelapesExacerbationText.setTextColor(Color
					.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnLabs:
			v.setBackgroundResource(R.drawable.labs_t);
			tvLabsText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnMsProfile:
			v.setBackgroundResource(R.drawable.ms_profile_t);
			tvMsProfileText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnMsResources:
			v.setBackgroundResource(R.drawable.ms_resources_t);
			tvMsResourcesText.setTextColor(Color.parseColor(tabedTextColor));
			startActivity(MSResources.class);
			v.invalidate();
			break;

		case R.id.btnAboutMs:
			v.setBackgroundResource(R.drawable.about_ms_t);
			tvAboutMsText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnInjectionTracker:
			v.setBackgroundResource(R.drawable.injection_tracker_t);
			tvInjectionTrackerText.setTextColor(Color
					.parseColor(tabedTextColor));
			v.invalidate();
			break;

		case R.id.btnSymptoms:
			v.setBackgroundResource(R.drawable.symptoms_t);
			tvSymptomsText.setTextColor(Color.parseColor(tabedTextColor));
			v.invalidate();
			break;

		}
	}

}
