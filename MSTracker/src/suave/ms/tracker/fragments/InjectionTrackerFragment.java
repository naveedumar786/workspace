package suave.ms.tracker.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.helper.InjectionTrackerClass;
import suave.ms.tracker.R;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InjectionTrackerFragment extends Fragment implements
		OnClickListener, OnTaskCompleted {

	private static final String TAG = "InjectionTrackerFragment ";
	OnUpdateListener listener;
	Context context;
	TextView tvTopMenuTitle;
	Button btnReset, btnBack;
	Button btnLeftArm, btnRightArm, btnLeftAbdomen, btnRightAbdomen,
			btnLeftThigh, btnRightThigh, btnLeftBack, btnRightBack;
	String title;
	boolean isReset = false;
	RelativeLayout rlInj_arms, rlInj_abdomen, rlInj_thigh, rlInj_back;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.injection_tracker, container,
				false);

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(getString(R.string.txtInjectionTracker));

		// btnReset = (Button) getActivity().findViewById(R.id.btnReset);
		// btnReset.setOnClickListener(this);

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnReset = (Button) getActivity().findViewById(R.id.btnAdd);
		btnReset.setBackgroundResource(R.drawable.reset_button);
		btnReset.setOnClickListener(this);

		btnLeftArm = (Button) view.findViewById(R.id.btnLeftArm);
		btnLeftArm.setOnClickListener(this);

		btnRightArm = (Button) view.findViewById(R.id.btnRightArm);
		btnRightArm.setOnClickListener(this);

		btnLeftAbdomen = (Button) view.findViewById(R.id.btnLeftAbdomen);
		btnLeftAbdomen.setOnClickListener(this);

		btnRightAbdomen = (Button) view.findViewById(R.id.btnRightAbdomen);
		btnRightAbdomen.setOnClickListener(this);

		btnLeftThigh = (Button) view.findViewById(R.id.btnLeftThigh);
		btnLeftThigh.setOnClickListener(this);

		btnRightThigh = (Button) view.findViewById(R.id.btnRightThigh);
		btnRightThigh.setOnClickListener(this);

		btnLeftBack = (Button) view.findViewById(R.id.btnLeftBack);
		btnLeftBack.setOnClickListener(this);

		btnRightBack = (Button) view.findViewById(R.id.btnRightBack);
		btnRightBack.setOnClickListener(this);

		rlInj_arms = (RelativeLayout) view.findViewById(R.id.rlInj_arms);
		rlInj_abdomen = (RelativeLayout) view.findViewById(R.id.rlInj_abdomen);
		rlInj_thigh = (RelativeLayout) view.findViewById(R.id.rlInj_thigh);
		rlInj_back = (RelativeLayout) view.findViewById(R.id.rlInj_back);

		// call web services...
		serverRequest();

		return view;
	}

	/*** calling web services start **********/
	private void serverRequest() {

		if (HelperMethods.isNetworkAvailable(getActivity())) {

			LoadingDialog.dialog2 = null;
			LoadingDialog.showLoadingDialog(context);

			int userId = suave.ms.tracker.helper.Utils.readPreferences(
					getActivity(), "userId", 0);

			if (isReset) {
				Webservices
						.getData(this, "deleteinjection", "userid=" + userId);
			} else {

				Webservices.getData(this, "getinjection", "userid=" + userId);
			}

		} else {
			HelperMethods.showToast(context,
					this.getResources().getString(R.string.msgNetWorkError));
		}

	}

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {

		try {

			if (json.getBoolean("status")) {

				if (isReset) {
					isReset = false;
					InjectionTrackerClass.clearInjectionsList();
					updateSegmentUI();
					LoadingDialog.cancelLoadingDialog();
					return;
				}

				InjectionTrackerClass.bodyPartList.clear();
				JSONArray arrayOfResults = json.getJSONArray("results");

				for (int i = 0; i < arrayOfResults.length(); i++) {

					JSONObject rObj = arrayOfResults.getJSONObject(i);

					JSONArray arrayOfInjection = rObj.getJSONArray("injection");

					ArrayList<InjectionTrackerClass> injectionList = new ArrayList<InjectionTrackerClass>();

					if (arrayOfInjection.length() > 0) {

						for (int k = 0; k < arrayOfInjection.length(); k++) {

							JSONObject injection = arrayOfInjection
									.getJSONObject(k);

							injectionList.add(new InjectionTrackerClass(
									injection.getInt("id"), injection
											.getString("drug"), injection
											.getString("date_injected")));

						}

					}

					InjectionTrackerClass.bodyPartList
							.add(new InjectionTrackerClass(rObj
									.getInt("body_part_id"), rObj
									.getString("name"), injectionList));

					injectionList.clear();
				}

			} else {
				if (isReset) {
					Log.e(TAG, "onSuccessWebservice is rest");
					isReset = false;
					HelperMethods
							.showToast(context,
									"There is an error on the network please try again later.");
					LoadingDialog.cancelLoadingDialog();
					return;
				}

				Log.e(TAG, "No record Found.");
			}
		} catch (Exception e) {
			Log.e(TAG + "Exception", "" + e);
		}

		// set the buttons
		updateSegmentUI();
		LoadingDialog.cancelLoadingDialog();
	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(TAG, "onFailureWebservice");
		LoadingDialog.cancelLoadingDialog();
		HelperMethods.showToast(context,
				getResources().getString(R.string.msgConnectionError));

	}

	private void updateSegmentUI() {

		rlInj_arms
				.setBackgroundResource(findResourceId(
						InjectionTrackerClass.Left_Arm,
						InjectionTrackerClass.Right_Arm));

		rlInj_abdomen.setBackgroundResource(findResourceId(
				InjectionTrackerClass.Left_Abdomen,
				InjectionTrackerClass.Right_Abdomen));

		rlInj_thigh.setBackgroundResource(findResourceId(
				InjectionTrackerClass.Left_Thigh,
				InjectionTrackerClass.Right_Thigh));

		rlInj_back.setBackgroundResource(findResourceId(
				InjectionTrackerClass.Left_Buutock,
				InjectionTrackerClass.Right_Buutock));
	}

	private int findResourceId(int leftIndex, int rightIndex) {
		int index = 0;

		boolean lIndex = InjectionTrackerClass.bodyPartList.get(leftIndex).injectionList
				.isEmpty();
		boolean rIndex = InjectionTrackerClass.bodyPartList.get(rightIndex).injectionList
				.isEmpty();

		Log.e(TAG,
				"findResourceId"
						+ InjectionTrackerClass.bodyPartList.get(leftIndex).injectionList
								.size()
						+ " - "
						+ InjectionTrackerClass.bodyPartList.get(rightIndex).injectionList
								.size());
		// if both are empty
		if (lIndex && rIndex) {
			index = getResourceId(leftIndex, 0);
		} else if (lIndex && !rIndex) {
			index = getResourceId(leftIndex, 20);
		} else if (!lIndex && rIndex) {
			index = getResourceId(leftIndex, 10);
		} else {
			index = getResourceId(leftIndex, 11);
		}
		Log.e(TAG, "findResourceId: " + index);
		return index;

	}

	private int getResourceId(int res, int resType) {
		Log.e(TAG, "res " + res + " resType " + resType);
		int id = 0;

		if (res == InjectionTrackerClass.Left_Arm
				|| res == InjectionTrackerClass.Right_Arm) {

			switch (resType) {
			case 11:
				id = R.drawable.inj_arm_both;
				break;
			case 10:
				id = R.drawable.inj_arm_left;
				break;
			case 20:
				id = R.drawable.inj_arm_right;
				break;
			case 0:
				id = R.drawable.inj_arms_dim;
				break;

			}

		} else if (res == InjectionTrackerClass.Left_Abdomen
				|| res == InjectionTrackerClass.Right_Abdomen) {
			switch (resType) {
			case 11:
				id = R.drawable.inj_abdomen_both;
				break;
			case 10:
				id = R.drawable.inj_abdomen_left;
				break;
			case 20:
				id = R.drawable.inj_abdomen_right;
				break;
			case 0:
				id = R.drawable.inj_abdomen_dim;
				break;

			}
		} else if (res == InjectionTrackerClass.Left_Thigh
				|| res == InjectionTrackerClass.Right_Thigh) {
			switch (resType) {

			case 11:
				id = R.drawable.inj_thigh_both;
				break;
			case 10:
				id = R.drawable.inj_thigh_left;
				break;
			case 20:
				id = R.drawable.inj_thigh_right;
				break;
			case 0:
				id = R.drawable.inj_thigh_dim;
				break;
			}
		} else if (res == InjectionTrackerClass.Left_Buutock
				|| res == InjectionTrackerClass.Right_Buutock) {
			switch (resType) {
			case 11:
				id = R.drawable.inj_back_both;
				break;
			case 10:
				id = R.drawable.inj_back_left;
				break;
			case 20:
				id = R.drawable.inj_back_right;
				break;
			case 0:
				id = R.drawable.inj_back_dim;
				break;

			}
		}

		return id;
	}

	public interface OnUpdateListener {
		public void OnUpdateInjectionTrackerFragment();

	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
		if (activity instanceof OnUpdateListener) {
			listener = (OnUpdateListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ "must implemenet in main activity");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(View v) {
		int selectedIndex = -1;
		switch (v.getId()) {
		case R.id.btnBack:
			getActivity().onBackPressed();
			break;
		case R.id.btnAdd:
			// btnAdd => btnReset
			isReset = true;
			serverRequest();
			break;

		case R.id.btnLeftArm:
			selectedIndex = InjectionTrackerClass.Left_Arm;
			break;

		case R.id.btnRightArm:
			selectedIndex = InjectionTrackerClass.Right_Arm;
			break;

		case R.id.btnLeftAbdomen:
			selectedIndex = InjectionTrackerClass.Left_Abdomen;
			break;

		case R.id.btnRightAbdomen:
			selectedIndex = InjectionTrackerClass.Right_Abdomen;
			break;

		case R.id.btnLeftThigh:
			selectedIndex = InjectionTrackerClass.Left_Thigh;
			break;

		case R.id.btnRightThigh:
			selectedIndex = InjectionTrackerClass.Right_Thigh;
			break;

		case R.id.btnLeftBack:
			selectedIndex = InjectionTrackerClass.Left_Buutock;
			break;

		case R.id.btnRightBack:
			selectedIndex = InjectionTrackerClass.Right_Buutock;
			break;
		default:
			break;
		}
		// if index changed go to next fragment
		if (selectedIndex != -1) {
			InjectionTrackerClass.selectedIndex = selectedIndex;
			listener.OnUpdateInjectionTrackerFragment();
		}

	}
}
