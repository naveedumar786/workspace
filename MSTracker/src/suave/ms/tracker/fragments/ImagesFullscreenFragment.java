package suave.ms.tracker.fragments;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import suave.ms.tracker.R;
import suave.ms.tracker.helper.ImagesClass;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagesFullscreenFragment extends Fragment implements
		OnClickListener {

	String[] indexString;
	TextView tvTopMenuTitle;
	Button btnBack, btnAdd;
	ImageView imgDisplay;

	String TAG = "ImagesFullscreenFragment";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.image_fullscreen_view, container,
				false);

		// indexString = getArguments().getString("msg").split(",");
		// int parentIndex = Integer.parseInt(indexString[0]);
		// int childIndex = Integer.parseInt(indexString[1]);

		int parentIndex = ImagesClass.parentIndex;
		int childIndex = ImagesClass.childIndex;

		Log.e(TAG, "Selected position " + parentIndex + " " + childIndex);

		Log.e(TAG,
				"Selected Image name "
						+ ImagesClass.imagesCategoryList.get(parentIndex)
								.getImagesImagesList().get(childIndex)
								.getImageName());

		tvTopMenuTitle = (TextView) getActivity().findViewById(
				R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(ImagesClass.imagesCategoryList.get(parentIndex)
				.getCategoryName());

		btnBack = (Button) getActivity().findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnAdd = (Button) getActivity().findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.GONE);

		imgDisplay = (ImageView) view.findViewById(R.id.imgDisplay);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.broken_image_link)
				.cacheInMemory(true).cacheOnDisc(true)
				.showImageOnLoading(R.drawable.broken_image_link).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000)
				// 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		imageLoader.displayImage(ImagesClass.imagesCategoryList
				.get(parentIndex).getImagesImagesList().get(childIndex)
				.getImageName(),
				(ImageView) view.findViewById(R.id.imgDisplay), options);

		return view;
	}

	public interface OnUpdateListener {
		public void OnUpdateImagesFullscreenFragment(int index);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			getActivity().onBackPressed();
			break;
		}
	}

}
