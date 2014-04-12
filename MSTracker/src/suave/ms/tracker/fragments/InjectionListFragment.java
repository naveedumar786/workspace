package suave.ms.tracker.fragments;

import java.util.ArrayList;

import suave.ms.tracker.helper.InjectionTrackerClass;
import suave.ms.tracker.R;
import suave.ms.tracker.helper.HelperMethods;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InjectionListFragment extends Fragment implements OnClickListener {

	private static final String TAG = "InjectionListFragment ";
	OnUpdateListener listener;
	Context context;
	TextView tvTopMenuTitle;
	Button btnAdd, btnBack;
	ListView lvInjection;
	injectionListAdapter adapter;
	ArrayList<InjectionTrackerClass> injectionList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.simple_list_view, container,
				false);

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);

		tvTopMenuTitle.setText(getString(R.string.txtInjectionTracker));

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnAdd = (Button) getActivity().findViewById(R.id.btnAdd);
		btnAdd.setBackgroundResource(R.drawable.add_button);
		btnAdd.setOnClickListener(this);

		injectionList = InjectionTrackerClass.bodyPartList
				.get(InjectionTrackerClass.selectedIndex).injectionList;

		lvInjection = (ListView) view.findViewById(R.id.lvSimpleList);
		adapter = new injectionListAdapter(getActivity());
		lvInjection.setAdapter(adapter);
		lvInjection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Doing Nothing
			}

		});

		adapter.notifyDataSetChanged();

		return view;
	}

	/******************** class Treatment ListAdapter start *******************************/
	// Schedule category List Adapter
	class injectionListAdapter extends BaseAdapter {

		Context context;
		Button btnUpdate;

		public injectionListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return injectionList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return injectionList.get(index);
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

			/*** Register update Button start *****/
			btnUpdate = (Button) row.findViewById(R.id.btnUpdate);
			btnUpdate.setTag(index);
			btnUpdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					int index = (Integer) view.getTag();
					InjectionTrackerClass.selectedChildIndex = index;
					listener.OnUpdateInjectionListFragment();
					// updateUserDiagnosis(index);
				}
			});
			/*** Register update Button end *****/

			InjectionTrackerClass injection = injectionList.get(index);

			((CheckBox) row.findViewById(R.id.cbStatus))
					.setVisibility(View.GONE);

			TextView rowTitle = (TextView) row.findViewById(R.id.tvDrName);
			rowTitle.setText(injection.getInjectedDrug());

			TextView date = (TextView) row.findViewById(R.id.tvAppointmentDate);
			date.setText(injection.getInjectedDate());
			return row;
		}
	}

	/******************** class medication List Adapter end *******************************/
	public interface OnUpdateListener {
		public void OnUpdateInjectionListFragment();

	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
		if (activity instanceof OnUpdateListener) {
			listener = (OnUpdateListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ "must implemenet in main activity");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			getActivity().onBackPressed();
			break;
		case R.id.btnAdd:
			// To identify adding new record
			InjectionTrackerClass.selectedChildIndex = -1;
			listener.OnUpdateInjectionListFragment();
			break;

		default:
			break;
		}

	}
}
