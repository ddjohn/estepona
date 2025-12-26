package gibraltar.helpers;

import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import gibraltar.logs.DLog;

public class MyAdbHelper {
	private static final String TAG = DLog.forTag(MyAdbHelper.class);
	
    public static void tap(IDevice device, int x, int y) {
        String cmd = String.format("input tap %d %d", x, y);
        try {
			device.executeShellCommand(cmd, new NullOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG,  "tap", e);
		}
    }

    public static void key(IDevice device, int keycode) {
        String cmd = String.format("input keyboard keyevent %d", keycode);
        try {
			device.executeShellCommand(cmd, new NullOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG,  "tap", e);
		}
    }
}
