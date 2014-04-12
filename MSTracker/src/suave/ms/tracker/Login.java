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
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener, OnTaskCompleted {

	Button btnLogin, btnFrogetPassword, btnRegisterd;
	EditText etEmail, etPassword;
	CheckBox cbRememberMe;
	LoadingDialog dialog;
	// Services
	String registerDevice = "registerDevice";
	String loginUser = "loginUser";

	String tag = "class Login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// Buttons
		(btnLogin = (Button) findViewById(R.id.btnLogin))
				.setOnClickListener(this);

		(btnRegisterd = (Button) findViewById(R.id.btnRegisterd))
				.setOnClickListener(this);

		(btnFrogetPassword = (Button) findViewById(R.id.btnFrogetPassword))
				.setOnClickListener(this);
		// Input fields
		(etEmail = (EditText) findViewById(R.id.etEmail))
				.setOnClickListener(this);

		(etPassword = (EditText) findViewById(R.id.etPassword))
				.setOnClickListener(this);

		(cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe))
				.setOnClickListener(this);

		boolean isRemember = Utils.readPreferences(this, "isRemember", false);

		if (isRemember) {
			cbRememberMe.setChecked(true);
			etEmail.setText(Utils
					.readPreferences(this, "rememberUserEmail", ""));
		}
	}

	private void validateUser() {

		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString().trim();
		String android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		// saving remember me Preferences
		if (cbRememberMe.isChecked()) {

			Utils.savePreferences(this, "isRemember", true);
			Utils.savePreferences(this, "rememberUserEmail", email);
			// Log.e("validateUser", "checked" + email + " is saved");

		} else {
			// Log.e("validateUser", "unchecked");
			Utils.clearPreferences(this, "isRemember");
			Utils.clearPreferences(this, "rememberUserEmail");
		}

		// verifying network status
		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}
		// verifying email and password
		if (!HelperMethods.isEmailValid(email) || password.length() < 6) {
			alertDialog(this, "Login error",
					getResources().getString(R.string.msgErrorLogin), "login");
			return;
		}
		// Preparing web service parameters
		String parmsString = "email=" + URLEncoder.encode(email) + "&password="
				+ URLEncoder.encode(password) + "&userDeviceId="
				+ URLEncoder.encode(android_id);
		// Log.e("Parms ", parmsString);
		showLoadingDialog(getResources().getString(
				R.string.msgLoadingWebservice));
		// sending request

		Webservices.getData(this, loginUser, parmsString);
	}

	private void registerDevice() {
		String android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);

		int userId = Utils.readPreferences(this, "userId", 0);

		String parmsString = "userId=" + userId + "&userDeviceId=" + android_id
				+ "&deviceType=android " + HelperMethods.getDeviceName();

		showLoadingDialog(getResources().getString(
				R.string.msgLoadingWebservice));

		Webservices.getData(this, registerDevice, parmsString);

	}

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		try {
			boolean status = (Boolean) json.get("status");
			if (action.equalsIgnoreCase(loginUser)) {

				boolean userStatus = (Boolean) json.get("userStatus");
				if (status && userStatus) {

					JSONObject user = json.getJSONObject("user");
					// save use information in shared Preferences
					Utils.savePreferences(this, "userId", user.getInt("userId"));

					Utils.savePreferences(this, "userEmail",
							user.getString("userEmail"));

					if (!(Boolean) json.get("device")) {

						alertDialog(this, "New Device Found",
								"Register the new Device.", "registerDevice");

					} else {
						goToHomeMenu();
					}
					// account registered but not confirmed
				} else if (status && !userStatus) {
					etPassword.setText("");

					alertDialog(this, "Login error",
							"Email confirmation required", "errorLogin");
					// email or password invalid or user not found
				} else {
					etPassword.setText("");
					alertDialog(this, "Login error",
							"Invalid email or password", "errorLogin");
				}
				// login using different device
			} else if (action.equalsIgnoreCase(registerDevice)) {
				if (status) {

					HelperMethods.showToast(this, json.get("message")
							.toString());
					goToHomeMenu();
				} else {
					Log.e(tag, "onSuccessWebservice");
					HelperMethods.showToast(this, json.get("message")
							.toString());
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

	private void goToHomeMenu() {
		Intent intent = new Intent(this, MSTrackerHome.class);
		startActivity(intent);
		finish();

	}

	private void alertDialog(Context context, String dialogTitle,
			String dialogMessage, final String action) {
		AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog1.setTitle(dialogTitle);

		// Setting Dialog Message
		alertDialog1.setMessage(dialogMessage);

		// Setting Icon to Dialog
		// alertDialog1.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (action.equalsIgnoreCase("registerDevice")) {
					registerDevice();
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
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnLogin:
			Utils.hideSoftkeybord(this);
			validateUser();
			break;
		case R.id.btnFrogetPassword:
			intent = new Intent(this, ForgetPassword.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btnRegisterd:
			intent = new Intent(this, RegisterAccount.class);
			startActivity(intent);
			finish();
			break;
		case R.id.cbRememberMe:
			if (cbRememberMe.isChecked()) {
				Log.e("onClick", "cbRememberMe Checked");
			} else {
				Log.e("onClick", "cbRememberMe un Checked");
			}
			break;
		default:
			break;
		}
	}

}
