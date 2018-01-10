package hit.phishingtester.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class URLScrambleUtlils {

	private static final String TLD_FILE_PATH = "resources/tld.txt";
	private static final List<String> topLevelDomains;
	private static final String[] commonLetters = { "a", "i", "e", "o", "u", "y", "h", "w" };
	private static final Map<String, String> commonReplacements;
	
	private URLScrambleUtlils() {
		
	}
	
	static {
		// Load Top Level Domains file
		Path tldFile = Paths.get(TLD_FILE_PATH);
		List<String> allLines = null;
		try {
			allLines = Files.readAllLines(tldFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		topLevelDomains = allLines;
		
		// Load common strings replacements
		commonReplacements = new HashMap<String, String>();
		commonReplacements.put("o", "oo");
		commonReplacements.put("oo", "u");
		commonReplacements.put("u", "oo");
		commonReplacements.put("sh", "ch");
		commonReplacements.put("ch", "sh");
		commonReplacements.put("q", "k");
		commonReplacements.put("y", "i");
		commonReplacements.put("i", "y");
		commonReplacements.put("ee", "i");
		commonReplacements.put("i", "ee");
		commonReplacements.put("e", "y");
		commonReplacements.put("y", "e");
		commonReplacements.put("i", "e");
		commonReplacements.put("e", "i");
	}
	
	public static List<String> hostWithAllTLDs(String host) {
		List<String> result = new ArrayList<String>();
		
		for(String tld : topLevelDomains) {
			result.add(host + '.' + tld);
		}
		
		return result;
	}
	
	/**
	 * Multiplies common letters in the host
	 * @param host
	 * @return list of strings with letters multiplied
	 */
	public static List<String> multiplyCommonLetters(String host) {
		List<String> result = new ArrayList<String>();
		String replacement = null;
		String doubleLetter = null;
		
		for (String replaceChar : commonLetters) {
			doubleLetter = replaceChar + replaceChar;
			replacement = host.replace(replaceChar, doubleLetter);

			// A replacement was actually made
			if (!replacement.equals(host)) {
				result.add(replacement);
			}
		}

		return result;
	}
	
	/**
	 * Replaces strings with common replacement possibilities
	 * @param host
	 * @return list of strings with common strings replaced
	 */
	public static List<String> replaceCommonStrings(String host) {
		List<String> result = new ArrayList<String>();
		String replacement;
		
		for(Entry<String, String> commonReplacement : commonReplacements.entrySet()) {
			replacement = host.replace(commonReplacement.getKey(), commonReplacement.getValue());

			// A replacement was actually made
			if (!replacement.equals(host)) {
				result.add(replacement);
			}
		}
		
		return result;
	}
	
	public static String addHttpProtocol(String host) {
		return "http://" + host;
	}
	
	public static String addHttpsProtocol(String host) {
		return "https://" + host;
	}
	
	public static List<String> addDifferentProtocols(String host) {
		List<String> result = new ArrayList<String>();
		
		result.add(addHttpsProtocol(host));
		result.add(addHttpsProtocol(host));
		
		return result;
	}
}
