package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.helper.AppointmentsClass;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleAppointment extends MainActivity implements
		OnTaskCompleted {
	ListView lvAppointment;
	ArrayList<AppointmentsClass> appointmentList;
	Button btnBack, btnAdd;
	TextView topMenuTitle;
	private int parentId;
	private String parentTitle;
	AppointmentsAdapter adapter;
	private String tag = "ScheduleAppointment ";
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_appointment_list);

		// context to exit activity From global
		super.activeActivityContext = this;

		Log.e(tag, "OnCreate");
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			parentId = extra.getInt("id");
			parentTitle = extra.getString("title");

			topMenuTitle = (TextView) findViewById(R.id.topMenuTitle);
			topMenuTitle.setText(parentTitle);

		}

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.VISIBLE);
		btnAdd.setOnClickListener(this);

		// calling webservice
		appointmentList = new ArrayList<AppointmentsClass>();
		loadDataFromServer("userSchedule", null);

		lvAppointment = (ListView) findViewById(R.id.lvAppointments);
		adapter = new AppointmentsAdapter(this);
		lvAppointment.setAdapter(adapter);
		lvAppointment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(getApplicationContext(),
						appointmentList.get(position).getDoctorName(),
						Toast.LENGTH_SHORT).show();

				// Loading the Schedule for current category

				// Intent intent = new Intent(ScheduleAppointment.this,
				// ScheduleAppointmentUpdate.class);
				//
				// intent.putExtra("id", appointmentList.get(position)
				// .getScheduleId());
				// // Load Activity
				// startActivity(intent);
				// finish();
			}

		});

	}

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

	/******************** class AppointmentsAdapter start *******************************/
	// Schedule category List Adapter
	class AppointmentsAdapter extends BaseAdapter {

		Context context;
		Button btnUpdate;
		CheckBox cbStatus;

		public AppointmentsAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return appointmentList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return appointmentList.get(index);
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

			View row = inflater.inflate(R.layout.simple_list_view_row, parent,
					false);

			TextView drName = (TextView) row.findViewById(R.id.tvDrName);
			TextView appointmentDate = (TextView) row
					.findViewById(R.id.tvAppointmentDate);

			AppointmentsClass appointment = appointmentList.get(index);

			drName.setText(appointment.getDoctorName());
			appointmentDate.setText(appointment.getVisitTiming());

			/*** Register update Button start *****/
			btnUpdate = (Button) row.findViewById(R.id.btnUpdate);
			btnUpdate.setTag(index);
			btnUpdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					int index = (Integer) view.getTag();
					updateAppointment(index);
				}
			});
			/*** Register update Button end *****/

			cbStatus = (CheckBox) row.findViewById(R.id.cbStatus);
			cbStatus.setTag(index);
			cbStatus.setVisibility(View.VISIBLE);
			cbStatus.setChecked(appointment.getStatus());
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

		/*** update Button handler *****/
		void updateAppointment(int index) {
			AppointmentsClass apointment = appointmentList.get(index);
			Intent intent = new Intent(ScheduleAppointment.this,
					ScheduleAppointmentAdd.class);
			intent.putExtra("suave.ms.tracker.helper.AppointmentsClass",
					apointment);

			intent.putExtra("schedualId", apointment.getScheduleId());
			intent.putExtra("title", parentTitle);
			intent.putExtra("id", parentId);
			intent.putExtra("task", "Update");

			// Load Activity
			startActivity(intent);
			finish();
		}

		/*** status checkbox handler *****/
		void updateStatus(boolean isChecked, int index) {

			AppointmentsClass apointment = appointmentList.get(index);
			apointment.setStatus(isChecked);
			loadDataFromServer("updateUserScheduleStatus", apointment);

			// if (isChecked) {
			// HelperMethods.showToast(getApplicationContext(), "Checked");
			// } else {
			// HelperMethods.showToast(getApplicationContext(), "Unchecked");
			// }

		}
	}

	/*** calling web services start **********/
	private void loadDataFromServer(String ServiceName,
			AppointmentsClass appointment) {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			if (ServiceName.equalsIgnoreCase("updateUserScheduleStatus")) {
				adapter.notifyDataSetChanged();
			}
			return;
		}

		if (ServiceName.equalsIgnoreCase("userSchedule")) {

			int userId = Utils.readPreferences(this, "userId", -1);

			String parmsString = "userId=" + userId + "&scheduleCatId="
					+ parentId;

			showLoadingDialog("Loading...");
			Webservices.getData(this, "userSchedule", parmsString);

		} else if (ServiceName.equalsIgnoreCase("updateUserScheduleStatus")) {
			// Update user schedule status
			int status = appointment.getStatus() ? 1 : 0;
			String parmsString = "scheduleId=" + appointment.getScheduleId()
					+ "&status=" + status;

			showLoadingDialog(getResources().getString(R.string.msgUpdating));
			Webservices.getData(this, "updateUserScheduleStatus", parmsString);

		}

	}

	/*** calling web services end **********/
	/******************** class AppointmentsAdapter end *******************************/

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {
			boolean status = (Boolean) json.get("status");
			if (status) {
				if (action.equalsIgnoreCase("userSchedule")) {

					JSONArray arrayOfResults = json.getJSONArray("results");

					for (int i = 0; i < arrayOfResults.length(); i++) {
						JSONObject resultObj = arrayOfResults.getJSONObject(i);

						String visitDate = HelperMethods.getFormatedDate(
								resultObj.getString("visitTiming"),
								"yyyy-MM-dd hh:mm:ss", "MM/dd/yy hh:mm aa");

						appointmentList.add(new AppointmentsClass(resultObj
								.getInt("scheduleId"), resultObj
								.getInt("scheduleCatId"), 1,
								resultObj.getString("status").equalsIgnoreCase(
										"1") ? true : false, resultObj
										.getString("doctorName"), visitDate,
								resultObj.getString("contactInfo"), resultObj
										.getString("staffName")));
					}
					adapter.notifyDataSetChanged();
				} else if (action.equalsIgnoreCase("updateUserScheduleStatus")) {
					adapter.notifyDataSetChanged();
				}

			} else {
				HelperMethods.showToast(this, "No record found");
			}
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(ScheduleAppointment.this, Schedule.class);
		// Load Activity
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnBack:
			intent = new Intent(ScheduleAppointment.this, Schedule.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnAdd:
			// HelperMethods.showToast(getApplicationContext(),
			// "Under Development");
			intent = new Intent(ScheduleAppointment.this,
					ScheduleAppointmentAdd.class);
			intent.putExtra("title", parentTitle);
			intent.putExtra("id", parentId);
			intent.putExtra("task", "Add");
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
