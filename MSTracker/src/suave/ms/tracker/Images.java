package suave.ms.tracker;

import suave.ms.tracker.fragments.ImagesCategoryFragment;
import suave.ms.tracker.fragments.ImagesCategoryFragment.OnUpdateListener;
import suave.ms.tracker.fragments.ImagesFullscreenFragment;
import suave.ms.tracker.fragments.ImagesImagesFragment;
import suave.ms.tracker.helper.ImagesClass;
import suave.ms.tracker.helper.LoadingDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

public class Images extends MainActivity implements OnUpdateListener,
		ImagesImagesFragment.OnUpdateListener,
		suave.ms.tracker.fragments.ImagesFullscreenFragment.OnUpdateListener {
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

	ImagesCategoryFragment mImagesFragment;
	ImagesImagesFragment mImagesImagesFragment;
	ImagesFullscreenFragment mImagesFullscreenFragment;
	Button btnBack;

	// To identify which data is holding images or labs
	private boolean isFromImages = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template);

		activeActivityContext = this;

		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			LoadingDialog.dialog2 = null;
			ImagesClass.imagesCategoryList.clear();
			isFromImages = extra.getBoolean("isFromImages");

		}

		ImagesClass.isImages = isFromImages;

		mImagesFragment = new ImagesCategoryFragment();
		replaceFragment(mImagesFragment, "");
	}

	/**
	 * @fragment view name
	 * @text used as a flag
	 * 
	 */

	private void replaceFragment(Fragment fragment, String text) {

		String backStateName = fragment.getClass().getName();

		if (text.length() > 0) {
			Bundle args = new Bundle();
			args.putString("msg", text);
			fragment.setArguments(args);
		}

		// fragment not in back stack create it

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		// ft.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_left,
		// R.anim.slide_in_right, R.anim.slide_out_right);

		ft.replace(R.id.fragmentContainer, fragment, backStateName);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onBackPressed() {
		if (mImagesFragment != null && mImagesFragment.isVisible()) {
			Intent intent = new Intent(this, MSTrackerHome.class);
			startActivity(intent);
			finish();
		}
		super.onBackPressed();
	}

	@Override
	public void OnUpdateImagesFragment(int index) {

		mImagesImagesFragment = new ImagesImagesFragment();
		replaceFragment(mImagesImagesFragment, String.valueOf(index));
	}

	@Override
	public void OnUpdateImagesImagesFragment(int parentIndex, int childIndex) {
		mImagesFullscreenFragment = new ImagesFullscreenFragment();
		replaceFragment(mImagesFullscreenFragment, String.valueOf(parentIndex)
				+ "," + String.valueOf(childIndex));
	}

	@Override
	public void OnUpdateImagesFullscreenFragment(int index) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			onBackPressed();
			break;

		default:
			break;
		}
	}

}
