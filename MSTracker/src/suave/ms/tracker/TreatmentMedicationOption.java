package suave.ms.tracker;

import java.util.ArrayList;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.MedicationClass;
import suave.ms.tracker.helper.TreatmentClass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class TreatmentMedicationOption extends MainActivity {
	ArrayList<String> medicationOptionList;
	String tag = "TreatmentMedicationOption";
	String activityTitle = "Current or Previous";

	int medicationId, medicationParentId;
	MedicationClass medicationObj;
	ListView lvMedicationOptions;
	Button btnBack;
	TextView tvTopMenuTitle;
	MedicationOptionListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		// context to exit activity From global
		super.activeActivityContext = this;

		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);
		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);
		Log.e(tag, "OnCreate");
		Bundle extra = getIntent().getExtras();
		if (extra != null) {

			medicationId = extra.getInt("medicationIdIndex");
			medicationParentId = extra.getInt("medicationParentId");
			// get selected medication
			medicationObj = TreatmentClass.treatmetnList
					.get(medicationParentId).medicationList.get(medicationId);

			Log.e(tag, "medicationIdIndex:" + medicationId
					+ " medicationParentId:" + medicationParentId);
		}

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		lvMedicationOptions = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new MedicationOptionListAdapter(this);

		// remove the more information for user created medication
		if (medicationObj.getUserId() > 0) {
			medicationOptionList.remove(2);
		}

		lvMedicationOptions.setAdapter(adapter);

		lvMedicationOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				optionListClickHandler(position);

			}

		});

		adapter.notifyDataSetChanged();
	}

	private void optionListClickHandler(int position) {
		String optionString = (String) medicationOptionList.get(position);
		Intent intent;
		String treatmentType = "";
		switch (position) {
		case 0:
			treatmentType = "Current";
			// HelperMethods.showToast(this, optionString);
			break;
		case 1:
			treatmentType = "Previous";

			// HelperMethods.showToast(this, optionString);
			break;
		case 2:

			Log.e("info log url", medicationObj.getMoreInfo());

			if (medicationObj.getMoreInfo().equalsIgnoreCase("null")
					|| medicationObj.getMoreInfo().equalsIgnoreCase("")
					|| medicationObj.getMoreInfo() == null) {
				HelperMethods.showToast(this,
						getResources().getString(R.string.msgNoIfnormation));
			} else {
				Uri url = Uri.parse(medicationObj.getMoreInfo());
				intent = new Intent(Intent.ACTION_VIEW, url);
				startActivity(intent);
			}

			return;
			// HelperMethods.showToast(this, optionString);

		default:
			HelperMethods.showToast(this, optionString);
			break;
		}
		// Loading the Schedule for current category
		intent = new Intent(TreatmentMedicationOption.this,
				TreatmentDrugInformation.class);

		intent.putExtra("medicationIdIndex", medicationId);

		intent.putExtra("medicationParentId", medicationParentId);

		intent.putExtra("treatmentType", treatmentType);
		// Load Activity
		startActivity(intent);
		finish();
	}

	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class MedicationOptionListAdapter extends BaseAdapter {

		Context context;

		public MedicationOptionListAdapter(Context c) {
			context = c;
			initOPtionList();

		}

		private void initOPtionList() {
			String[] mediactionOptionArray;
			medicationOptionList = new ArrayList<String>();
			mediactionOptionArray = getResources().getStringArray(
					R.array.medicationOptionList);
			for (int i = 0; i < mediactionOptionArray.length; i++) {
				medicationOptionList.add(mediactionOptionArray[i]);
			}
		}

		@Override
		public int getCount() {
			// return the total number of item
			return medicationOptionList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return medicationOptionList.get(index);
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

			String optionString = (String) medicationOptionList.get(index);
			Log.e(tag + "test heree", optionString);

			title.setText(optionString);
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
		switch (v.getId()) {
		case R.id.btnBack:
			Intent intent = new Intent(getApplicationContext(),
					TreatmentMedication.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}
}
