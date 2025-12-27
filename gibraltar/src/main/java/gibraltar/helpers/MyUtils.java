package gibraltar.helpers;

import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gibraltar.logs.DLog;

public class MyUtils {
	private static final String TAG = DLog.forTag(MyUtils.class);

	public static boolean sleep(int ms) {
		try {
			Thread.sleep(ms);
			return true;
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}	
	}

	public static void lookAndFeel(String lnf) {
		UIManager.put("TextField.font", new Font("Serif", Font.PLAIN, 36));
		UIManager.put("Menu.font", new Font("Serif", Font.PLAIN, 36));
		UIManager.put("MenuItem.font", new Font("Serif", Font.PLAIN, 36));
		UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 36));
		UIManager.put("TextArea.font", new Font("Courier", Font.PLAIN, 24));
		UIManager.put("TitledBorder.font",  new Font("Serif", Font.PLAIN, 18));
		
		UIManager.put("OptionPane.font",  new Font("Serif", Font.PLAIN, 36));
		
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks) {
            DLog.info(TAG, "lookNfeel: " + look.getClassName());
        }

		try {
			UIManager.setLookAndFeel(lnf);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			DLog.error(TAG, "look n feel", e);
		}
/*
			//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.createLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			 */
	}
}
