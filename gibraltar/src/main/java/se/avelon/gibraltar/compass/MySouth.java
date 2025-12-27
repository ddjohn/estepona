package se.avelon.gibraltar.compass;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.android.ddmlib.IDevice;

import se.avelon.gibraltar.Main;
import se.avelon.gibraltar.iface.IMyMainListener;
import se.avelon.gibraltar.logs.DLog;
import se.avelon.gibraltar.subbuttons.MyBackButton;
import se.avelon.gibraltar.subbuttons.MyBugreportButton;
import se.avelon.gibraltar.subbuttons.MyHomeButton;
import se.avelon.gibraltar.subbuttons.MyRebootButton;
import se.avelon.gibraltar.subbuttons.MyRootButton;
import se.avelon.gibraltar.subbuttons.MyUnrootButton;

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