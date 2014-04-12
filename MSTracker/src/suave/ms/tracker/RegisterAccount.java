package suave.ms.tracker;

import java.net.URLEncoder;

import org.json.JSONObject;

import suave.ms.tracker.helper.HelperMethods;
import suave.ms.tracker.helper.LoadingDialog;
import suave.ms.tracker.helper.MyApplication;
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
import android.widget.EditText;

public class RegisterAccount extends Activity implements OnClickListener,
		OnTaskCompleted {

	Button btnRegisterd, btnBack, btnLogin;
	EditText etEmail, etPassword, etConfirmPassword;
	String registerUser = "registerUser", tag = "RegisterAccount";
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		btnRegisterd = (Button) findViewById(R.id.btnRegisterd);
		btnRegisterd.setOnClickListener(this);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		etEmail = (EditText) findViewById(R.id.etEmail);
		etEmail.setOnClickListener(this);

		etPassword = (EditText) findViewById(R.id.etPassword);
		etPassword.setOnClickListener(this);

		etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
		etConfirmPassword.setOnClickListener(this);

		Utils.hideSoftkeybord(this);
	}

	private void register() {
		Utils.hideSoftkeybord(this);
		if (!HelperMethods.isNetworkAvailable(this)) {

			HelperMethods.showToast(this,
					this.getResources().getString(R.string.msgNetWorkError));
			return;
		}

		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString().trim();
		String cPassword = etConfirmPassword.getText().toString().trim();
		String android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);

		if (email.equals("") || password.equals("") || cPassword.equals("")) {
			String emptyFields = "";
			if (email.equals("")) {
				emptyFields += "Email ";
			}

			if (password.equals("")) {
				if (email.equals("")) {
					emptyFields += ", ";
				}
				emptyFields += "Password ";
			}

			if (cPassword.equals("")) {

				if (password.equals("") || email.equals("")) {
					emptyFields += ", ";
				}

				emptyFields += "Confirm Password ";
			}
			emptyFields += " fields Required. ";
			alertDialog(this,
					getResources().getString(R.string.msgRegisterErrorTitle),
					emptyFields, "errorRegister");
			emptyFields();
			return;
		}

		if (!HelperMethods.isEmailValid(email)) {
			alertDialog(this,
					getResources().getString(R.string.msgRegisterErrorTitle),
					getResources().getString(R.string.msgInvalidEmail),
					"errorRegister");
			return;
		}

		if (password.length() < MyApplication.globals.getPASSWORD_LENGTH()) {
			alertDialog(this,
					getResources().getString(R.string.msgRegisterErrorTitle),
					getResources().getString(R.string.msgErrorPassworLength),
					"errorRegister");
			emptyFields();
			return;
		}
		if (!password.equals(cPassword)) {
			alertDialog(this,
					getResources().getString(R.string.msgRegisterErrorTitle),
					getResources().getString(R.string.msgErrorPassworMismatch),
					"errorRegister");
			emptyFields();
			return;
		}

		String parmsString = "email=" + URLEncoder.encode(email) + "&password="
				+ URLEncoder.encode(password) + "&userDeviceId="
				+ URLEncoder.encode(android_id) + "&deviceType=android "
				+ HelperMethods.getDeviceName();
		showLoadingDialog(getResources().getString(
				R.string.msgLoadingWebservice));
		Webservices.getData(this, registerUser, parmsString);
	}

	@Override
	public void onSuccessWebservice(JSONObject json, String action) {
		emptyFields();
		try {
			boolean status = (Boolean) json.get("status");
			int userId =  json.getInt("userId");
			cancelLoadingDialog();
			if (status) {
				alertDialog(
						this,
						getResources().getString(
								R.string.msgRegisterSuccessTitle),
						getResources().getString(R.string.msgRegisterSuccess),
						"register");

			} else if (!status && userId > 0) {

				alertDialog(this,
						getResources()
								.getString(R.string.msgRegisterErrorTitle),
						getResources().getString(R.string.msgAlreadyRegister),
						"errorRegister");
			} else {
				alertDialog(
						this,
						getResources()
								.getString(R.string.msgRegisterErrorTitle),
						getResources().getString(R.string.msgLoadingWebservice),
						"errorRegister");
			}

		} catch (Exception e) {
			Log.e(tag + " Exception", "" + e);
		}

	}

	private void emptyFields() {
		etPassword.setText("");
		etConfirmPassword.setText("");
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

		// Setting Dialog Title
		alertDialog1.setTitle(dialogTitle);

		// Setting Dialog Message
		alertDialog1.setMessage(dialogMessage);

		// Setting Icon to Dialog
		// alertDialog1.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (action.equalsIgnoreCase("register")) {

					RegisterAccount.this.onClick(btnBack);
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
		case R.id.btnRegisterd:
			register();
			break;

		case R.id.btnLogin:
			onClick(btnBack);
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
