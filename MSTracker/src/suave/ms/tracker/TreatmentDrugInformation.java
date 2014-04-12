package suave.ms.tracker;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import suave.ms.tracker.TreatmentMedicationOption.MedicationOptionListAdapter;
import suave.ms.tracker.helper.DiagnosisClass;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.TreatmentClass;
import suave.ms.tracker.helper.UserDiagnosisClass;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TreatmentDrugInformation extends MainActivity implements
		OnTaskCompleted {
	ArrayList<String> medicationOptionList;
	String tag = "DrugInformation";
	String activityTitle = "Drug Information";
	String treatmentType;
	LoadingDialog dialog;
	int medicationId, medicationParentId;
	MedicationClass medicationObj, newMedication;
	ListView lvMedicationOptions;
	Button btnBack, btnSave;
	EditText etDrugName, etDosage, etIndication, etReminder, etStartDate,
			etEndDate, etMoreInfo;
	TextView tvTopMenuTitle;
	MedicationOptionListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drug_information);


		// context to exit activity From global
		super.activeActivityContext = this;

		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		Utils.hideSoftkeybord(getApplicationContext());

		TextView tvEndDate = (TextView) findViewById(R.id.tvEndDate);
		tvTopMenuTitle = (TextView) findViewById(R.id.topMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		Log.e(tag, "OnCreate");

		etDrugName = (EditText) findViewById(R.id.etDrugName);

		etDosage = (EditText) findViewById(R.id.etDosage);

		etIndication = (EditText) findViewById(R.id.etIndication);

		etReminder = (EditText) findViewById(R.id.etReminder);

		etStartDate = (EditText) findViewById(R.id.etStartDate);

		etEndDate = (EditText) findViewById(R.id.etEndDate);

		etMoreInfo = (EditText) findViewById(R.id.etMoreInfo);

		Bundle extra = getIntent().getExtras();

		if (extra != null) {

			medicationId = extra.getInt("medicationIdIndex");
			medicationParentId = extra.getInt("medicationParentId");
			treatmentType = extra.getString("treatmentType");

			if (treatmentType.equalsIgnoreCase("Previous")) {
				etEndDate.setVisibility(View.VISIBLE);
				tvEndDate.setVisibility(View.VISIBLE);
			}

			// fill fields if previous or current
			if (treatmentType.equalsIgnoreCase("Previous")
					|| treatmentType.equalsIgnoreCase("Current")) {
				// get selected medication
				medicationObj = TreatmentClass.treatmetnList
						.get(medicationParentId).medicationList
						.get(medicationId);

				etDrugName.setText(medicationObj.getMedicationName());
				etDosage.setText(medicationObj.getDosage());
				etIndication.setText(medicationObj.getIndication());
				etReminder.setText(medicationObj.getReminder());
				etStartDate.setText(medicationObj.getStart_date());
				etEndDate.setText(medicationObj.getEnd_date());
				btnBack = (Button) findViewById(R.id.btnBack);
				btnBack.setOnClickListener(this);
				btnSave = (Button) findViewById(R.id.btnSave);
				btnSave.setOnClickListener(this);
				if (medicationObj.getUserId() == 0) {
					disableEditFields();
				}
			}
		}

	}

	private void disableEditFields() {
		etDrugName.setEnabled(false);
		etDosage.setEnabled(false);
		etIndication.setEnabled(false);
		etStartDate.setEnabled(false);
		etEndDate.setEnabled(false);
		btnSave.setVisibility(View.GONE);
	}

	/*** calling web services start **********/
	private void executeService() {

		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String ServiceName, parmsString;

		newMedication = new MedicationClass(0, Utils.readPreferences(this,
				"userId", -1), etDrugName.getText().toString(), etDosage
				.getText().toString(), etIndication.getText().toString(),
				etReminder.getText().toString(), etStartDate.getText()
						.toString(), "", "current", etMoreInfo.getText()
						.toString(), TreatmentClass.treatmetnList.get(
						medicationParentId).getTreatmentID());

		parmsString = "treatmentId=" + newMedication.getTreatmentId()
				+ "&medicationName="
				+ URLEncoder.encode(newMedication.getMedicationName())
				+ "&dosage=" + URLEncoder.encode(newMedication.getDosage())
				+ "&indication=" + newMedication.getIndication() + "&reminder="
				+ URLEncoder.encode(newMedication.getReminder())
				+ "&start_date=" + newMedication.getStart_date() + "&end_date="
				+ newMedication.getEnd_date() + "&treatment_type="
				+ newMedication.getTreatment_type() + "&userId="
				+ newMedication.getUserId();

		if (treatmentType.equalsIgnoreCase("New Drug")) {

			ServiceName = "add_medication";

		} else {

			ServiceName = "update_mdication";

			newMedication.setMedicationId(medicationObj.getMedicationId());

			parmsString += "&medicationId=" + newMedication.getMedicationId();
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

			if (action.equalsIgnoreCase("add_medication")) {
				if (status) {

					int insertId = (Integer) json.get("insertId");

					newMedication.setMedicationId(insertId);

					TreatmentClass.treatmetnList.get(medicationParentId)
							.getMedicationList().add(newMedication);

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgMedicationInsert));
				} else {

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgError));
				}
			} else if (action.equalsIgnoreCase("update_mdication")) {

				if (status) {
					TreatmentClass.treatmetnList.get(medicationParentId).medicationList
							.set(medicationId, newMedication);

					HelperMethods.showToast(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.msgMedicationUpdate));

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
			if (treatmentType.equalsIgnoreCase("Current")
					|| treatmentType.equalsIgnoreCase("Previous")) {

				intent = new Intent(getApplicationContext(),
						TreatmentMedicationOption.class);
				intent.putExtra("medicationIdIndex", medicationId);
				intent.putExtra("medicationParentId", medicationParentId);

			} else {
				intent = new Intent(getApplicationContext(),
						TreatmentMedication.class);

				intent.putExtra("title",
						TreatmentClass.treatmetnList.get(medicationParentId)
								.getTreatmentName());
				intent.putExtra("parentIdIndex", medicationParentId);
			}

			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnSave:

			if (etDrugName.getText().toString().isEmpty()
					|| etDosage.getText().toString().isEmpty()
					|| etStartDate.getText().toString().isEmpty()) {
				HelperMethods.showToast(getApplicationContext(), getResources()
						.getString(R.string.msgEmptyField));
				return;
			}

			executeService();

			break;
		}

	}
}
