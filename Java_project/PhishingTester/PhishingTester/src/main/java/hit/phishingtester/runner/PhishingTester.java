package hit.phishingtester.runner;

import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import hit.phishingtester.tester.URLTester;

import java.util.Scanner;

public class PhishingTester {

	public static void main(String[] args) {
		String url;
		disableHTMLUnitConsoleWarnings();
		
		try(Scanner inputScanner = new Scanner(System.in)) {
			System.out.println("Please enter a url to check");
			url = inputScanner.nextLine();
			URLTester.run(url);
		}
	}
	
	private static void disableHTMLUnitConsoleWarnings() {
		// Disable HTML Unit console warnings
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
	}
}
