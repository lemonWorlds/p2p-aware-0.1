package peer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class DiffDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new DiffDriver().launch();
	}

	private void launch() throws Exception {
		List<String> cat = fileToLines("C:\\Users\\Mum\\git\\p2p-aware-0.1\\src\\peer\\cat.txt");
		List<String> dog = fileToLines("C:\\Users\\Mum\\git\\p2p-aware-0.1\\src\\peer\\dog.txt");
		Patch patch = DiffUtils.diff(cat, dog);

        for (Delta delta: patch.getDeltas()) {
                System.out.println(delta);
        }
        patch.applyTo(dog);
        System.out.println(dog);
 	}
	
    private List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
                BufferedReader in = new BufferedReader(new FileReader(filename));
                while ((line = in.readLine()) != null) {
                        lines.add(line);
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return lines;
    }


}
