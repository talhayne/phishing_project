package hit.phishingtester.tester;

import hit.phishingtester.utils.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class URLTester {
	public static void run(String urlToCheck) {
		System.out.println("Going to check " + urlToCheck);
		try {
			/*
			 * Get the FQDN of the URL (for example www.google.co.il out of
			 * https://www.google.co.il/something)
			 */
			String fqdn = HostUtils.extractFQDN(urlToCheck);
			if (fqdn == null) {
				throw new URISyntaxException(urlToCheck, "URL could not be parsed. Please provide a valid URL format");
			}
			
			String sourceURLTitle = HTMLUtils.checkTitle(urlToCheck);
			System.out.println("The source url title is " + sourceURLTitle);
			
			System.out.println("Extracted the fully qualified domain name : " + fqdn);

			// Extract the host name of the FQDN (google)
			String extractedDomain = HostUtils.extractDomain(fqdn);
			System.out.println("Extracted the domain name : " + extractedDomain);

			String extractedHostName = HostUtils.extractHostWithNoSubDomain(extractedDomain);

			// Create a list of possible fake host names
			System.out.println("Creating a list of possible fake host names");
			List<String> possibleFakeHostNames = manipulateHostName(extractedHostName);
			System.out.println("Finished creating a list of possible fake hostnames");

			// Create a list of different FQDNs for each of the hosts
			System.out.println("Create a list of different FQDNs for each of the hosts");
			List<String> possibleFakeDomainNames = createDifferentFQDNs(possibleFakeHostNames);
			System.out.println("Finished creating a list of different FQDNs for each of the hosts");

			// Remove the actual requested domain from the list
			possibleFakeDomainNames.remove(extractedDomain);

			System.out.println("Create a list of responding URLs with HTTP/HTTPS, this might take a while...");
			List<String> possibleFakeURLs = testURLsConnectivity(possibleFakeDomainNames);
			System.out.println("Finished creating a list of responding URLs");

			System.out.println("Checking title for each responding URL");
			String title;
			List<String> matchingTitles = new ArrayList<String>();

			for (String possibleFakeURL : possibleFakeURLs) {
				title = HTMLUtils.checkTitle(possibleFakeURL);
				if(title != null) {
					System.out.println("title for " + possibleFakeURL + " is " + title);
					if (title.equals(sourceURLTitle)) {
						matchingTitles.add(possibleFakeURL);
					}	
				}
			}

//			System.out.println("List of matching titles :");
//			for (String matchingTitleURL : matchingTitles) {
//				System.out.println(matchingTitleURL);
//			}
			
			showResults(matchingTitles);
		} catch (URISyntaxException e) {
			System.out.println("URL could not be parsed. Please provide a valid URL format");
		}
	}

	/**
	 * Manipulates the host name string by several different methods
	 * 
	 * @param hostname
	 * @return a list of several possible manipulations of the host name
	 */
	public static List<String> manipulateHostName(String hostname) {
		List<String> possibleFakeHosts = new ArrayList<String>();

		possibleFakeHosts.add(hostname);

		// Add possible host names with multiplied letters
		possibleFakeHosts.addAll(URLScrambleUtlils.multiplyCommonLetters(hostname));

		// Add possible host names with common strings replaced
		possibleFakeHosts.addAll(URLScrambleUtlils.replaceCommonStrings(hostname));

		return possibleFakeHosts;
	}

	/**
	 * Manipulates the host names by adding different top level domain names
	 * 
	 * @param hostnames
	 * @return a list of fully qualified domain names
	 */
	public static List<String> createDifferentFQDNs(List<String> hostnames) {
		List<String> fqdns = new ArrayList<String>();

		for (String hostname : hostnames) {
			fqdns.addAll(URLScrambleUtlils.hostWithAllTLDs(hostname));
		}

		return fqdns;
	}

	/**
	 * Checks for the fqdns HTTP/HTTPS connectivity
	 * 
	 * @param fqdns
	 * @return a list of URLs that respond to HTTP/HTTPS
	 */
	public static List<String> testURLsConnectivity(List<String> fqdns) {
		List<String> respondingURLs = new ArrayList<String>();
		boolean didHTTPSucceed = false;
		
		// Check for HTTP and HTTPs connectivity
		for (String fqdn : fqdns) {
			System.out.println("Checking HTTP " + fqdn);
			if (HostUtils.checkHttpAvailability(fqdn)) {
				System.out.println("HTTP Connected! " + fqdn);
				didHTTPSucceed = true;
				respondingURLs.add(URLScrambleUtlils.addHttpProtocol(fqdn));
			}
			// Checking HTTPS if HTTP is not available
			if (didHTTPSucceed == false) {
				System.out.println("Checking HTTPS " + fqdn);
				if (HostUtils.checkHttpsAvailability(fqdn)) {
					System.out.println("HTTPS Connected! " + fqdn);
					respondingURLs.add(URLScrambleUtlils.addHttpsProtocol(fqdn));
				}
			}
			
			didHTTPSucceed = false;
		}

		return respondingURLs;
	}

	private static void showResults(List<String> results) {
		StringBuilder sb = new StringBuilder();
		
		for(String result : results) {
			sb.append(result);
			sb.append(System.getProperty("line.separator"));
		}
		
		SwingUtils.popUpMessage("Matching titles", sb.toString());
	}
}
