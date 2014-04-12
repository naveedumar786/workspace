package suave.ms.tracker.helper;

import suave.ms.tracker.R;
import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

	ProgressDialog loadingDialog;
	static LoadingDialog dialog;
	private static Context context;

	public LoadingDialog(Context context) {

		loadingDialog = new ProgressDialog(context);

		loadingDialog.setTitle(null);

		loadingDialog.setIndeterminate(true);

		loadingDialog.setCancelable(true);

	}

	public void show() {

		loadingDialog.show();

	}

	public void cancel() {

		loadingDialog.cancel();

	}

	public void setMessage(String msg) {

		loadingDialog.setMessage(msg);

	}

	/**
	 * Modified Loading dialog
	 */
	public static ProgressDialog dialog2;

	public static void showLoadingDialog(Context context) {

		if (dialog2 == null) {
			dialog2 = new ProgressDialog(context);
			dialog2.setTitle(context.getString(R.string.msgLoadingWebservice));
			dialog2.setIndeterminate(true);
			dialog2.setCancelable(true);
		}

		dialog2.show();

	}

	public static void cancelLoadingDialog() {

		dialog2.cancel();

	}
}
