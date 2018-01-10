package hit.phishingtester.utils;

import javax.swing.JOptionPane;

public class SwingUtils {
	
    public static void popUpMessage(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
