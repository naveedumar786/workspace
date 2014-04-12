package suave.ms.tracker;

import java.util.ArrayList;

import suave.ms.tracker.TreatmentMedication.MedicationListAdapter;
import suave.ms.tracker.helper.AppointmentsClass;
import suave.ms.tracker.helper.DiagnosisClass;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.TreatmentClass;
import suave.ms.tracker.helper.UserDiagnosisClass;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DiagnosisUserDiagnosis extends MainActivity {
	Button btnBack, btnAdd;
	TextView tvTopMenuTitle;
	ListView lvUserDiagnosis;
	UserDiagnosisListAdapter adapter;
	String tag = "TreatmentMedication.class";
	int parentId;
	public static ArrayList<UserDiagnosisClass> userDiagnosisList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		// context to exit activity From global
		super.activeActivityContext = this;

		// calling web services......
		// serverRequest();
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			parentId = extra.getInt("parentIdIndex");

		}
		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(DiagnosisClass.diagnosisList.get(parentId)
				.getCatName());

		Log.e(tag, "OnCreate");

		userDiagnosisList = DiagnosisClass.diagnosisList.get(parentId)
				.getUserDignosisList();
		if (userDiagnosisList.size() == 0) {
			HelperMethods.showToast(this, "No Record Found.");
		}

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.VISIBLE);
		btnAdd.setOnClickListener(this);

		lvUserDiagnosis = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new UserDiagnosisListAdapter(this);
		lvUserDiagnosis.setAdapter(adapter);

		lvUserDiagnosis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Loading the Medication option for current category
				Intent intent = new Intent(DiagnosisUserDiagnosis.this,
						DiagnosisUserDiagnoseAdd.class);

				intent.putExtra("userDiagnosisIndex", position);

				intent.putExtra("task", "Update");

				intent.putExtra("parentIndexId", parentId);

				// Load Activity
				startActivity(intent);
				finish();
			}

		});

		adapter.notifyDataSetChanged();
	}

	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class UserDiagnosisListAdapter extends BaseAdapter {

		Context context;
		Button btnUpdate;

		public UserDiagnosisListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return userDiagnosisList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return userDiagnosisList.get(index);
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

			UserDiagnosisClass userDiagnos = userDiagnosisList.get(index);

			/*** Register update Button start *****/
			btnUpdate = (Button) row.findViewById(R.id.btnUpdate);
			btnUpdate.setTag(index);
			btnUpdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					int index = (Integer) view.getTag();
					updateUserDiagnosis(index);
				}
			});
			/*** Register update Button end *****/
			((CheckBox) row.findViewById(R.id.cbStatus))
					.setVisibility(View.GONE);

			TextView drName = (TextView) row.findViewById(R.id.tvDrName);
			drName.setText(userDiagnos.getDoctorName());

			TextView daignosDatePlace = (TextView) row
					.findViewById(R.id.tvAppointmentDate);
			daignosDatePlace.setText(userDiagnos.getDiagnosisPlace() + "    "
					+ userDiagnos.getDiagnosisDate());
			return row;
		}
	}

	/*** update Button handler *****/
	void updateUserDiagnosis(int index) {
		UserDiagnosisClass userDiagnose = userDiagnosisList.get(index);
		Intent intent = new Intent(DiagnosisUserDiagnosis.this,
				DiagnosisUserDiagnoseAdd.class);

		intent.putExtra("userDiagnoseIndex", index);
		intent.putExtra("parentIndexId", parentId);
		intent.putExtra("task", "Update");

		// Load Activity
		startActivity(intent);
		finish();
	}

	/*** status checkbox handler *****/
	/******************** class medication List Adapter end *******************************/

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
			Globals.backButtonId = Globals.AC_DIAGNOSIS;
			intent = new Intent(getApplicationContext(), Diagnosis.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnAdd:
			intent = new Intent(getApplicationContext(),
					DiagnosisUserDiagnoseAdd.class);

			intent.putExtra("parentIndexId", parentId);

			intent.putExtra("task", "New Diagnose");
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}
}
