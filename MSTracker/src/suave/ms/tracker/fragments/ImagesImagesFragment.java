package suave.ms.tracker.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import suave.ms.tracker.R;
import suave.ms.tracker.fragments.ImagesCategoryFragment.ImagesListAdapter;
import suave.ms.tracker.fragments.ImagesCategoryFragment.OnUpdateListener;
import suave.ms.tracker.helper.Globals;
import suave.ms.tracker.helper.GridViewAdapter;
import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.ImageItem;
import suave.ms.tracker.helper.ImagesClass;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ImagesImagesFragment extends Fragment implements OnClickListener {
	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	Context context;
	OnUpdateListener listener;
	int index;

	TextView tvTopMenuTitle;
	Button btnAdd, btnBack;
	private static final String TAG = "ImagesImagesFragment ";

	public int RESULT_LOAD_IMAGE = 492;
	private int CAMERA_REQUEST = 1888;

	String uploadServiceType;
	ArrayList<ImageItem> imageItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.simple_grid_view, container,
				false);

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		// index = Integer.parseInt(getArguments().getString("msg"));
		index = ImagesClass.parentIndex;

		if (ImagesClass.isImages) {
			uploadServiceType = "categoryimage";
		} else {
			uploadServiceType = "labimages";
		}

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);

		tvTopMenuTitle.setText(ImagesClass.imagesCategoryList.get(index)
				.getCategoryName());

		btnAdd = (Button) getActivity().findViewById(R.id.btnAdd);
		btnAdd.setBackgroundResource(R.drawable.add_image);

		btnAdd.setVisibility(View.VISIBLE);
		btnAdd.setOnClickListener(this);

		gridView = (GridView) view.findViewById(R.id.gridView);
		customGridAdapter = new GridViewAdapter(context,
				R.layout.simple_grid_view_row, getData());

		gridView.setAdapter(customGridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Log.e(TAG, "Selected position " + position);
				ImagesClass.childIndex = position;
				listener.OnUpdateImagesImagesFragment(index, position);
				// Toast.makeText(context, position + "#Selected",
				// Toast.LENGTH_LONG).show();
			}

		});

		return view;
	}

	private ArrayList<ImageItem> getData() {

		imageItems = new ArrayList<ImageItem>();
		// retrieve String drawable array
		// TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);

		ArrayList<ImagesClass> imagesList = ImagesClass.imagesCategoryList.get(
				index).getImagesImagesList();

		Log.e("images List size ", "" + imagesList.size());
		if (imagesList.size() == 0) {
			HelperMethods.showToast(context,
					getString(R.string.txtNoRecordFound));
		}
		for (int i = 0; i < imagesList.size(); i++) {

			imageItems.add(new ImageItem(imagesList.get(i).getImageName(),
					"Image#" + i));
		}

		return imageItems;

	}

	public void showImageUploadDialog() {

		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.image_upload_dialog);

		Button btnGallery = (Button) dialog.findViewById(R.id.btnGallery);
		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);

		btnCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
				dialog.cancel();
			}
		});

		btnGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
				dialog.cancel();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * Upload image to device external storages
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.e("onActivityResult", "onActivityResult");

		String imageName = "img.jpg";
		if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == android.app.Activity.RESULT_OK && data != null) {

			String picturePath;
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getActivity().getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);

			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			Log.e("picturePath", "" + picturePath);

			// mIvProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			Bitmap photo = BitmapFactory.decodeFile(picturePath);

			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// options.inSampleSize = 4;

			if (saveImageToSD(photo, imageName)) {
				LoadingDialog.showLoadingDialog(context);
				uploadImage(imageName);
			}

		} else if (requestCode == CAMERA_REQUEST
				&& resultCode == android.app.Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			// mIvProfile.setImageBitmap(photo);

			if (saveImageToSD(photo, imageName)) {
				LoadingDialog.showLoadingDialog(context);
				uploadImage(imageName);
			}

		}

		// if (mHelper != null
		// && !mHelper.handleActivityResult(requestCode, resultCode, data)) {
		// super.onActivityResult(requestCode, resultCode, data);
		// }
	}

	/**
	 * Save image to app directory
	 * 
	 * @Bitmap image
	 * @String name
	 * @return
	 */
	public boolean saveImageToSD(Bitmap image, String name) {
		Log.e("saveImageToSD", "saveImageToSD");

		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ getString(R.string.app_name)
				+ "/images/";

		try {

			Log.e("onActivityResult", "" + fullPath + name);

			File dir = new File(fullPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			OutputStream fOut = null;
			File file = new File(fullPath, name);
			file.createNewFile();
			fOut = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

			fOut.flush();
			fOut.close();
			return true;

		} catch (Exception e) {
			Log.e("saveToExternalStorage()", "" + e.getMessage());
			return false;
		}
	}

	/**
	 * Save image to Live server
	 * 
	 * @String nameName
	 * @return
	 */
	public void uploadImage(String imageName) {

		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ getString(R.string.app_name)
				+ "/images/" + imageName;

		AsyncHttpClient client = new AsyncHttpClient();
		File myFile = new File(fullPath);
		RequestParams params1 = new RequestParams();

		try {
			int userId = Utils.readPreferences(getActivity(), "userId", -1);
			int catId = ImagesClass.imagesCategoryList.get(index)
					.getCategoryId();

			params1.put("imageName", myFile);
			params1.put("type", uploadServiceType);
			params1.put("catId", String.valueOf(catId));
			params1.put("userId", String.valueOf(userId));

			Log.e("upload ", "file name = " + Globals.UrlForImagesUpload
					+ fullPath + "type=" + uploadServiceType + "image&catId="
					+ catId + "&userId=1");

			client.post(Globals.UrlForImagesUpload, params1,
					new AsyncHttpResponseHandler() {
						public void onSuccess(String response) {
							Log.e("upload image response ", response);

							LoadingDialog.cancelLoadingDialog();

							JSONObject json = null;

							try {
								json = new JSONObject(response);
							} catch (JSONException e) {

							}
							String imgPath = "";
							try {
								imgPath = json.getString("URL");
								Log.e("imgPath ", imgPath);

								String imageName = json
										.getString("newImageName");
								int imageId = json.getInt("insertId");

								ImagesClass.imagesCategoryList.get(index).imagesImagesList
										.add(new ImagesClass(imageId,
												Globals.IMAGES_PATH + imageName));
								imageItems.add(new ImageItem(
										Globals.IMAGES_PATH + imageName,
										"Image#" + imageItems.size()));
								customGridAdapter.notifyDataSetChanged();

								Log.e("Images List size ",
										""
												+ ImagesClass.imagesCategoryList
														.get(index).imagesImagesList
														.size());

							} catch (Exception e) {
								Log.e("upload imgPath... ", imgPath);
							}

							// url.substring( url.lastIndexOf('/')+1,
							// url.length() );

						}

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							super.onFailure(arg0, arg1);
							LoadingDialog.cancelLoadingDialog();
						}
					});
		} catch (FileNotFoundException e) {
			LoadingDialog.cancelLoadingDialog();
			Log.e("Exception", "UploadImage" + e);
		}

	}

	public interface OnUpdateListener {
		public void OnUpdateImagesImagesFragment(int parentIndex, int childIndex);

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
			showImageUploadDialog();
			break;

		default:
			break;
		}

	}

}
