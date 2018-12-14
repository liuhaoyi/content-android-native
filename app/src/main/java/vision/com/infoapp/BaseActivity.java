package vision.com.infoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class BaseActivity extends Activity {

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		new AlertDialog.Builder(BaseActivity.this).setTitle("标题").setMessage(
				"message").setPositiveButton("PositiveButton",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Globals.EXIT = true;
						Globals.IS_LOGIN=false;
						finish();
					}
				}).setNegativeButton("NegativeButton",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Globals.EXIT = false;
						}
					}	
				).show();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Globals.EXIT ) {
			finish();
		}
	}
}
