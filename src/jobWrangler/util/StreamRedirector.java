package jobWrangler.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * While running, continually reads strings from the InputStream and writes
 * them to the output stream
 * @author brendanofallon
 *
 */
public class StreamRedirector extends Thread {

	InputStream inpStr;
	PrintStream stream;

	public StreamRedirector(InputStream inpStr, OutputStream stream) {
		this.inpStr = inpStr;
		this.stream = new PrintStream(stream);
	}

	public StreamRedirector(InputStream inpStr, PrintStream stream) {
		this.inpStr = inpStr;
		this.stream = stream;
	}

	public void run() {
		try {
			InputStreamReader inpStrd = new InputStreamReader(inpStr);
			BufferedReader buffRd = new BufferedReader(inpStrd);
			String line = null;
			int count = 0;
			while((line = buffRd.readLine()) != null) {
				if (stream != null) {
					stream.println(line);

					count++;
				}
			}
			buffRd.close();

		} catch(Exception e) {
			System.out.println(e);
		}
	}

}
