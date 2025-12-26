package gibraltar.subbuttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.android.ddmlib.IShellOutputReceiver;

import gibraltar.MyFontButton;
import gibraltar.logs.DLog;

public abstract class MyButton extends MyFontButton implements ActionListener, IShellOutputReceiver  {
	private static final long serialVersionUID = 1L;
	private static String TAG = DLog.forTag(MyButton.class);

	public MyButton(String title, String iconName) {
		super(title);

		addActionListener(this);
		setIcon(UIManager.getIcon(iconName));
	}

	public abstract void action();
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		DLog.method(TAG, "actionPerformed(): " + ae);
		
	    action();	
	}
	

	@Override
	public void addOutput(byte[] data, int offset, int length) {
		//DLog.method(TAG, "addOutput(): " + Arrays.toString(data));
		DLog.method(TAG, "addOutput(): " + new String(data, offset, length));
		DLog.method(TAG, "addOutput(): " + offset + "/" + length);
	}

	@Override
	public void flush() {
		DLog.method(TAG, "flush()");
	}

	@Override
	public boolean isCancelled() {
		DLog.method(TAG, "isCancelled()");
		return false;
	}
}

/*
UIManager.getIcon("FileView.directoryIcon");
UIManager.getIcon("FileView.fileIcon");
UIManager.getIcon("FileView.computerIcon");
UIManager.getIcon("FileView.hardDriveIcon");
UIManager.getIcon("FileView.floppyDriveIcon");

UIManager.getIcon("FileChooser.newFolderIcon");
UIManager.getIcon("FileChooser.upFolderIcon");
UIManager.getIcon("FileChooser.homeFolderIcon");
UIManager.getIcon("FileChooser.detailsViewIcon");
UIManager.getIcon("FileChooser.listViewIcon");

UIManager.getIcon("Tree.collapsedIcon");
UIManager.getIcon("Tree.openIcon");
UIManager.getIcon("Tree.leafIcon");
UIManager.getIcon("Tree.closedIcon");

UIManager.getIcon("OptionPane.questionIcon");
UIManager.getIcon("OptionPane.errorIcon");
UIManager.getIcon("OptionPane.informationIcon");
UIManager.getIcon("OptionPane.warningIcon");
*/