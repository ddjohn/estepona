package gibraltar;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.android.ddmlib.IDevice;

import gibraltar.subbuttons.MyRebootButton;
import gibraltar.subbuttons.MyRootButton;
import gibraltar.subbuttons.MyUnrootButton;

public class MyBottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MyBottomPanel(IDevice device) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new MyRebootButton(device));
		add(new MyRootButton(device));
		add(new MyUnrootButton(device));
	}
}
