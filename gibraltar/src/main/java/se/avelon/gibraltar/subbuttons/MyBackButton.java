package se.avelon.gibraltar.subbuttons;

import com.android.ddmlib.IDevice;

import se.avelon.gibraltar.helpers.MyAdbHelper;
import se.avelon.gibraltar.logs.DLog;

public class MyBackButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyBackButton.class);
	
	private IDevice device;

	public MyBackButton(IDevice device) {
		super("Back", "FileView.floppyDriveIcon");

		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "back");
		
		MyAdbHelper.key(device, 4 /*KEYCODE_BACK*/);
	}
}
