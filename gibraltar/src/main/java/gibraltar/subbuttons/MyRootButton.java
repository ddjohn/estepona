package gibraltar.subbuttons;

import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import gibraltar.logs.DLog;

public class MyRootButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyRootButton.class);
	
	private IDevice device;

	public MyRootButton(IDevice device) {
		super("Root", "Tree.openIcon");
		
		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "root()");
		
	    try {
			device.executeShellCommand("root", new NullOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG, "root", e);
		}
	}
}
