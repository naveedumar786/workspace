package suave.ms.tracker;

import java.net.URLEncoder;

import org.json.JSONObject;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.OnTaskCompleted;
import suave.ms.tracker.helper.Utils;
import suave.ms.tracker.helper.Webservices;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPassword extends Activity implements OnClickListener,
		OnTaskCompleted {

	Button btnRecoverPassword, btnBack;
	EditText etEmail;
	String recoverPassword = "recoverPassword";
	String tag = "ForgetPassword";
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);

		(btnRecoverPassword = (Button) findViewById(R.id.btnRecoverPassword))
				.setOnClickListener(this);

		(btnBack = (Button) findViewById(R.id.btnBack))
				.setOnClickListener(this);

		(etEmail = (EditText) findViewById(R.id.etEmail))
				.setOnClickListener(this);

	}

	private void recoverPassword() {
		// hide the soft key board
		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String email = etEmail.getText().toString();

		if (HelperMethods.isEmailValid(email)) {
			String parmsString = "email=" + URLEncoder.encode(email);

			showLoadingDialog(getResources().getString(
					R.string.msgLoadingWebservice));

			Webservices.getData(this, recoverPassword, parmsString);
		} else {
			HelperMethods.showToast(this,
					getResources().getString(R.string.msgInvalidEmail));
		}

	}

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {
			boolean status = (Boolean) json.get("status");

			if (action.equalsIgnoreCase(recoverPassword)) {

				if (status) {
					alertDialog(
							this,
							getResources().getString(
									R.string.txtRecoverPassword),
							"A new password has been sent to your e-mail address",
							recoverPassword);
				} else {
					HelperMethods.showToast(this,
							getResources().getString(R.string.msgInvalidEmail));
				}
			}
		} catch (Exception e) {
			Log.e(tag + " Exception", "" + e);
		}

		cancelLoadingDialog();

	}

	@Override
	public void onFailureWebservice(boolean satus) {
		Log.e(tag, "onFailureWebservice");
		alertDialog(this,
				getResources().getString(R.string.msgConnectionErrorTitle),
				getResources().getString(R.string.msgConnectionError),
				"onFailureWebservice");
		cancelLoadingDialog();

	}

	private void alertDialog(Context context, String dialogTitle,
			String dialogMessage, final String action) {
		AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();

		alertDialog1.setTitle(dialogTitle);

		alertDialog1.setMessage(dialogMessage);

		// Setting Icon to Dialog
		// alertDialog1.setIcon(R.drawable.tick);

		alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (action.equalsIgnoreCase(recoverPassword)) {

					Intent intent = new Intent(getApplicationContext(),
							Login.class);
					startActivity(intent);
					finish();
				}
				// Write your code here to execute after dialog
				// closed
				// Toast.makeText(getApplicationContext(), "You clicked on OK",
				// Toast.LENGTH_SHORT).show();
			}
		});

		// Showing Alert Message
		alertDialog1.show();
	}

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
		onClick(btnBack);
	}

	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.btnRecoverPassword:
			Utils.hideSoftkeybord(this);
			recoverPassword();
			break;

		case R.id.btnBack:
			intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

}
