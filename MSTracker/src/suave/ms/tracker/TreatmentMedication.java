package suave.ms.tracker;

import java.util.ArrayList;

import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.TreatmentClass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TreatmentMedication extends MainActivity {
	// ArrayList<ScheduleCategoryClass> treatmentList;
	Button btnBack, btnAdd;
	TextView tvTopMenuTitle;
	ListView lvMedication;
	MedicationListAdapter adapter;
	String tag = "TreatmentMedication.class";
	String activityTitle = "Medication for MS";
	int parentId;
	public static ArrayList<MedicationClass> medicationList;

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
			String parentTitle = extra.getString("title");

		}
		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		Log.e(tag, "OnCreate");

		medicationList = TreatmentClass.treatmetnList.get(parentId)
				.getMedicationList();

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.VISIBLE);
		btnAdd.setOnClickListener(this);

		lvMedication = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new MedicationListAdapter(this);
		lvMedication.setAdapter(adapter);

		lvMedication.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Loading the Medication option for current category
				Intent intent = new Intent(TreatmentMedication.this,
						TreatmentMedicationOption.class);

				intent.putExtra("medicationIdIndex", position);

				intent.putExtra("medicationParentId", parentId);

				// Load Activity
				startActivity(intent);
				finish();
			}

		});

		adapter.notifyDataSetChanged();
	}

	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class MedicationListAdapter extends BaseAdapter {

		Context context;

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
			View row = inflater.inflate(R.layout.medication_option_row, parent,
					false);
			TextView title = (TextView) row
					.findViewById(R.id.tvScheduleCateTitle);

			MedicationClass medication = medicationList.get(index);
			Log.e(tag + "test heree", medication.getMedicationName());

			title.setText(medication.getMedicationName());
			return row;
		}
	}

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
			Globals.backButtonId = Globals.AC_TREATMENT_MEDICATION;
			intent = new Intent(getApplicationContext(), Treatment.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;
		case R.id.btnAdd:
			intent = new Intent(getApplicationContext(),
					TreatmentDrugInformation.class);

			intent.putExtra("medicationIdIndex", 0);

			intent.putExtra("medicationParentId", parentId);

			intent.putExtra("treatmentType", "New Drug");
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}
}
