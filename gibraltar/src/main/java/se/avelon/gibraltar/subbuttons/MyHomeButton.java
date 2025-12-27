package se.avelon.gibraltar.subbuttons;

import com.android.ddmlib.IDevice;

import se.avelon.gibraltar.helpers.MyAdbHelper;
import se.avelon.gibraltar.logs.DLog;

public class MyHomeButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyHomeButton.class);
	
	private IDevice device;

	public MyHomeButton(IDevice device) {
		super("Home", "FileView.floppyDriveIcon");

		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "home");
		
		MyAdbHelper.key(device, 3 /*KEYCODE_HOME*/);
	}
}
