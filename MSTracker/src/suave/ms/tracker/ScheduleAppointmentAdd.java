package suave.ms.tracker;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;
import org.w3c.dom.Text;

import suave.ms.tracker.helper.AppointmentsClass;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ScheduleAppointmentAdd extends MainActivity implements
		OnDateSetListener, OnTaskCompleted {
	Button btnBack, btnSave;
	EditText etDrName, etDateTime, etReminder, etStaffName, etContectInfo;
	Calendar myCalendar;
	private int parentId, scheduleId;
	private String parentTitle;
	DatePickerDialog.OnDateSetListener date;
	static final int TIME_DIALOG_ID = 999;
	private int hour, minute;
	LoadingDialog dialog;
	String task, tag = "ScheduleAppointmentAdd";
	AppointmentsClass updateAbleAppointment;
	TextView tvTopMenuTitle;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_appointment_add);

		// context to exit activity From global
		super.activeActivityContext = this;

		tvTopMenuTitle = (TextView) findViewById(R.id.topMenuTitle);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		etDrName = (EditText) findViewById(R.id.etDrName);
		etStaffName = (EditText) findViewById(R.id.etStaffName);
		etContectInfo = (EditText) findViewById(R.id.etContectInfo);
		etDateTime = (EditText) findViewById(R.id.etDateTime);

		myCalendar = Calendar.getInstance();

		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		if (extra != null) {
			parentId = extra.getInt("id");
			parentTitle = extra.getString("title");
			task = extra.getString("task");
			if (task.equalsIgnoreCase("Update")) {
				tvTopMenuTitle.setText("Update Appointment");
				updateAbleAppointment = (AppointmentsClass) extra
						.getParcelable("suave.ms.tracker.helper.AppointmentsClass");
				scheduleId = extra.getInt("schedualId");
				etDrName.setText(updateAbleAppointment.getDoctorName());
				etStaffName.setText(updateAbleAppointment.getStaffName());
				etContectInfo.setText(updateAbleAppointment.getContactInfo());
				etDateTime.setText(updateAbleAppointment.getVisitTiming());

			}

		}
		etDateTime.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DatePickerDialog dialog = new DatePickerDialog(
							ScheduleAppointmentAdd.this,
							ScheduleAppointmentAdd.this, myCalendar
									.get(Calendar.YEAR), myCalendar
									.get(Calendar.MONTH), myCalendar
									.get(Calendar.DAY_OF_MONTH));
					dialog.show();
				}
			}
		});

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		myCalendar.set(Calendar.YEAR, year);
		myCalendar.set(Calendar.MONTH, monthOfYear);
		myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		updateLabel();
		showDialog(TIME_DIALOG_ID);

	}

	private void updateLabel() {

		String myFormat = "MM/dd/yy hh:mm aa"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		etDateTime.setText(sdf.format(myCalendar.getTime()));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			hour = myCalendar.get(Calendar.HOUR);
			minute = myCalendar.get(Calendar.MINUTE);

			return (new TimePickerDialog(this, timePickerListener, hour,
					minute, true));

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			myCalendar.set(Calendar.HOUR, hour);
			myCalendar.set(Calendar.MINUTE, minute);
			updateLabel();

		}

	};

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

	/*** calling web services start **********/
	private void executeService(String ServiceName,
			AppointmentsClass appointment) {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String parmsString = "scheduleCatId=" + parentId + "&userId="
				+ Utils.readPreferences(this, "userId", -1) + "&doctorName="
				+ appointment.getDoctorName() + "&visitTiming="
				+ appointment.getVisitTiming() + "&reminderTime="
				+ appointment.getVisitTiming() + "&contactInfo="
				+ appointment.getContactInfo() + "&staffName="
				+ appointment.getStaffName();

		showLoadingDialog(getResources().getString(R.string.msgUpdating));

		if (ServiceName.equalsIgnoreCase("updateUserSchedule")) {
			parmsString += "&scheduleId=" + this.scheduleId;
		}

		Webservices.getData(this, ServiceName, parmsString);
	}

	/*** calling web services end **********/
	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		cancelLoadingDialog();
		try {
			boolean status = (Boolean) json.get("status");
			if (status) {
				if (action.equalsIgnoreCase("addUserSchedule")) {

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgApointmentInsert));

				} else if (action.equalsIgnoreCase("updateUserSchedule")) {
					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgApointmentUpdate));

				}

			}

		} catch (Exception e) {
			Log.e("Exception", "" + e);
		}

		onClick(btnBack);
	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(tag, "onFailureWebservice");
		HelperMethods.showToast(this,
				getResources().getString(R.string.msgConnectionError));

		cancelLoadingDialog();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		Intent intent = new Intent(ScheduleAppointmentAdd.this,
				ScheduleAppointment.class);
		intent.putExtra("title", this.parentTitle);
		intent.putExtra("id", this.parentId);
		// Load Activity
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnBack:

			intent = new Intent(ScheduleAppointmentAdd.this,
					ScheduleAppointment.class);
			intent.putExtra("title", this.parentTitle);
			intent.putExtra("id", this.parentId);
			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnSave:
			Utils.hideSoftkeybord(ScheduleAppointmentAdd.this);
			if (etDrName.getText().toString().isEmpty()
					|| etDateTime.getText().toString().isEmpty()) {
				HelperMethods
						.showToast(this, getString(R.string.msgEmptyField));
				return;
			}

			AppointmentsClass appointment = new AppointmentsClass(0, parentId,
					Utils.readPreferences(this, "userId", -1), true,
					URLEncoder.encode(etDrName.getText().toString()),
					URLEncoder.encode(etDateTime.getText().toString()),
					URLEncoder.encode(etContectInfo.getText().toString()),
					URLEncoder.encode(etStaffName.getText().toString()));

			if (task.equalsIgnoreCase("Update")) {
				executeService("updateUserSchedule", appointment);
			} else {
				executeService("addUserSchedule", appointment);
			}
			break;

		default:
			break;
		}

	}
}
