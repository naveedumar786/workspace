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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InjectionAddUpdateFragment extends Fragment implements
		OnClickListener, OnTaskCompleted {

	private static final String TAG = "InjectionAddUpdateFragment ";
	OnUpdateListener listener;
	Context context;
	TextView tvTopMenuTitle;
	Button btnSave, btnBack;
	EditText etName, etDateTime;
	String webservice;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.injection_add_update, container,
				false);

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(getString(R.string.txtInjectionTracker));

		btnSave = (Button) getActivity().findViewById(R.id.btnAdd);
		btnSave.setBackgroundResource(R.drawable.save_button);
		btnSave.setOnClickListener(this);

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		etName = (EditText) view.findViewById(R.id.etName);
		etDateTime = (EditText) view.findViewById(R.id.etDateDiagnose);

		if (InjectionTrackerClass.selectedChildIndex == -1) {// for inserting
																// new record
			webservice = "injection";
		} else {
			webservice = "updateinjection";

			InjectionTrackerClass injection = InjectionTrackerClass.bodyPartList
					.get(InjectionTrackerClass.selectedIndex).injectionList
					.get(InjectionTrackerClass.selectedChildIndex);

			etName.setText(injection.getInjectedDrug());
			etDateTime.setText(injection.getInjectedDate());

		}

		return view;
	}

	/*** calling web services start **********/
	private void serverRequest() {

		if (HelperMethods.isNetworkAvailable(getActivity())) {
			LoadingDialog.dialog2 = null;
			LoadingDialog.showLoadingDialog(context);

			int userId = suave.ms.tracker.helper.Utils.readPreferences(
					getActivity(), "userId", 0);

			int body_partId = InjectionTrackerClass.bodyPartList.get(
					InjectionTrackerClass.selectedIndex).getBodyPartId();
			String injectionId = "";

			if (InjectionTrackerClass.selectedChildIndex > 0) {

				injectionId = "&id="
						+ InjectionTrackerClass.bodyPartList
								.get(InjectionTrackerClass.selectedIndex).injectionList
								.get(InjectionTrackerClass.selectedChildIndex)
								.getInjectionId();
			}

			Webservices.getData(this, webservice,
					"userid=" + userId + "&body_partId=" + body_partId
							+ "&drug=" + etName.getText() + "&date_injected="
							+ etDateTime.getText() + "" + injectionId);

		} else {
			HelperMethods.showToast(context,
					this.getResources().getString(R.string.msgNetWorkError));
		}

	}

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {

		try {

			if (json.getBoolean("status")) {

				InjectionTrackerClass newInjection = new InjectionTrackerClass(
						0, etName.getText().toString(), etDateTime.getText()
								.toString());

				if (InjectionTrackerClass.selectedChildIndex > 0) {
					InjectionTrackerClass.bodyPartList
							.get(InjectionTrackerClass.selectedIndex).injectionList
							.get(InjectionTrackerClass.selectedChildIndex)
							.setInjectedDrug(newInjection.getInjectedDrug());
					InjectionTrackerClass.bodyPartList
							.get(InjectionTrackerClass.selectedIndex).injectionList
							.get(InjectionTrackerClass.selectedChildIndex)
							.setInjectedDate(newInjection.getInjectedDate());
				} else {
					int insertedId = json.getInt("insertedId");
					newInjection.setInjectionId(insertedId);
					InjectionTrackerClass.bodyPartList
							.get(InjectionTrackerClass.selectedIndex).injectionList
							.add(newInjection);
				}
				LoadingDialog.cancelLoadingDialog();
				onClick(btnBack);

			} else {
				Log.e(TAG, "No record Found.");
			}
		} catch (Exception e) {
			Log.e(TAG + "Exception", "" + e);
		}

		LoadingDialog.cancelLoadingDialog();
	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(TAG, "onFailureWebservice");
		LoadingDialog.cancelLoadingDialog();
		HelperMethods.showToast(context,
				getResources().getString(R.string.msgConnectionError));

	}

	public interface OnUpdateListener {
		public void OnUpdateInjectionAddUpdateFragment();

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
		switch (v.getId()) {
		case R.id.btnBack:
			HelperMethods.hiddenKeyboard(getActivity());
			getActivity().onBackPressed();
			break;
		case R.id.btnAdd:
			// btnAdd => btnSave
			serverRequest();
			break;

		default:
			break;
		}

	}
}
