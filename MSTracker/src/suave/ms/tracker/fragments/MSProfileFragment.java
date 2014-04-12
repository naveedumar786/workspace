package suave.ms.tracker.fragments;

import java.util.ArrayList;
import java.util.Collections;

import suave.ms.tracker.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MSProfileFragment extends Fragment implements OnClickListener {

	private static final String TAG = "MSProfileFragment ";
	OnUpdateListener listener;
	Context context;
	TextView tvTopMenuTitle;
	Button btnAdd, btnBack;
	ListView lv;
	injectionListAdapter adapter;

	ArrayList<MSProfileList> profileList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.simple_list_view, container,
				false);

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);

		tvTopMenuTitle.setText(getString(R.string.txtMSProfile));

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		((Button) getActivity().findViewById(R.id.btnAdd))
				.setVisibility(View.GONE);

		profileList = new ArrayList<MSProfileList>();
		String[] list = getResources().getStringArray(R.array.msprofile_list);
		for (int i = 0; i < list.length; i++) {
			boolean hasNext = true;
			if (i == 3 || i == 4 || i == 5) {
				hasNext = false;
			}
			profileList.add(new MSProfileList(list[i], i, hasNext));
		}
		// Collections.addAll(profileList,
		// getResources().getStringArray(R.array.msprofile_list));

		lv = (ListView) view.findViewById(R.id.lvSimpleList);
		adapter = new injectionListAdapter(getActivity());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

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
			return profileList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return profileList.get(index);
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
			Button btnNext = (Button) row.findViewById(R.id.btnNext);
			if (!profileList.get(index).hasNext) {
				btnNext.setVisibility(View.GONE);
			}
			TextView title = (TextView) row
					.findViewById(R.id.tvScheduleCateTitle);

			title.setText(profileList.get(index).text);
			return row;
		}
	}

	/******************** class medication List Adapter end *******************************/
	public interface OnUpdateListener {
		public void OnUpdateFragment();

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
		default:
			break;
		}

	}

	class MSProfileList {
		public String text;
		public int id;
		public boolean hasNext;

		/**
		 * @param text
		 * @param id
		 */
		public MSProfileList(String text, int id, boolean hasNext) {
			super();
			this.text = text;
			this.id = id;
			this.hasNext = hasNext;
		}

	}
}
