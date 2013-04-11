package wranglerView.server.auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wranglerView.logging.WLogger;
import wranglerView.server.WranglerProperties;

/**
 * Interface to a password file. We use jBCrypt to encrypt passwords
 * @author brendan
 *
 */
public class PasswordStore {

	private static final String passwordFilename = "jw.passwd";
	private static final String pathToPasswordFile = WranglerProperties.getWranglerRoot() + "/" + passwordFilename;
	
	public PasswordStore() {
		
	}
	
	public static boolean checkPassword(String username, String candidatePassword) {
		try {
			Map<String, String> passwords = loadPasswordFile();
			String hashed = passwords.get(username);
			if (BCrypt.checkpw(candidatePassword, hashed))
				return true;
			else
				return false;
			
		} catch (IOException e) {
			e.printStackTrace();
			WLogger.severe("Could not open password file : " + e.getMessage() );
			return false;
		}	
	}

	private static Map<String, String> loadPasswordFile() throws IOException {
		WLogger.info("Loading password file from : " + pathToPasswordFile);
		BufferedReader reader = new BufferedReader(new FileReader(pathToPasswordFile));
		String line = reader.readLine();
		Map<String, String> map = new HashMap<String, String>();
		while(line != null) {
			int index = line.indexOf(":");
			if (index < 0) {
				WLogger.warn("Invalid line found in password file : " + line);
			}
			else {
				String username = line.substring(0, index);
				String hashed = line.substring(index+1, line.length());
				map.put(username, hashed);
			}
			line = reader.readLine();
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) {
		if (args.length==0) {
			System.out.println("Enter a password to hash");
			return;
		}
		String hashed = BCrypt.hashpw(args[0], BCrypt.gensalt());
		System.out.println( hashed );
	}
	

}

