package suave.ms.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutMS extends MainActivity implements OnClickListener {

	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template);

		activeActivityContext = this;

		TextView tvTopMenuTitle = (TextView) findViewById(R.id.tvTopMenuTitle);
		tvTopMenuTitle.setText(getString(R.string.txtAboutms));

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setVisibility(View.GONE);

		TextView aboutTextView = new TextView(this);
		aboutTextView.setText(getString(R.string.aboutText));

		aboutTextView.setId(5);
		aboutTextView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		aboutTextView.setSingleLine(false);

		ScrollView scroll = new ScrollView(this);

		scroll.setPadding(10, 10, 10, 10);
		scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		scroll.addView(aboutTextView);

		RelativeLayout fragmentContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
		fragmentContainer.addView(scroll);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Goto Last Activity
		Intent intent = new Intent(AboutMS.this, MSTrackerHome.class);
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
}
