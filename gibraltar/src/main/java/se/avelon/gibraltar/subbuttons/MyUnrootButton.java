package se.avelon.gibraltar.subbuttons;

import java.io.IOException;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import se.avelon.gibraltar.logs.DLog;

public class MyUnrootButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyUnrootButton.class);
	
	private IDevice device;

	public MyUnrootButton(IDevice device) {
		super("Unroot", "Tree.closedIcon");
		
		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "unroot()");
		
	    try {
			device.executeShellCommand("root", new NullOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG, "unroot", e);
		}
	}
}
