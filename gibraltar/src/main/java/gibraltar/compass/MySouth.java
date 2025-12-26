package gibraltar.compass;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.android.ddmlib.IDevice;

import gibraltar.Main;
import gibraltar.iface.IMyMainListener;
import gibraltar.logs.DLog;
import gibraltar.subbuttons.MyBackButton;
import gibraltar.subbuttons.MyBugreportButton;
import gibraltar.subbuttons.MyHomeButton;
import gibraltar.subbuttons.MyRebootButton;
import gibraltar.subbuttons.MyRootButton;
import gibraltar.subbuttons.MyUnrootButton;

public class MySouth extends JPanel implements IMyMainListener {
	private static final long serialVersionUID = 1L;

	private static final String TAG = DLog.forTag(MySouth.class);
	
	public MySouth(Main main) {
		DLog.method(TAG, "MySouth()");
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		main.register(this);
	}

	@Override
	public void deviceChange(IDevice device) {
		DLog.method(TAG, "deviceChange(): " + device);

		removeAll();
		
		add(new MyRebootButton(device));
		add(new MyBugreportButton(device));
		add(new MyRootButton(device));
		add(new MyUnrootButton(device));	
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(new JSeparator(JSeparator.VERTICAL));
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(new JSeparator(JSeparator.VERTICAL));
        add(new MyHomeButton(device));		
		add(new MyBackButton(device));	
	}
}