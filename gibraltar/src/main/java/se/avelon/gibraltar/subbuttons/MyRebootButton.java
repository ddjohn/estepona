package se.avelon.gibraltar.subbuttons;

import java.io.IOException;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import se.avelon.gibraltar.logs.DLog;

public class MyRebootButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyRebootButton.class);
	
	private IDevice device;

	public MyRebootButton(IDevice device) {
		super("Reboot", "FileView.floppyDriveIcon");

		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "reboot");
		
	    try {
			device.executeShellCommand("reboot", this);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG, "reboot", e);
		}
	}

}
