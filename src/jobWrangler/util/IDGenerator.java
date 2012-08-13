package jobWrangler.util;

/**
 * Class to generate random strings used for Job IDs
 * @author brendanofallon
 *
 */
public class IDGenerator {

	/**
	 * Generate a random series of letters and numbers of the given length
	 * @param length
	 * @return
	 */
	public static String generateID(int length) {
		StringBuilder str = new StringBuilder();
		for(int i=0; i<length; i++) {
			char c = chars.charAt( (int)(chars.length()*Math.random()) );
			str.append(c);
		}
		
		return str.toString();
	}
	
	
	public static final String chars = "ABCDEFGHIJKLMNOPQRSTUVQXYZ123456789bcedfghijklmnopqrstuvqxyz";
}
