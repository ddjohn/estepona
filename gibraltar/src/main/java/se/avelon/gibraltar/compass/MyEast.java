package se.avelon.gibraltar.compass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import se.avelon.gibraltar.Main;
import se.avelon.gibraltar.iface.IMyMainListener;
import se.avelon.gibraltar.logs.DLog;

public class MyEast extends JPanel implements IMyMainListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyEast.class);
	
	private final JTextArea area = new JTextArea("adb> ");
	private IDevice device;
	private String input = new String();
	
	public MyEast(Main main) {
		DLog.method(TAG, "MyEast2");
		
		main.register(this);
		
		setBorder(BorderFactory.createTitledBorder("Shell"));
		setLayout(new BorderLayout());
		
		area.addKeyListener(this);
	    area.getCaret().setVisible(true);
		area.setCaretPosition(area.getText().length());
		area.setPreferredSize(new Dimension(600, 100));
		
		add(new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.EAST);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		DLog.method(TAG, "keyReleased(): " + ke);

		if(ke.getKeyCode() == 10) {
			
			try {
				device.executeShellCommand(input, new MultiLineReceiver() {

					@Override
					public boolean isCancelled() {
						return false;
					}

					@Override
					public void processNewLines(String[] lines) {
						for (String line : lines) {
							area.append("out> " + line + "\r\n");
						}
						
						area.append("adb> ");
						area.setCaretPosition(area.getText().length());						
					}
				});
			} 
			catch(TimeoutException|AdbCommandRejectedException|ShellCommandUnresponsiveException|IOException e) {
				DLog.error(TAG, "shell", e);
			}
			input = "";
		}
		else {
			input += ke.getKeyChar();
		}		
	}


	@Override
	public void keyPressed(KeyEvent ke) {}

	@Override
	public void keyTyped(KeyEvent ke) {}

	@Override
	public void deviceChange(IDevice device) {
		DLog.method(TAG, "deviceChange(): " + device);

		this.device = device;
	}
}
