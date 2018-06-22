package pplr.gui.application.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Configuration of reading a file from file system.
 * @author CM
 *
 */
public abstract class FileConfiguration {

	/**
	 * Read a file from the file system
	 * @param f the file
	 * @return list with the content of the file
	 */
	public static ArrayList<String> readFile(File f) {

		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String temp = "";
			while ((temp = br.readLine()) != null) {
				list.add(temp);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
