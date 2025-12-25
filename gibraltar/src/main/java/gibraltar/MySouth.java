package gibraltar;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import com.android.ddmlib.IDevice;
import gibraltar.logs.DLog;
import gibraltar.subbuttons.MyBugreportButton;
import gibraltar.subbuttons.MyRebootButton;
import gibraltar.subbuttons.MyRootButton;
import gibraltar.subbuttons.MyUnrootButton;

public class MySouth extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String TAG = DLog.forTag(MySouth.class);
	
	public MySouth() {
		DLog.method(TAG, "MySouth()");
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setDevice(IDevice device) {
		DLog.method(TAG, "setDevice()");
		
		removeAll();
		
		add(new MyRebootButton(device));
		add(new MyBugreportButton(device));
		add(new MyRootButton(device));
		add(new MyUnrootButton(device));		
	}
}