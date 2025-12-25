package gibraltar;

import java.awt.Font;
import javax.swing.JButton;

public class MyFontButton extends JButton {
	private static final long serialVersionUID = 1L;

	public MyFontButton(String text) {
		super(text);
		
		setFont(new Font("Arial", Font.PLAIN, 40));
	}
}
