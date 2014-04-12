package suave.ms.tracker;

import suave.ms.tracker.fragments.MSProfileFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

public class MSProfile extends MainActivity implements
		MSProfileFragment.OnUpdateListener {
	MSProfileFragment mMSProfileFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.template);

		activeActivityContext = this;

		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		mMSProfileFragment = new MSProfileFragment();
		replaceFragment(mMSProfileFragment, "");
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
		if (mMSProfileFragment != null && mMSProfileFragment.isVisible()) {
			Intent intent = new Intent(this, MSTrackerHome.class);
			startActivity(intent);
			finish();
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnBack:
			onBackPressed();
			break;

		default:
			break;
		}

	}

	@Override
	public void OnUpdateFragment() {
		// TODO Auto-generated method stub

	}

}
