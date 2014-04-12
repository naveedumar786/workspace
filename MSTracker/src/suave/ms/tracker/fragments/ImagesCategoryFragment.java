package suave.ms.tracker.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import suave.ms.tracker.R;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.ImagesClass;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ImagesCategoryFragment extends Fragment implements
		OnTaskCompleted, OnClickListener {

	private static final String TAG = "ImagesCategoryFragment ";
	OnUpdateListener listener;
	Context context;
	ListView lvImagesCategory;
	ImagesListAdapter adapter;

	TextView tvTopMenuTitle;
	Button btnAdd, btnBack;
	String title;
	String webservcieName;
	private boolean isFromImages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.simple_list_view, container,
				false);

		// isFromImages = getArguments().getBoolean("isFromImages");
		isFromImages = ImagesClass.isImages;
		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);

		if (isFromImages) {
			// To identify which type of data ImagesClass is Holding in other
			// fragments
			ImagesClass.isImages = true;
			title = getString(R.string.txtImages);
			webservcieName = "getimages";
		} else {
			ImagesClass.isImages = false;
			title = getString(R.string.txtLabs);
			webservcieName = "labs";
		}

		tvTopMenuTitle.setText(title);
		btnAdd = (Button) getActivity().findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.GONE);

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		if (ImagesClass.imagesCategoryList.size() == 0) {
			// call web services...
			serverRequest();
		}

		lvImagesCategory = (ListView) view.findViewById(R.id.lvSimpleList);
		adapter = new ImagesListAdapter(context);
		lvImagesCategory.setAdapter(adapter);

		lvImagesCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImagesClass.parentIndex = position;
				listener.OnUpdateImagesFragment(position);
			}

		});

		return view;
	}

	/*** calling web services start **********/
	private void serverRequest() {

		if (HelperMethods.isNetworkAvailable(getActivity())) {

			LoadingDialog.showLoadingDialog(context);

			int userId = suave.ms.tracker.helper.Utils.readPreferences(
					getActivity(), "userId", 0);

			Webservices.getData(this, webservcieName, "userid=" + userId);
		} else {
			HelperMethods.showToast(context,
					this.getResources().getString(R.string.msgNetWorkError));
		}

	}

	/*** calling web services end **********/

	/******************** class Images ListAdapter start *******************************/
	// Schedule category List Adapter
	class ImagesListAdapter extends BaseAdapter {

		Context context;
		String TAG = "ImagesListAdapter ";

		public ImagesListAdapter(Context c) {
			context = c;

		}

		@Override
		public int getCount() {
			// return the total number of item
			return ImagesClass.imagesCategoryList.size();
		}

		@Override
		public Object getItem(int index) {
			// return the object at position
			return ImagesClass.imagesCategoryList.get(index);
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
			View row = inflater.inflate(R.layout.list_view_row, parent, false);
			TextView title = (TextView) row.findViewById(R.id.tvText);
			ImagesClass imageCategory = ImagesClass.imagesCategoryList
					.get(index);

			// Log.e(TAG, imageCategory.getCategoryName());

			title.setText(imageCategory.getCategoryName());
			return row;
		}

	}

	/******************** class TreatmentList Adapter end *******************************/

	public void onSuccessWebservice(JSONObject json, String action) {

		try {
			// boolean status = json.getBoolean("status");
			if (json.getBoolean("status")) {

				Globals.IMAGES_PATH = json.getString("url");

				JSONArray arrayOfResults = json.getJSONArray("results");

				for (int i = 0; i < arrayOfResults.length(); i++) {

					JSONObject rObj = arrayOfResults.getJSONObject(i);

					JSONArray arrayImages_imagesList = rObj
							.getJSONArray("images");

					ArrayList<ImagesClass> Images_imagesList = new ArrayList<ImagesClass>();

					if (arrayImages_imagesList.length() > 0) {

						for (int k = 0; k < arrayImages_imagesList.length(); k++) {

							JSONObject imagesObj = arrayImages_imagesList
									.getJSONObject(k);

							// Log.e(imagesObj.getString("image_name") +
							// " K = ",
							// Integer.toString(k));

							if (isFromImages) {
								Images_imagesList
										.add(new ImagesClass(
												imagesObj.getInt("image_id"),
												Globals.IMAGES_PATH
														+ imagesObj
																.getString("image_name")));
							} else {
								Images_imagesList.add(new ImagesClass(imagesObj
										.getInt("id"), Globals.IMAGES_PATH
										+ imagesObj.getString("images")));
							}

						}

					}
					if (isFromImages) {
						ImagesClass.imagesCategoryList.add(new ImagesClass(rObj
								.getInt("imageCatId"), rObj
								.getString("categoryName"), Images_imagesList));
					} else {
						ImagesClass.imagesCategoryList.add(new ImagesClass(rObj
								.getInt("id"), rObj.getString("lab_name"),
								Images_imagesList));
					}

					Images_imagesList.clear();
				}

				adapter.notifyDataSetChanged();

			} else {
				Log.e(TAG, "No record Found.");
			}
		} catch (Exception e) {
			Log.e(TAG + "Exception", "" + e);
		}

		LoadingDialog.cancelLoadingDialog();
	}

	// Async service call Success response
	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(TAG, "onFailureWebservice");
		HelperMethods.showToast(context,
				getResources().getString(R.string.msgConnectionError));

		LoadingDialog.cancelLoadingDialog();
	}

	/*** calling web services end **********/

	public interface OnUpdateListener {
		public void OnUpdateImagesFragment(int index);

	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
		if (activity instanceof OnUpdateListener) {
			listener = (OnUpdateListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ "must implemenet ImagesFragment.OnUpdateListener");
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
			break;

		default:
			break;
		}
	}
}