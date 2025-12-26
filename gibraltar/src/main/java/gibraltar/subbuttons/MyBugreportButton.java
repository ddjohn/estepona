package gibraltar.subbuttons;


import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import gibraltar.logs.DLog;

public class MyBugreportButton extends MyButton {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyBugreportButton.class);
	
	private IDevice device;

	public MyBugreportButton(IDevice device) {
		super("Bugreport", "OptionPane.questionIcon");

		this.device = device;
	}

	@Override
	public void action() {
		DLog.method(TAG, "bugreport");
		
	    try {
			device.executeShellCommand("bugreport", this);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG, "bugreport", e);
		}
	}
}
