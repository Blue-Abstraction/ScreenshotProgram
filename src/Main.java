import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.SwingUtilities;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
	
	private static String file = "";
	
	public static void main(String[] args) throws AWTException, NativeHookException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))){
			reader.readLine();
			file = reader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//System.out.println(file);
		
		SwingUtilities.invokeLater(() -> {
			try {
				new Window();
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (NativeHookException e) {
				e.printStackTrace();
			}
		});
	}

	public static String getFile() {
		return file;
	}
}