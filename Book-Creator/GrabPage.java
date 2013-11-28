import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;


public class GrabPage {

	private static int bottomBookRange = 46;
	private static int topBookRange = 72;
	
	public static void main(String args[]) {
		final String page = "Chapter 1 - Artificial Intelligence and Agents";
		for (int i = bottomBookRange; i < topBookRange; i++) {

			
			try {
				String webpageURL = "http://artint.info/html/ArtInt_" + i
						+ ".html";
				URL webURL = new URL(webpageURL);
				webURL.openConnection();

				final File f = new File(page+i+".txt");
				PrintWriter pw = null;
				
				try {
					pw = new PrintWriter(f);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						webURL.openStream()));

				String ln = "";

				while ((ln = br.readLine()) != null) {
					pw.println(ln);
				}

				if (ln == null) {
					System.out.println("Downloaded " + webpageURL
							+ " and saved it to " + webpageURL);
				}

				pw.close();
				br.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
}
