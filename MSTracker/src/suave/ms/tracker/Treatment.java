package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.TreatmentClass;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Treatment extends MainActivity implements OnTaskCompleted {

	Button btnBack;
	TextView tvTopMenuTitle;
	ListView lvTreatment;
	TreatmentListAdapter adapter;
	String tag = "Treatment.class ", activityTitle = "Treatment";
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		// context to exit activity From global
		super.activeActivityContext = this;

		if (Globals.backButtonId == Globals.AC_TREATMENT_MEDICATION) {
			Globals.backButtonId = 0;
		} else {
			// calling web services......
			serverRequest();
		}
		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);
		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		lvTreatment = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new TreatmentListAdapter(this);
		lvTreatment.setAdapter(adapter);

		lvTreatment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Loading the Schedule for current category
				Intent intent = new Intent(Treatment.this,
						TreatmentMedication.class);

				intent.putExtra("title",
						TreatmentClass.treatmetnList.get(position)
								.getTreatmentName());
				intent.putExtra("parentIdIndex", position);
				// Load Activity
				startActivity(intent);
				finish();
			}

		});
	}

	/*** calling web services start **********/
	private void serverRequest() {

		TreatmentClass.treatmetnList = new ArrayList<TreatmentClass>();

		if (HelperMethods.isNetworkAvailable(this)) {
			showLoadingDialog(getString(R.string.msgLoadingWebservice));
			int userId = Utils.readPreferences(this, "userId", 0);
			Webservices.getData(this, "treatment_with_medication", "userId="
					+ userId);
		} else {
			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
		}

	}

	/*** calling web services end **********/
	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class TreatmentListAdapter extends BaseAdapter {

		Context context;

		public TreatmentListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return TreatmentClass.treatmetnList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return TreatmentClass.treatmetnList.get(index);
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
			TreatmentClass treatment = TreatmentClass.treatmetnList.get(index);
			Log.e(tag, treatment.getTreatmentName());

			title.setText(treatment.getTreatmentName());
			return row;
		}

	}

	/******************** class TreatmentList Adapter end *******************************/

	public void onSuccessWebservice(JSONObject json, String action) {

		try {
			// boolean status = json.getBoolean("status");
			if (json.getBoolean("status")) {

				JSONArray arrayOfResults = json.getJSONArray("results");

				for (int i = 0; i < arrayOfResults.length(); i++) {

					JSONObject rObj = arrayOfResults.getJSONObject(i);

					ArrayList<MedicationClass> medicationList = new ArrayList<MedicationClass>();

					if (rObj.getInt("mediacations_count") > 0) {
						JSONArray arrayOfMedication = rObj
								.getJSONArray("mediacations");
						for (int k = 0; k < arrayOfMedication.length(); k++) {

							Log.e(rObj.getString("treatmentId") + " K = ",
									Integer.toString(k));

							JSONObject medicationObj = arrayOfMedication
									.getJSONObject(k);

							medicationList.add(new MedicationClass(
									medicationObj.getInt("medicationId"),
									medicationObj.getInt("userId"),
									medicationObj.getString("medicationName"),
									medicationObj.getString("dosage"),
									medicationObj.getString("indication"),
									medicationObj.getString("reminder"),
									medicationObj.getString("start_date"),
									medicationObj.getString("end_date"),
									medicationObj.getString("treatment_type"),
									medicationObj.getString("more_info"),
									medicationObj.getInt("treatmentId")));
						}

					}

					TreatmentClass.treatmetnList.add(new TreatmentClass(rObj
							.getInt("treatmentId"), rObj
							.getString("treatmentName"), medicationList));
					medicationList.clear();
				}

				adapter.notifyDataSetChanged();

				cancelLoadingDialog();
			}
		} catch (Exception e) {
			Log.e(tag + "Exception", "" + e);
		}

		cancelLoadingDialog();
	}

	// Async service call Success response
	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(tag, "onFailureWebservice");
		HelperMethods.showToast(this,
				getResources().getString(R.string.msgConnectionError));

		cancelLoadingDialog();
	}

	/*** calling web services end **********/
	private void showLoadingDialog(String msg) {
		if (dialog != null) {
			dialog.setMessage(msg);
			dialog.show();
		} else {
			dialog = new LoadingDialog(this);
			dialog.setMessage(msg);
			dialog.show();
		}
	}

	private void cancelLoadingDialog() {
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
			Intent intent = new Intent(Treatment.this, MSTrackerHome.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
