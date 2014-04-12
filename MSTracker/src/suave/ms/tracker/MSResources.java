package suave.ms.tracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.FutureAppointment.AppointmentListAdapter;
import suave.ms.tracker.helper.AppointmentsClass;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MSResources extends MainActivity implements OnTaskCompleted {

	Button btnBack, btnAdd;
	TextView tvTopMenuTitle;
	ListView lvMedication;
	ResourcesListAdapter adapter;
	String tag = "MSResources.class";
	String activityTitle = "MS Resources";
	LoadingDialog dialog;

	ArrayList<ResourcesClass> resouriceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		// context to exit activity From global
		super.activeActivityContext = this;

		((Button) findViewById(R.id.btnAdd)).setVisibility(View.GONE);

		resouriceList = new ArrayList<ResourcesClass>();

		// calling web services......
		serverRequest();

		tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(activityTitle);

		Log.e(tag, "OnCreate");

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		lvMedication = (ListView) findViewById(R.id.lvScheduleCatList);
		adapter = new ResourcesListAdapter(this);
		lvMedication.setAdapter(adapter);

		lvMedication.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!resouriceList.get(position).isTitle) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(resouriceList.get(position).resourceUrl));
					startActivity(browserIntent);
				}
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

		showLoadingDialog("Loading...");

		Webservices.getData(this, "msresources", "");

	}

	/*** calling web services end **********/
	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class ResourcesListAdapter extends BaseAdapter {

		Context context;
		CheckBox cbStatus;

		public ResourcesListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return resouriceList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return resouriceList.get(index);
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

			ResourcesClass resources = resouriceList.get(index);

			if (resources.isTitle) {
				drName.setText(resources.resourceText);
				appointmentDate.setVisibility(View.GONE);
				row.setOnClickListener(null);
				row.setOnLongClickListener(null);
				row.setLongClickable(false);
				row.setBackgroundResource(R.color.listTitleBackground);
			} else {
				drName.setText(resources.resourceText);
				appointmentDate.setText(resources.resourceUrl);
			}

			/*** Register update Button start *****/
			((Button) row.findViewById(R.id.btnUpdate))
					.setVisibility(View.GONE);

			((CheckBox) row.findViewById(R.id.cbStatus))
					.setVisibility(View.GONE);
			/*** Register update Button end *****/
			return row;
		}
	}

	/******************** class AppointmentsAdapter end *******************************/

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {

			if (json.getBoolean("status")) {
				JSONObject results = json.getJSONObject("result");
				JSONArray resultsArray = results.getJSONArray("details");

				for (int i = 0; i < resultsArray.length(); i++) {

					JSONObject obj = resultsArray.getJSONObject(i);

					ResourcesClass resourice = new ResourcesClass(
							obj.getString("categoryname"), "", true);
					resouriceList.add(resourice);

					JSONArray subcats = obj.getJSONArray("subcategory");

					for (int j = 0; j < subcats.length(); j++) {

						String text = subcats.getString(j);
						String url = subcats.getString(++j);

						resourice = new ResourcesClass(text, url, false);
						resouriceList.add(resourice);

					}
				}

			} else {
				HelperMethods.showToast(getApplicationContext(),
						"No Record Found");
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
		Intent intent = new Intent(MSResources.this, MSTrackerHome.class);
		// Load Activity
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			onBackPressed();
			break;

		default:
			break;
		}

	}

	class ResourcesClass {
		public String resourceText, resourceUrl;
		public boolean isTitle;

		/**
		 * @param resourceText
		 * @param resourceurl
		 * @param isTitle
		 */
		public ResourcesClass(String resourceText, String resourceUrl,
				boolean isTitle) {
			super();
			this.resourceText = resourceText;
			this.resourceUrl = resourceUrl;
			this.isTitle = isTitle;
		}
	}
}
