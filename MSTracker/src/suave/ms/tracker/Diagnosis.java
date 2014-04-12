package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.Treatment.TreatmentListAdapter;
import suave.ms.tracker.helper.DiagnosisClass;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.TreatmentClass;
import suave.ms.tracker.helper.UserDiagnosisClass;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Diagnosis extends MainActivity implements OnTaskCompleted {

	Button btnBack, btnAdd;
	LoadingDialog dialog;
	TextView tvTopMenuTitle;
	ListView lvDiagnosis;
	DiagnosisListAdapter adapter;
	String tag = "Treatment.class ", activityTitle = "Diagnosis";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_view);

		// context to exit activity From global
		super.activeActivityContext = this;

		if (Globals.backButtonId == Globals.AC_DIAGNOSIS) {
			Globals.backButtonId = 0;
		} else {
			// calling web services......
			serverRequest();
		}

		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);

		lvDiagnosis = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new DiagnosisListAdapter(this);
		lvDiagnosis.setAdapter(adapter);

		lvDiagnosis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Loading the Schedule for current category
				Intent intent = new Intent(Diagnosis.this,
						DiagnosisUserDiagnosis.class);

				intent.putExtra("parentIdIndex", position);
				// Load Activity
				startActivity(intent);
				finish();
			}

		});
	}

	/*** calling web services start **********/
	private void serverRequest() {

		DiagnosisClass.diagnosisList = new ArrayList<DiagnosisClass>();

		if (HelperMethods.isNetworkAvailable(this)) {

			showLoadingDialog(getResources().getString(
					R.string.msgLoadingWebservice));
			int userId = Utils.readPreferences(this, "userId", 0);
			Webservices.getData(this, "diagnosis", "userId=" + userId);
		} else {
			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
		}

	}

	/*** calling web services end **********/
	/******************** class TreatmentList Adapter end *******************************/
	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {
			// boolean status = json.getBoolean("status");
			if (json.getBoolean("status")) {

				JSONArray arrayOfResults = json.getJSONArray("results");

				for (int i = 0; i < arrayOfResults.length(); i++) {

					JSONObject rObj = arrayOfResults.getJSONObject(i);

					ArrayList<UserDiagnosisClass> userDiagnosisList = new ArrayList<UserDiagnosisClass>();

					JSONArray arrayOfUserDiagnosis = rObj
							.getJSONArray("userDiagonses");
					for (int k = 0; k < arrayOfUserDiagnosis.length(); k++) {

						Log.e(rObj.getString("diagnosisCateId") + " K = ",
								Integer.toString(k));

						JSONObject userDiagnosisObj = arrayOfUserDiagnosis
								.getJSONObject(k);

						userDiagnosisList.add(new UserDiagnosisClass(
								userDiagnosisObj.getInt("diagnosisId"),
								userDiagnosisObj.getInt("diagnosisCatId"),
								userDiagnosisObj.getInt("userId"),
								userDiagnosisObj.getString("diagnosisDate"),
								userDiagnosisObj.getString("diagnosisPlace"),
								userDiagnosisObj.getString("doctorName"),
								userDiagnosisObj.getString("labImageDone")
										.equalsIgnoreCase("1") ? true : false,
								userDiagnosisObj.getString("status")
										.equalsIgnoreCase("1") ? true : false));
					}

					DiagnosisClass.diagnosisList.add(new DiagnosisClass(rObj
							.getInt("diagnosisCateId"), rObj
							.getString("catName"), userDiagnosisList));
					userDiagnosisList.clear();
				}

				adapter.notifyDataSetChanged();

				cancelLoadingDialog();
			}
		} catch (Exception e) {
			Log.e(tag + "Exception", "" + e);
		}

		cancelLoadingDialog();

	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(tag, "onFailureWebservice");
		HelperMethods.showToast(this,
				getResources().getString(R.string.msgConnectionError));

		cancelLoadingDialog();

	}

	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class DiagnosisListAdapter extends BaseAdapter {

		Context context;

		public DiagnosisListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return DiagnosisClass.diagnosisList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return DiagnosisClass.diagnosisList.get(index);
		}

		@Override
		public long getItemId(int index) {
			// return the id of the row which is our array index.
			return index;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.schedule_category_row, parent,
					false);

			TextView title = (TextView) row
					.findViewById(R.id.tvScheduleCateTitle);
			DiagnosisClass diagnos = DiagnosisClass.diagnosisList.get(index);
			Log.e(tag, diagnos.getCatName());

			title.setText(diagnos.getCatName());
			return row;
		}

	}

	public void showLoadingDialog(String msg) {
		if (dialog != null) {
			dialog.setMessage(msg);
			dialog.show();
		} else {
			dialog = new LoadingDialog(this);
			dialog.setMessage(msg);
			dialog.show();
		}
	}

	public void cancelLoadingDialog() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onClick(btnBack);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			Intent intent = new Intent(Diagnosis.this, MSTrackerHome.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}
}
