package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class TodayMedication extends MainActivity implements OnTaskCompleted {
	// ArrayList<ScheduleCategoryClass> treatmentList;
	Button btnBack, btnAdd;
	TextView tvTopMenuTitle;
	ListView lvMedication;
	MedicationListAdapter adapter;
	String tag = "TodayMedication.class";
	String activityTitle = "Today's Medication";
	LoadingDialog dialog;

	public ArrayList<MedicationClass> medicationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		// context to exit activity From global
		super.activeActivityContext = this;

		findViewById(R.id.btnTodayMedication).setBackgroundResource(
				R.drawable.today_medication_t);
		((TextView) findViewById(R.id.btnTodayMedicationText))
				.setTextColor(Color.parseColor(Globals.tabedTextColor));

		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);

		medicationList = new ArrayList<MedicationClass>();

		// calling web services......
		serverRequest();

		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		Log.e(tag, "OnCreate");

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		lvMedication = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new MedicationListAdapter(this);
		lvMedication.setAdapter(adapter);

		lvMedication.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});

		adapter.notifyDataSetChanged();
	}

	/*** calling web services start **********/
	private void serverRequest() {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String parmsString = "userId="
				+ Utils.readPreferences(this, "userId", 0);

		showLoadingDialog("Loading...");
		Webservices.getData(this, "today_mdication", parmsString);

	}

	/*** calling web services end **********/
	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class MedicationListAdapter extends BaseAdapter {

		Context context;
		CheckBox cbStatus;

		public MedicationListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return medicationList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return medicationList.get(index);
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

			View row = inflater.inflate(R.layout.simple_list_view_row,
					parent, false);

			TextView medicationName = (TextView) row
					.findViewById(R.id.tvDrName);
			TextView dosage = (TextView) row
					.findViewById(R.id.tvAppointmentDate);

			MedicationClass medication = medicationList.get(index);

			medicationName.setText(medication.getMedicationName());
			dosage.setText(medication.getDosage());

			/*** Hide update Button start *****/
			((Button) row.findViewById(R.id.btnUpdate))
					.setVisibility(View.GONE);
			/*** Hide update Button end *****/

			cbStatus = (CheckBox) row.findViewById(R.id.cbStatus);
			cbStatus.setTag(index);
			cbStatus.setChecked(medication.getStatus());
			cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					int index = (Integer) buttonView.getTag();
					updateStatus(isChecked, index);
				}

			});
			/*** Register update Button end *****/
			return row;
		}
	}

	/*** status checkbox handler *****/
	void updateStatus(boolean isChecked, int index) {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			adapter.notifyDataSetChanged();
			return;
		}

		MedicationClass medication = medicationList.get(index);
		medication.setStatus(isChecked);

		String parmsString = "userId="
				+ Utils.readPreferences(this, "userId", 0);
		String status = (isChecked) ? "1" : "0";
		showLoadingDialog("Loading...");
		Webservices.getData(this, "updateUserMedicationStatus", "medicationId="
				+ medication.getMedicationId() + "&status=" + status);

		// if (isChecked) {
		// HelperMethods.showToast(getApplicationContext(), "Checked");
		// } else {
		// HelperMethods.showToast(getApplicationContext(), "Unchecked");
		// }

	}

	/******************** class AppointmentsAdapter end *******************************/

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {
			if (action.equalsIgnoreCase("today_mdication")) {
				boolean status = (Boolean) json.get("status");
				if (status) {
					JSONArray arrayOfResults = json.getJSONArray("results");

					for (int i = 0; i < arrayOfResults.length(); i++) {
						JSONObject medicationObj = arrayOfResults
								.getJSONObject(i);
						MedicationClass medication = new MedicationClass(
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
								medicationObj.getInt("treatmentId"));

						boolean medicationStatus = (medicationObj
								.getString("status").equalsIgnoreCase("1")) ? true
								: false;
						medication.setStatus(medicationStatus);
						medicationList.add(medication);
					}

				} else {
					HelperMethods.showToast(getApplicationContext(),
							"No Record Found");
				}
			} else if (action.equalsIgnoreCase("updateUserMedicationStatus")) {

			}

			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("Exception", "" + e);
		}

		cancelLoadingDialog();

	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(tag, "onFailureWebservice");
		HelperMethods.showToast(this,
				getResources().getString(R.string.msgConnectionError));
		adapter.notifyDataSetChanged();
		cancelLoadingDialog();

	}

	/******************** class medication List Adapter end *******************************/
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
			finish();
			break;

		default:
			break;
		}

	}
}
