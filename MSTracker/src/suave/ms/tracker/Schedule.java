package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.ScheduleCategoryClass;
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

public class Schedule extends MainActivity implements OnTaskCompleted {
	ListView lvScheduleCatList;
	ScheduleListAdapter adapter;
	ArrayList<ScheduleCategoryClass> ScheduleList;
	Button btnBack;
	LoadingDialog dialog;
	String tag = "Schedule";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_view);
		// context to exit activity From global
		super.activeActivityContext = this;

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		// calling web services
		serverRequest();

		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);

		lvScheduleCatList = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new ScheduleListAdapter(this);
		lvScheduleCatList.setAdapter(adapter);

		lvScheduleCatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Loading the Schedule for current category
				Intent intent = new Intent(Schedule.this,
						ScheduleAppointment.class);

				intent.putExtra("title", ScheduleList.get(position).getTitle());
				intent.putExtra("id", ScheduleList.get(position).getId());
				// Load Activity
				startActivity(intent);
				finish();
			}

		});

	}

	/*** calling web services start **********/
	private void serverRequest() {

		ScheduleList = new ArrayList<ScheduleCategoryClass>();

		if (HelperMethods.isNetworkAvailable(this)) {
			showLoadingDialog(getResources().getString(
					R.string.msgLoadingWebservice));
			Webservices.getData(this, "scheduleCategories", "");
		} else {
			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
		}

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

	/******************** class ScheduleListAdapter start *******************************/
	// Schedule category List Adapter
	class ScheduleListAdapter extends BaseAdapter {

		Context context;

		public ScheduleListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return ScheduleList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return ScheduleList.get(index);
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
			ScheduleCategoryClass temp = ScheduleList.get(index);
			title.setText(temp.getTitle());
			return row;
		}

	}

	/******************** class ScheduleListAdapter end *******************************/
	public void onSuccessWebservice(JSONObject json, String action) {

		try {
			boolean status = (Boolean) json.get("status");
			if (status) {

				JSONArray arrayOfResults = json.getJSONArray("results");

				for (int i = 0; i < arrayOfResults.length(); i++) {
					JSONObject rObj = arrayOfResults.getJSONObject(i);

					ScheduleList
							.add(new ScheduleCategoryClass(rObj
									.getString("catName"), rObj
									.getInt("scheduleCatId")));
				}
				adapter.notifyDataSetChanged();
				cancelLoadingDialog();
			}
		} catch (Exception e) {
			Log.e("Exception", "" + e);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onClick(btnBack);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			Intent intent = new Intent(Schedule.this, MSTrackerHome.class);
			// Load Activity
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
