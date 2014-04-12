package suave.ms.tracker;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import suave.ms.tracker.helper.DiagnosisClass;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.UserDiagnosisClass;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class DiagnosisUserDiagnoseAdd extends MainActivity implements
		OnDateSetListener, OnTaskCompleted {

	Button btnBack, btnSave;
	EditText etDrName, etDateDiagnose, etPlaceDiagnose;
	CheckBox cbImaging;
	Calendar myCalendar;
	private int parentIndexId, userDiagnoseIndex;
	DatePickerDialog.OnDateSetListener date;
	static final int TIME_DIALOG_ID = 999;
	private int hour, minute;
	TextView tvTopMenuTitle;
	LoadingDialog dialog;
	String task, tag = "DiagnosisUserDiagnoseAdd";
	UserDiagnosisClass updateableUserDiagnosis, updatedUserDiagnosis;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagnosis_add);


		// context to exit activity From global
		super.activeActivityContext = this;

		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		tvTopMenuTitle = (TextView) findViewById(R.id.topMenuTitle);
		tvTopMenuTitle.setText("Add New Diagnose");

		etDrName = (EditText) findViewById(R.id.etDrName);
		etDateDiagnose = (EditText) findViewById(R.id.etDateDiagnose);
		etPlaceDiagnose = (EditText) findViewById(R.id.etPlaceDiagnose);
		cbImaging = (CheckBox) findViewById(R.id.cbImaging);

		myCalendar = Calendar.getInstance();

		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		if (extra != null) {
			parentIndexId = extra.getInt("parentIndexId");

			task = extra.getString("task");
			if (task.equalsIgnoreCase("Update")) {

				tvTopMenuTitle.setText("Update Diagnose");

				userDiagnoseIndex = extra.getInt("userDiagnoseIndex");

				updateableUserDiagnosis = DiagnosisClass.diagnosisList
						.get(parentIndexId).userDignosisList
						.get(userDiagnoseIndex);

				etDrName.setText(updateableUserDiagnosis.getDoctorName());

				etDateDiagnose.setText(updateableUserDiagnosis
						.getDiagnosisDate());

				etPlaceDiagnose.setText(updateableUserDiagnosis
						.getDiagnosisPlace());
				cbImaging.setChecked(updateableUserDiagnosis.isLabImageDone());

			}

		}

		etDateDiagnose.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DatePickerDialog dialog = new DatePickerDialog(
							DiagnosisUserDiagnoseAdd.this,
							DiagnosisUserDiagnoseAdd.this, myCalendar
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
		// showDialog(TIME_DIALOG_ID);

	}

	private void updateLabel() {

		String myFormat = "MM/dd/yyyy"; // In which you need put here
		// String myFormat = "MM/dd/yy hh:mm aa"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		etDateDiagnose.setText(sdf.format(myCalendar.getTime()));
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
	private void executeService() {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String ServiceName, parmsString;

		if (task.equalsIgnoreCase("Update")) {
			ServiceName = "updateUserDiagnosis";
			updatedUserDiagnosis = new UserDiagnosisClass(
					updateableUserDiagnosis.getDiagnosisId(),
					updateableUserDiagnosis.getDiagnosisCatId(),
					updateableUserDiagnosis.getUserId(), etDateDiagnose
							.getText().toString(), etPlaceDiagnose.getText()
							.toString(), etDrName.getText().toString(),
					cbImaging.isChecked(), updateableUserDiagnosis.isStatus());

			String labImageDone = updatedUserDiagnosis.isLabImageDone() ? "1"
					: "0";
			parmsString = "diagnosisId="
					+ updatedUserDiagnosis.getDiagnosisId()
					+ "&diagnosisDate="
					+ URLEncoder
							.encode(updatedUserDiagnosis.getDiagnosisDate())
					+ "&diagnosisPlace="
					+ URLEncoder.encode(updatedUserDiagnosis
							.getDiagnosisPlace()) + "&labImageDone="
					+ labImageDone + "&doctorName="
					+ URLEncoder.encode(updatedUserDiagnosis.getDoctorName());
		} else {

			ServiceName = "addUserDiagnosis";
			updatedUserDiagnosis = new UserDiagnosisClass(0,
					DiagnosisClass.diagnosisList.get(parentIndexId)
							.getDiagnosisCateId(), Utils.readPreferences(this,
							"userId", -1), etDateDiagnose.getText().toString(),
					etPlaceDiagnose.getText().toString(), etDrName.getText()
							.toString(), cbImaging.isChecked(), true);

			String labImageDone = updatedUserDiagnosis.isLabImageDone() ? "1"
					: "0";
			parmsString = "diagnosisCatId="
					+ updatedUserDiagnosis.getDiagnosisCatId()
					+ "&userId="
					+ updatedUserDiagnosis.getUserId()
					+ "&dateDiagonsed="
					+ URLEncoder
							.encode(updatedUserDiagnosis.getDiagnosisDate())
					+ "&placeDiagonsed="
					+ URLEncoder.encode(updatedUserDiagnosis
							.getDiagnosisPlace()) + "&labImageDone="
					+ labImageDone + "&drName="
					+ URLEncoder.encode(updatedUserDiagnosis.getDoctorName());
		}

		showLoadingDialog(getResources().getString(R.string.msgUpdating));

		Webservices.getData(this, ServiceName, parmsString);
	}

	/*** calling web services end **********/
	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		cancelLoadingDialog();
		try {

			boolean status = (Boolean) json.get("status");

			if (action.equalsIgnoreCase("addUserDiagnosis")) {
				if (status) {
					int insertId = (Integer) json.get("insertId");

					updatedUserDiagnosis.setDiagnosisId(insertId);

					DiagnosisClass.diagnosisList.get(parentIndexId)
							.getUserDignosisList().add(updatedUserDiagnosis);

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgDiagnoseInsert));
				} else {
					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgError));
				}
			} else if (action.equalsIgnoreCase("updateUserDiagnosis")) {
				if (status) {
					DiagnosisClass.diagnosisList.get(parentIndexId)
							.getUserDignosisList()
							.set(userDiagnoseIndex, updatedUserDiagnosis);

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgDiagnoseUpdate));

				} else {
					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgError));
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
		onClick(btnBack);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnBack:
			intent = new Intent(DiagnosisUserDiagnoseAdd.this,
					DiagnosisUserDiagnosis.class);

			intent.putExtra("parentIdIndex", this.parentIndexId);
			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnSave:

			Utils.hideSoftkeybord(DiagnosisUserDiagnoseAdd.this);
			if (etDrName.getText().toString().isEmpty()
					|| etDateDiagnose.getText().toString().isEmpty()
					|| etPlaceDiagnose.getText().toString().isEmpty()) {
				HelperMethods
						.showToast(this, getString(R.string.msgEmptyField));
				return;
			}
			// arrList.set(5,newValue);
			executeService();

			break;

		default:
			break;
		}

	}
}
